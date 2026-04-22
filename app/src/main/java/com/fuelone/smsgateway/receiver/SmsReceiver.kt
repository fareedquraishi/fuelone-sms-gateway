package com.fuelone.smsgateway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import com.fuelone.smsgateway.service.SmsGatewayService

/**
 * SmsReceiver
 *
 * Declared in manifest with priority 999 so it intercepts SMS before other apps.
 * Extracts all PDU (Protocol Data Unit) messages, assembles them, and
 * forwards to SmsGatewayService via a local broadcast.
 */
class SmsReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "SmsReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        try {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            if (messages.isNullOrEmpty()) return

            // Group multipart messages by originating address
            val grouped = mutableMapOf<String, StringBuilder>()
            messages.forEach { sms ->
                val from = sms.originatingAddress ?: return@forEach
                grouped.getOrPut(from) { StringBuilder() }
                    .append(sms.messageBody)
            }

            grouped.forEach { (from, bodyBuilder) ->
                val body = bodyBuilder.toString().trim()
                Log.i(TAG, "Received SMS from $from: $body")

                // Forward to service via local broadcast
                val broadcastIntent = Intent(SmsGatewayService.ACTION_INBOUND).apply {
                    putExtra(SmsGatewayService.EXTRA_FROM, from)
                    putExtra(SmsGatewayService.EXTRA_MESSAGE, body)
                    setPackage(context.packageName)
                }
                context.sendBroadcast(broadcastIntent)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error processing SMS: ${e.message}", e)
        }
    }
}
