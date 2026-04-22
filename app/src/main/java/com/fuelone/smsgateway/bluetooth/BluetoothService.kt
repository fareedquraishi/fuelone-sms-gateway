package com.fuelone.smsgateway.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.util.Log
import com.fuelone.smsgateway.model.SendRequest
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID

/**
 * BluetoothService — RFCOMM Server
 *
 * Listens for incoming Bluetooth RFCOMM connections from the PC bridge script.
 * Protocol: newline-delimited JSON over RFCOMM socket.
 *
 * Message format (PC → Android):
 *   {"type":"send","phone":"...","message":"...","message_id":"...","customer_name":"..."}
 *
 * Message format (Android → PC):
 *   {"type":"response","success":true,"message_id":"...","timestamp":...}
 *   {"type":"inbox","messages":[...]}
 *   {"type":"status","mode":"BLUETOOTH",...}
 *   {"type":"inbound","from":"...","message":"...","timestamp":...}
 */
@SuppressLint("MissingPermission")
class BluetoothService(
    private val context: Context,
    private val smsSender: (SendRequest) -> Boolean,
    private val statusJson: () -> String,
    private val inboxJson: () -> String
) {

    companion object {
        private const val TAG = "BluetoothService"
        // Standard UUID for SPP (Serial Port Profile)
        val BT_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
        const val BT_SERVICE_NAME = "FuelOneSmsGateway"
    }

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var serverSocket: BluetoothServerSocket? = null
    private var clientSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        (context.getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)?.adapter
    }

    var isRunning = false
        private set

    // ─── Start RFCOMM Server ──────────────────────────────────────────────────
    fun start() {
        if (isRunning) return
        val adapter = bluetoothAdapter ?: run {
            Log.e(TAG, "No Bluetooth adapter available")
            return
        }
        if (!adapter.isEnabled) {
            Log.e(TAG, "Bluetooth is not enabled")
            return
        }

        isRunning = true
        scope.launch { acceptLoop(adapter) }
        Log.i(TAG, "Bluetooth RFCOMM server started")
    }

    // ─── Accept Loop ─────────────────────────────────────────────────────────
    private suspend fun acceptLoop(adapter: BluetoothAdapter) {
        while (isRunning) {
            try {
                serverSocket = adapter.listenUsingRfcommWithServiceRecord(BT_SERVICE_NAME, BT_UUID)
                Log.d(TAG, "Waiting for Bluetooth connection...")

                val socket = serverSocket!!.accept() // blocking
                clientSocket = socket
                serverSocket?.close() // only one client at a time
                Log.i(TAG, "BT client connected: ${socket.remoteDevice.name}")

                handleClient(socket)

            } catch (e: IOException) {
                if (isRunning) Log.e(TAG, "Accept error: ${e.message}")
                delay(3000) // wait before re-listening
            }
        }
    }

    // ─── Handle Connected Client ──────────────────────────────────────────────
    private suspend fun handleClient(socket: BluetoothSocket) = withContext(Dispatchers.IO) {
        val inputStream: InputStream = socket.inputStream
        outputStream = socket.outputStream
        val buffer = StringBuilder()

        try {
            val bytes = ByteArray(1024)
            while (isRunning && socket.isConnected) {
                val count = inputStream.read(bytes)
                if (count == -1) break
                buffer.append(String(bytes, 0, count))

                // Process complete lines (newline-delimited protocol)
                while (buffer.contains('\n')) {
                    val newlineIdx = buffer.indexOf('\n')
                    val line       = buffer.substring(0, newlineIdx).trim()
                    buffer.delete(0, newlineIdx + 1)
                    if (line.isNotEmpty()) processCommand(line)
                }
            }
        } catch (e: IOException) {
            Log.d(TAG, "BT client disconnected: ${e.message}")
        } finally {
            outputStream = null
            clientSocket = null
            socket.close()
        }
    }

    // ─── Process Command from PC ──────────────────────────────────────────────
    private fun processCommand(json: String) {
        try {
            val cmd = gson.fromJson(json, Map::class.java)
            when (cmd["type"] as? String) {
                "send" -> {
                    val req = SendRequest(
                        phone        = cmd["phone"] as? String ?: "",
                        message      = cmd["message"] as? String ?: "",
                        messageId    = cmd["message_id"] as? String ?: "",
                        customerName = cmd["customer_name"] as? String ?: ""
                    )
                    val success = smsSender(req)
                    sendToPc("""{"type":"response","success":$success,"message_id":"${req.messageId}","timestamp":${System.currentTimeMillis()}}""")
                }
                "status" -> sendToPc("""{"type":"status_response","data":${statusJson()}}""")
                "inbox"  -> sendToPc("""{"type":"inbox_response","data":${inboxJson()}}""")
                "ping"   -> sendToPc("""{"type":"pong","timestamp":${System.currentTimeMillis()}}""")
                else     -> Log.w(TAG, "Unknown BT command: ${cmd["type"]}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Command parse error: ${e.message}")
        }
    }

    // ─── Send to PC ───────────────────────────────────────────────────────────
    fun sendToPc(json: String) {
        try {
            outputStream?.write("$json\n".toByteArray())
            outputStream?.flush()
        } catch (e: IOException) {
            Log.e(TAG, "BT write error: ${e.message}")
        }
    }

    /** Notify PC bridge about a newly received inbound SMS */
    fun notifyInbound(from: String, message: String, keywordMatched: String?) {
        val payload = """{"type":"inbound","from":"$from","message":"${message.replace("\"","\\\"")}", "keyword_matched":"${keywordMatched ?: ""}","timestamp":${System.currentTimeMillis()}}"""
        sendToPc(payload)
    }

    // ─── Stop ─────────────────────────────────────────────────────────────────
    fun stop() {
        isRunning = false
        scope.cancel()
        clientSocket?.close()
        serverSocket?.close()
        Log.i(TAG, "Bluetooth service stopped")
    }
}
