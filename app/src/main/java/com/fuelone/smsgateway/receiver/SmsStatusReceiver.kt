package com.fuelone.smsgateway.receiver

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import android.util.Log
import com.fuelone.smsgateway.data.GatewayDatabase
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.data.SupabaseRepository
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * SmsStatusReceiver
 *
 * Handles SMS_SENT and SMS_DELIVERED broadcast intents from SmsManager.
 * Updates local DB + Supabase with actual delivery status.
 */
class SmsStatusReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG         = "SmsStatusReceiver"
        const val ACTION_SMS_SENT     = "com.fuelone.smsgateway.SMS_SENT"
        const val ACTION_SMS_DELIVERED = "com.fuelone.smsgateway.SMS_DELIVERED"
        private val ISO_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    }

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        val messageId = intent.getStringExtra("message_id") ?: return
        val phone     = intent.getStringExtra("phone") ?: ""

        val db       = GatewayDatabase.getInstance(context)
        val prefs    = AppPreferences(context)
        val supabase = SupabaseRepository(prefs)

        when (intent.action) {
            ACTION_SMS_SENT -> {
                val status = when (resultCode) {
                    Activity.RESULT_OK            -> "sent"
                    SmsManager.RESULT_ERROR_RADIO_OFF -> "failed_radio_off"
                    SmsManager.RESULT_ERROR_NULL_PDU  -> "failed_null_pdu"
                    else                          -> "failed"
                }
                Log.i(TAG, "SMS sent status for $messageId: $status")
                scope.launch {
                    db.messageDao().updateStatus(messageId, status)
                    supabase.updateDeliveryStatus(messageId, status, ISO_FORMAT.format(Date()))
                }
            }

            ACTION_SMS_DELIVERED -> {
                Log.i(TAG, "SMS delivered to $phone [id=$messageId]")
                val deliveredAt = ISO_FORMAT.format(Date())
                scope.launch {
                    db.messageDao().updateStatus(messageId, "delivered", System.currentTimeMillis())
                    supabase.updateDeliveryStatus(messageId, "delivered", deliveredAt)
                }
            }
        }
    }
}
