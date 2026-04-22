# FuelOne SMS Gateway — Android App

> Persistent SMS sender/receiver bridge for the FuelOne / Petronas Auto Expert Centre ERP system.
> Runs as a foreground service on a dedicated Android phone and exposes an HTTP API consumed by the web dashboard.

---

## Project Structure

```
SmsGateway/
├── app/src/main/
│   ├── AndroidManifest.xml
│   └── java/com/fuelone/smsgateway/
│       ├── GatewayApplication.kt
│       ├── bluetooth/
│       │   └── BluetoothService.kt        # RFCOMM server
│       ├── data/
│       │   ├── AppPreferences.kt          # SharedPreferences wrapper
│       │   ├── Database.kt                # Room DB (MessageEntity, DAO)
│       │   └── SupabaseRepository.kt      # Supabase REST client
│       ├── engine/
│       │   └── AutoReplyEngine.kt         # Keyword matching
│       ├── model/
│       │   └── Models.kt                  # Data classes
│       ├── receiver/
│       │   ├── BootReceiver.kt            # Auto-start on boot
│       │   ├── SmsReceiver.kt             # Intercept incoming SMS
│       │   └── SmsStatusReceiver.kt       # Delivery reports
│       ├── server/
│       │   └── ApiServer.kt               # NanoHTTPD HTTP server
│       ├── service/
│       │   └── SmsGatewayService.kt       # Core foreground service
│       ├── ui/
│       │   ├── MainActivity.kt
│       │   ├── SettingsActivity.kt
│       │   └── InboxActivity.kt
│       └── util/
│           └── NetworkUtil.kt
├── bt_bridge.py                           # PC Bluetooth bridge (Python)
├── supabase_schema.sql                    # Run once in Supabase SQL Editor
└── build.gradle
```

---

## Quick Start

### 1. Build & Install
```bash
# Open in Android Studio → Build → Generate Signed APK
# Or: ./gradlew assembleDebug
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 2. Supabase Setup
```sql
-- Run supabase_schema.sql in your Supabase SQL Editor
```

### 3. App Configuration
Open the app → Settings:

| Field | Value |
|---|---|
| Connection Mode | WiFi (recommended) |
| Secret Key | your-secret-token |
| Port | 8080 |
| Supabase URL | https://iageuvrhveeptvyjiavy.supabase.co |
| Supabase Anon Key | your-anon-key |
| Webhook URL | https://your-dashboard.com/api/webhook |

### 4. Start Gateway
Toggle **"SMS Gateway"** switch on the main screen.

The persistent notification will show:
```
🟢 Gateway Active — WIFI
Sent today: 0 | Unread: 0 | 192.168.1.x:8080
```

---

## HTTP API

All endpoints return `Content-Type: application/json`.

### POST /send
Send an outbound SMS.

**Headers:**
```
Authorization: Bearer your-secret-token
Content-Type: application/json
```

**Body:**
```json
{
  "phone": "+60123456789",
  "message": "Thank you for visiting Euro-5!\nReply 1 for Oil Change\nReply 2 for Gear Oil\nReply 3 for Full Service",
  "message_id": "msg_001",
  "customer_name": "Ahmad bin Ali"
}
```

**Response:**
```json
{
  "success": true,
  "message_id": "msg_001",
  "timestamp": 1718000000000,
  "error": null
}
```

---

### GET /status
```json
{
  "mode": "WIFI",
  "ip": "192.168.1.105",
  "port": 8080,
  "messages_sent_today": 42,
  "queue_size": 0,
  "battery": 87,
  "signal_strength": 3
}
```

---

### GET /inbox
Returns last 50 received SMS messages.
```json
[
  {
    "from": "+60123456789",
    "message": "1",
    "timestamp": 1718000000000,
    "read": false,
    "keyword_matched": "1",
    "auto_reply_sent": true
  }
]
```

---

### POST /reply
```json
{
  "phone": "+60123456789",
  "message": "Our BA will contact you shortly.",
  "message_id": "reply_001"
}
```

---

## Connection Modes

### WiFi (Recommended)
Dashboard → `http://<phone-ip>:8080`

The phone's IP is shown on the main screen and in the notification.
Ensure phone and PC are on the **same WiFi network**.

### USB (adb forward)
Run once on PC:
```bash
adb forward tcp:8080 tcp:8080
```
Dashboard → `http://localhost:8080`

No network needed. Reliable for static setups.

### Bluetooth
1. Pair the Android phone with your PC via system Bluetooth settings.
2. Install the Python bridge on PC:
   ```bash
   pip install PyBluez flask
   ```
3. Find the phone's BT MAC address:
   ```bash
   python bt_bridge.py --scan
   ```
4. Start the bridge:
   ```bash
   python bt_bridge.py --device AA:BB:CC:DD:EE:FF --port 8080 --key your-secret-token
   ```
5. Dashboard → `http://localhost:8080` (talks to bridge, which relays to phone)

---

## Auto-Reply System

### Flow
```
Customer receives campaign SMS
        ↓
Customer replies: "1"
        ↓
Android SIM receives SMS
        ↓
SmsReceiver.kt catches it
        ↓
AutoReplyEngine checks configured rules
        ↓
Rule matched: keyword="1"
        ↓
Sends auto-reply: "Great! Your Engine Oil service is booked..."
        ↓
Both messages logged to Supabase message_log
        ↓
Webhook POSTed to dashboard
        ↓
Dashboard Inbox tab shows conversation
```

### Default Rules
| Keyword | Auto-Reply |
|---|---|
| 1 | Engine Oil service booked. BA will contact you. |
| 2 | Gear Oil service booked. BA will contact you. |
| 3 | Full Service booked. BA will contact you. |
| YES | Interest noted. BA will be in touch. |
| NO | Thank you! Hope to serve you next time. |

Configure in Settings → Auto-Reply Rules.

Use `{phone}` in the reply text to include the customer's number.

---

## Message Queue & Retry

- **Queue size:** Unlimited in-memory
- **Max retries:** 3
- **Retry interval:** 30 seconds
- **On failure after 3 attempts:** Logged as `status=failed` in Supabase

---

## Supabase Schema

```sql
message_log (
  id              uuid PRIMARY KEY,
  direction       text,        -- 'inbound' | 'outbound'
  phone           text,
  message         text,
  status          text,        -- sent|failed|delivered|received|pending
  customer_name   text,
  message_id      text,
  gateway_mode    text,        -- WIFI|USB|BLUETOOTH
  keyword_matched text,
  auto_reply_sent boolean,
  delivered_at    timestamptz,
  read_at         timestamptz,
  thread_id       text,
  created_at      timestamptz
)
```

---

## Dashboard Integration Checklist

- [ ] Add **Secret Key** field in Settings → Android Gateway section
- [ ] Add **Inbox tab** in Messaging page (poll `GET /inbox` every 30s)
- [ ] Add **Auto-Reply Rule builder** in Settings (save to localStorage, push to app)
- [ ] Show **delivery status** badge (poll `GET /status` or check `message_log.status`)
- [ ] Run `supabase_schema.sql` to add new columns

---

## Permissions Required

| Permission | Purpose |
|---|---|
| SEND_SMS | Send outbound messages |
| RECEIVE_SMS | Capture customer replies |
| READ_PHONE_STATE | Detect SIM number |
| BLUETOOTH_CONNECT | BT RFCOMM mode |
| RECEIVE_BOOT_COMPLETED | Auto-start on boot |
| FOREGROUND_SERVICE | Persistent service |
| REQUEST_IGNORE_BATTERY_OPTIMIZATIONS | Prevent Android from killing service |

---

## Troubleshooting

**Service keeps stopping:**
→ Grant "Ignore Battery Optimization" (prompted on first launch)
→ In phone settings: Apps → FuelOne Gateway → Battery → Unrestricted

**SMS not sending:**
→ Check SEND_SMS permission is granted
→ Verify the phone has SIM signal (check /status signal_strength)

**Delivery reports not updating:**
→ Some carriers don't support delivery reports — this is carrier-level, not app-level

**Bluetooth not connecting:**
→ Ensure both devices are paired in system Bluetooth settings
→ Check BT MAC address with `python bt_bridge.py --scan`
→ Only one RFCOMM connection at a time is supported
