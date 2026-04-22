package com.fuelone.smsgateway.service

import android.app.*
import android.content.*
import android.content.pm.ServiceInfo
import android.os.*
import android.telephony.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.fuelone.smsgateway.GatewayApplication
import com.fuelone.smsgateway.R
import com.fuelone.smsgateway.bluetooth.BluetoothService
import com.fuelone.smsgateway.data.*
import com.fuelone.smsgateway.engine.AutoReplyEngine
import com.fuelone.smsgateway.model.*
import com.fuelone.smsgateway.server.ApiServer
import com.fuelone.smsgateway.ui.MainActivity
import com.fuelone.smsgateway.util.NetworkUtil
import com.google.gson.Gson
import kotlinx.coroutines.*
import java.net.NetworkInterface
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * SmsGatewayService
 *
 * Central foreground service orchestrating:
 *  - NanoHTTPD HTTP server (WiFi/USB mode)
 *  - Bluetooth RFCOMM server (BT mode)
 *  - SMS send queue with retry (3 attempts × 30s intervals)
 *  - Delivery status tracking
 *  - Supabase logging
 *  - Persistent notification with live stats
 */
class SmsGatewayService : Service() {

    companion object {
        private const val TAG = "SmsGatewayService"
        const val NOTIFICATION_ID  = 1001
        const val CHANNEL_ID       = "sms_gateway_channel"
        const val CHANNEL_NAME     = "SMS Gateway"
        const val ACTION_STOP      = "com.fuelone.smsgateway.ACTION_STOP"
        const val ACTION_INBOUND   = "com.fuelone.smsgateway.INBOUND_SMS"
        const val EXTRA_FROM       = "from"
        const val EXTRA_MESSAGE    = "message"

        private const val MAX_RETRIES        = 3
        private const val RETRY_INTERVAL_MS  = 30_000L
        private const val QUEUE_PROCESS_INTERVAL = 5_000L
        private const val NOTIFICATION_UPDATE_INTERVAL = 10_000L
    }

    // ─── Core Components ─────────────────────────────────────────────────────
    private lateinit var prefs: AppPreferences
    private lateinit var db: GatewayDatabase
    private lateinit var supabase: SupabaseRepository
    private lateinit var autoReplyEngine: AutoReplyEngine
    private val gson = Gson()

    private var apiServer: ApiServer? = null
    private var bluetoothService: BluetoothService? = null

    // SMS queue: holds pending outbound messages
    private val smsQueue = ConcurrentLinkedQueue<QueuedSms>()
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private var messagesSentToday = 0
    private var unreadInboxCount  = 0

    // ─── Lifecycle ────────────────────────────────────────────────────────────
    override fun onCreate() {
        super.onCreate()
        prefs             = AppPreferences(this)
        db                = GatewayDatabase.getInstance(this)
        supabase          = SupabaseRepository(prefs)
        autoReplyEngine   = AutoReplyEngine(this)

        createNotificationChannel()
        startForeground(NOTIFICATION_ID, buildNotification(), ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE)

        registerInboundReceiver()
        startSelectedMode()
        startQueueProcessor()
        startNotificationUpdater()
        startDailyCounter()

        Log.i(TAG, "SmsGatewayService started — mode: ${prefs.connectionMode}")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY // Auto-restart if killed
    }

    override fun onDestroy() {
        super.onDestroy()
        apiServer?.stop()
        bluetoothService?.stop()
        scope.cancel()
        unregisterInboundReceiver()
        prefs.gatewayEnabled = false
        Log.i(TAG, "SmsGatewayService stopped")
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // ─── Mode Startup ─────────────────────────────────────────────────────────
    private fun startSelectedMode() {
        when (prefs.connectionMode) {
            ConnectionMode.WIFI, ConnectionMode.USB -> startHttpServer()
            ConnectionMode.BLUETOOTH                -> startBluetoothServer()
        }
    }

    private fun startHttpServer() {
        try {
            apiServer = ApiServer(
                port           = prefs.port,
                prefs          = prefs,
                db             = db,
                smsSender      = object : ApiServer.SmsSenderCallback {
                    override fun sendSms(request: SendRequest): Boolean {
                        enqueue(request)
                        return true // queued successfully
                    }
                },
                statusProvider = object : ApiServer.StatusProvider {
                    override fun getStatus(): GatewayStatus = buildStatus()
                }
            )
            apiServer?.start()
            Log.i(TAG, "HTTP server started on port ${prefs.port}")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to start HTTP server: ${e.message}", e)
        }
    }

    private fun startBluetoothServer() {
        bluetoothService = BluetoothService(
            context    = this,
            smsSender  = { req -> enqueue(req); true },
            statusJson = { gson.toJson(buildStatus()) },
            inboxJson  = { runBlocking { gson.toJson(db.messageDao().getInbox()) } }
        )
        bluetoothService?.start()
    }

    // ─── SMS Send Queue ───────────────────────────────────────────────────────
    fun enqueue(request: SendRequest) {
        smsQueue.offer(QueuedSms(sendRequest = request))
        Log.d(TAG, "Enqueued SMS to ${request.phone} [id=${request.messageId}]")
    }

    private fun startQueueProcessor() {
        scope.launch {
            while (isActive) {
                processQueue()
                delay(QUEUE_PROCESS_INTERVAL)
            }
        }
    }

    private suspend fun processQueue() {
        val now = System.currentTimeMillis()
        val iter = smsQueue.iterator()

        while (iter.hasNext()) {
            val item = iter.next()

            // Skip if not yet due for retry
            if (item.nextRetryAt > now) continue

            val req     = item.sendRequest
            val success = sendSmsNow(req)

            if (success) {
                iter.remove()
                messagesSentToday++
                logOutbound(req, "sent")
            } else {
                item.attempts++
                if (item.attempts >= MAX_RETRIES) {
                    Log.w(TAG, "SMS to ${req.phone} failed after $MAX_RETRIES attempts")
                    iter.remove()
                    logOutbound(req, "failed")
                } else {
                    item.nextRetryAt = now + RETRY_INTERVAL_MS
                    Log.d(TAG, "SMS retry ${item.attempts}/$MAX_RETRIES for ${req.phone} in 30s")
                }
            }
        }
    }

    // ─── Actual SMS Send via SmsManager ──────────────────────────────────────
    @Suppress("DEPRECATION")
    private fun sendSmsNow(req: SendRequest): Boolean {
        return try {
            val smsManager = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                getSystemService(SmsManager::class.java)
            } else {
                SmsManager.getDefault()
            }

            val sentIntent = PendingIntent.getBroadcast(
                this,
                req.messageId.hashCode(),
                Intent("com.fuelone.smsgateway.SMS_SENT").apply {
                    putExtra("message_id", req.messageId)
                    putExtra("phone", req.phone)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val deliveredIntent = PendingIntent.getBroadcast(
                this,
                req.messageId.hashCode() + 1,
                Intent("com.fuelone.smsgateway.SMS_DELIVERED").apply {
                    putExtra("message_id", req.messageId)
                    putExtra("phone", req.phone)
                },
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val parts = smsManager.divideMessage(req.message)
            if (parts.size > 1) {
                val sentIntents      = ArrayList<PendingIntent>(parts.map { sentIntent })
                val deliveredIntents = ArrayList<PendingIntent>(parts.map { deliveredIntent })
                smsManager.sendMultipartTextMessage(req.phone, null, parts, sentIntents, deliveredIntents)
            } else {
                smsManager.sendTextMessage(req.phone, null, req.message, sentIntent, deliveredIntent)
            }
            Log.d(TAG, "SMS sent to ${req.phone}")
            true
        } catch (e: Exception) {
            Log.e(TAG, "SMS send failed to ${req.phone}: ${e.message}")
            false
        }
    }

    // ─── Inbound SMS Handling (called by SmsReceiver) ─────────────────────────
    private var inboundReceiver: BroadcastReceiver? = null

    private fun registerInboundReceiver() {
        inboundReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val from    = intent?.getStringExtra(EXTRA_FROM) ?: return
                val message = intent.getStringExtra(EXTRA_MESSAGE) ?: return
                scope.launch { handleInboundSms(from, message) }
            }
        }
        val filter = IntentFilter(ACTION_INBOUND)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(inboundReceiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(inboundReceiver, filter)
        }
    }

    private fun unregisterInboundReceiver() {
        inboundReceiver?.let { unregisterReceiver(it) }
    }

    private suspend fun handleInboundSms(from: String, message: String) {
        Log.i(TAG, "Inbound SMS from $from: $message")

        // 1. Check auto-reply rules
        val matchedRule = autoReplyEngine.evaluate(message)
        val autoReplySent = matchedRule != null

        // 2. Store inbound message locally
        val entity = com.fuelone.smsgateway.data.MessageEntity(
            direction      = "inbound",
            phone          = from,
            message        = message,
            status         = "received",
            gatewayMode    = prefs.connectionMode.name,
            keywordMatched = matchedRule?.keyword,
            autoReplySent  = autoReplySent
        )
        val id = db.messageDao().insert(entity)
        unreadInboxCount++

        // 3. Log inbound to Supabase
        supabase.logMessage(MessageLog(
            direction      = "inbound",
            phone          = from,
            message        = message,
            status         = "received",
            gatewayMode    = prefs.connectionMode.name,
            keywordMatched = matchedRule?.keyword,
            autoReplySent  = autoReplySent
        ))

        // 4. Send auto-reply if rule matched
        if (matchedRule != null) {
            val replyText = autoReplyEngine.buildReplyText(matchedRule, from)
            val replyReq  = SendRequest(
                phone        = from,
                message      = replyText,
                messageId    = "auto_${System.currentTimeMillis()}",
                customerName = ""
            )
            enqueue(replyReq)
            Log.i(TAG, "Auto-reply queued for $from → keyword='${matchedRule.keyword}'")
        }

        // 5. POST webhook to dashboard
        supabase.postWebhook(WebhookPayload(
            from           = from,
            message        = message,
            timestamp      = System.currentTimeMillis(),
            keywordMatched = matchedRule?.keyword,
            autoReplySent  = autoReplySent
        ))

        // 6. Notify PC via Bluetooth if in BT mode
        bluetoothService?.notifyInbound(from, message, matchedRule?.keyword)

        // 7. Update notification
        updateNotification()
    }

    // ─── Supabase Logging ─────────────────────────────────────────────────────
    private fun logOutbound(req: SendRequest, status: String) {
        scope.launch {
            val entity = MessageEntity(
                direction    = "outbound",
                phone        = req.phone,
                message      = req.message,
                status       = status,
                customerName = req.customerName ?: "",
                messageId    = req.messageId ?: "",
                gatewayMode  = prefs.connectionMode.name
            )
            db.messageDao().insert(entity)

            supabase.logMessage(MessageLog(
                direction    = "outbound",
                phone        = req.phone,
                message      = req.message,
                status       = status,
                customerName = req.customerName ?: "",
                messageId    = req.messageId ?: "",
                gatewayMode  = prefs.connectionMode.name
            ))
        }
    }

    // ─── Status Builder ───────────────────────────────────────────────────────
    private fun buildStatus(): GatewayStatus {
        val ip   = if (prefs.connectionMode == ConnectionMode.WIFI) getLocalIpAddress() else "127.0.0.1"
        return GatewayStatus(
            mode              = prefs.connectionMode.name,
            ip                = ip,
            port              = prefs.port,
            messagesSentToday = messagesSentToday,
            queueSize         = smsQueue.size,
            battery           = getBatteryLevel(),
            signalStrength    = getSignalStrength()
        )
    }

    private fun getLocalIpAddress(): String {
        return try {
            NetworkInterface.getNetworkInterfaces().toList()
                .flatMap { it.inetAddresses.toList() }
                .firstOrNull { !it.isLoopbackAddress && it.hostAddress?.contains('.') == true }
                ?.hostAddress ?: "0.0.0.0"
        } catch (e: Exception) { "0.0.0.0" }
    }

    private fun getBatteryLevel(): Int {
        val bm = getSystemService(BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }

    private fun getSignalStrength(): Int {
        val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        return try {
            // Returns 0-4 asu level on Android 10+
            tm.signalStrength?.level ?: 0
        } catch (e: Exception) { 0 }
    }

    // ─── Daily Counter Reset ──────────────────────────────────────────────────
    private fun startDailyCounter() {
        scope.launch {
            while (isActive) {
                // Reload count from DB at midnight
                val cal = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                messagesSentToday = db.messageDao().countSentToday(cal.timeInMillis)
                delay(60_000) // check every minute
            }
        }
    }

    // ─── Notification ─────────────────────────────────────────────────────────
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "FuelOne SMS Gateway status"
            setShowBadge(false)
        }
        getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val mainIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val stopIntent = PendingIntent.getService(
            this, 1,
            Intent(this, SmsGatewayService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val mode = prefs.connectionMode.name
        val ip   = if (mode == "WIFI") getLocalIpAddress() else "USB/BT"

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("🟢 Gateway Active — $mode")
            .setContentText("Sent today: $messagesSentToday | Unread: $unreadInboxCount | $ip:${prefs.port}")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(mainIntent)
            .addAction(android.R.drawable.ic_delete, "Stop", stopIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private fun updateNotification() {
        val nm = getSystemService(NotificationManager::class.java)
        nm.notify(NOTIFICATION_ID, buildNotification())
    }

    private fun startNotificationUpdater() {
        scope.launch {
            while (isActive) {
                delay(NOTIFICATION_UPDATE_INTERVAL)
                unreadInboxCount = db.messageDao().getUnreadInbox().size
                updateNotification()
            }
        }
    }
}
