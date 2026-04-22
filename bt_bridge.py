#!/usr/bin/env python3
"""
bt_bridge.py — FuelOne SMS Gateway PC Bluetooth Bridge
========================================================
Runs on the PC / server side. Connects to the Android phone via
Bluetooth RFCOMM and exposes a local HTTP API that the dashboard
can call exactly as if it were talking to the WiFi/USB server.

Usage:
    pip install PyBluez flask requests
    python bt_bridge.py --device AA:BB:CC:DD:EE:FF --port 8080

Then configure the dashboard to point to http://localhost:8080

Requirements:
    pip install PyBluez flask requests
    On Windows: also install pybluez2 or use the pre-built wheel
    On Linux:   sudo apt install bluetooth libbluetooth-dev

Architecture:
    Dashboard → HTTP → bt_bridge (this script) → BT RFCOMM → Android Phone → SmsManager
    Android Phone → BT RFCOMM → bt_bridge → (stored locally for /inbox polling)
"""

import argparse
import json
import logging
import queue
import socket
import threading
import time
import uuid
from datetime import datetime
from typing import Optional

try:
    import bluetooth  # PyBluez
    BT_AVAILABLE = True
except ImportError:
    BT_AVAILABLE = False
    print("WARNING: PyBluez not installed. Run: pip install PyBluez")

from flask import Flask, request, jsonify

# ─── Config ───────────────────────────────────────────────────────────────────
BT_UUID        = "00001101-0000-1000-8000-00805F9B34FB"  # SPP UUID — must match Android
RECONNECT_DELAY = 5   # seconds between reconnect attempts
SEND_TIMEOUT    = 15  # seconds to wait for send response

logging.basicConfig(
    level=logging.INFO,
    format="%(asctime)s [%(levelname)s] %(name)s — %(message)s"
)
log = logging.getLogger("bt_bridge")

# ─── State ────────────────────────────────────────────────────────────────────
app              = Flask(__name__)
bt_socket: Optional[socket.socket] = None
bt_lock          = threading.Lock()
response_queues  = {}   # message_id → queue for blocking send
inbox_messages   = []   # in-memory inbox (last 200)
gateway_status   = {}   # last status from Android
secret_key       = ""   # set via --key arg

# ─── Bluetooth Connection Manager ─────────────────────────────────────────────
def bt_connect(device_address: str):
    """Blocking connect loop — reconnects on disconnect."""
    global bt_socket
    while True:
        try:
            log.info(f"Connecting to {device_address} ...")
            sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
            sock.connect((device_address, bluetooth.find_service(
                uuid=BT_UUID, address=device_address
            )[0]["port"] if BT_AVAILABLE else 1))
            with bt_lock:
                bt_socket = sock
            log.info("Bluetooth connected ✓")
            receive_loop(sock)
        except Exception as e:
            log.error(f"BT connection error: {e}")
            with bt_lock:
                bt_socket = None
            time.sleep(RECONNECT_DELAY)


def receive_loop(sock):
    """Read newline-delimited JSON from Android."""
    buffer = ""
    try:
        while True:
            data = sock.recv(4096).decode("utf-8", errors="replace")
            if not data:
                break
            buffer += data
            while "\n" in buffer:
                line, buffer = buffer.split("\n", 1)
                line = line.strip()
                if line:
                    handle_bt_message(line)
    except Exception as e:
        log.warning(f"BT receive error: {e}")


def handle_bt_message(raw: str):
    """Dispatch incoming JSON from Android."""
    global gateway_status
    try:
        msg = json.loads(raw)
        t   = msg.get("type")

        if t == "response":
            # Response to a /send or /reply command
            mid = msg.get("message_id")
            if mid and mid in response_queues:
                response_queues[mid].put(msg)

        elif t == "inbound":
            # New SMS received on Android SIM
            entry = {
                "from":           msg.get("from"),
                "message":        msg.get("message"),
                "timestamp":      msg.get("timestamp", int(time.time() * 1000)),
                "read":           False,
                "keyword_matched": msg.get("keyword_matched"),
                "auto_reply_sent": bool(msg.get("keyword_matched"))
            }
            inbox_messages.insert(0, entry)
            if len(inbox_messages) > 200:
                inbox_messages.pop()
            log.info(f"Inbound SMS from {entry['from']}: {entry['message'][:60]}")

        elif t == "status_response":
            gateway_status = msg.get("data", {})

        elif t == "pong":
            log.debug("BT pong received")

        else:
            log.debug(f"Unknown BT message type: {t}")

    except json.JSONDecodeError as e:
        log.warning(f"JSON parse error: {e} — raw: {raw[:200]}")


def bt_send(payload: dict) -> bool:
    """Thread-safe write to BT socket."""
    with bt_lock:
        sock = bt_socket
    if not sock:
        return False
    try:
        sock.send((json.dumps(payload) + "\n").encode())
        return True
    except Exception as e:
        log.error(f"BT send error: {e}")
        return False


def bt_send_and_wait(payload: dict, message_id: str, timeout: int = SEND_TIMEOUT) -> dict:
    """Send command and block until response or timeout."""
    q = queue.Queue()
    response_queues[message_id] = q
    try:
        if not bt_send(payload):
            return {"success": False, "error": "Bluetooth not connected"}
        return q.get(timeout=timeout)
    except queue.Empty:
        return {"success": False, "error": "Timeout waiting for response from Android"}
    finally:
        response_queues.pop(message_id, None)

# ─── Auth Middleware ──────────────────────────────────────────────────────────
def check_auth():
    if not secret_key:
        return True  # No key set — open
    auth = request.headers.get("Authorization", "")
    return auth == f"Bearer {secret_key}"

# ─── HTTP API Endpoints ───────────────────────────────────────────────────────
@app.post("/send")
def api_send():
    if not check_auth():
        return jsonify({"error": "Unauthorized"}), 401

    body = request.get_json(force=True, silent=True) or {}
    phone    = body.get("phone", "").strip()
    message  = body.get("message", "").strip()
    msg_id   = body.get("message_id") or str(uuid.uuid4())
    cust_name = body.get("customer_name", "")

    if not phone or not message:
        return jsonify({"error": "phone and message required"}), 400

    result = bt_send_and_wait({
        "type":          "send",
        "phone":         phone,
        "message":       message,
        "message_id":    msg_id,
        "customer_name": cust_name
    }, msg_id)

    status = 200 if result.get("success") else 500
    return jsonify({
        "success":    result.get("success", False),
        "message_id": msg_id,
        "timestamp":  int(time.time() * 1000),
        "error":      result.get("error")
    }), status


@app.get("/status")
def api_status():
    # Request fresh status from Android
    bt_send({"type": "status"})
    time.sleep(0.5)  # small wait for response
    status = gateway_status or {
        "mode":               "BLUETOOTH",
        "ip":                 "N/A",
        "port":               0,
        "messages_sent_today": 0,
        "queue_size":         0,
        "battery":            -1,
        "signal_strength":    -1,
        "bt_connected":       bt_socket is not None
    }
    status["bt_bridge"] = True
    status["bt_connected"] = bt_socket is not None
    return jsonify(status)


@app.get("/inbox")
def api_inbox():
    return jsonify(inbox_messages[:50])


@app.post("/reply")
def api_reply():
    if not check_auth():
        return jsonify({"error": "Unauthorized"}), 401

    body    = request.get_json(force=True, silent=True) or {}
    phone   = body.get("phone", "")
    message = body.get("message", "")
    msg_id  = body.get("message_id") or str(uuid.uuid4())

    result = bt_send_and_wait({
        "type":       "send",
        "phone":      phone,
        "message":    message,
        "message_id": msg_id
    }, msg_id)

    return jsonify({
        "success":    result.get("success", False),
        "message_id": msg_id,
        "timestamp":  int(time.time() * 1000)
    })


@app.get("/")
def api_root():
    return jsonify({
        "service":      "FuelOne SMS Gateway — BT Bridge",
        "version":      "1.0.0",
        "bt_connected": bt_socket is not None,
        "endpoints":    ["/send", "/status", "/inbox", "/reply"]
    })

# ─── Ping thread ─────────────────────────────────────────────────────────────
def ping_loop():
    while True:
        time.sleep(30)
        bt_send({"type": "ping"})

# ─── Entry Point ─────────────────────────────────────────────────────────────
def main():
    global secret_key

    parser = argparse.ArgumentParser(description="FuelOne BT Bridge")
    parser.add_argument("--device", required=True,   help="Android phone BT MAC address (AA:BB:CC:DD:EE:FF)")
    parser.add_argument("--port",   default=8080,    type=int, help="Local HTTP port (default 8080)")
    parser.add_argument("--key",    default="",      help="Secret key for Bearer auth")
    parser.add_argument("--scan",   action="store_true", help="Scan for nearby BT devices and exit")
    args = parser.parse_args()

    if args.scan:
        if not BT_AVAILABLE:
            print("PyBluez not installed"); return
        print("Scanning for Bluetooth devices...")
        devices = bluetooth.discover_devices(lookup_names=True)
        for addr, name in devices:
            print(f"  {addr}  —  {name}")
        return

    secret_key = args.key

    if not BT_AVAILABLE:
        print("ERROR: PyBluez not installed. Run: pip install PyBluez")
        return

    # Start BT connection thread
    bt_thread = threading.Thread(target=bt_connect, args=(args.device,), daemon=True)
    bt_thread.start()

    # Start ping thread
    ping_thread = threading.Thread(target=ping_loop, daemon=True)
    ping_thread.start()

    log.info(f"Starting HTTP bridge on port {args.port}")
    app.run(host="0.0.0.0", port=args.port, threaded=True)


if __name__ == "__main__":
    main()
