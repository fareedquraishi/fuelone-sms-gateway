package com.fuelone.smsgateway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.service.SmsGatewayService

/**
 * BootReceiver — starts SmsGatewayService automatically after device boot
 * if the user has auto-start enabled in settings.
 */
class BootReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action
        if (action != Intent.ACTION_BOOT_COMPLETED &&
            action != "android.intent.action.QUICKBOOT_POWERON") return

        val prefs = AppPreferences(context)
        if (!prefs.autoStart) {
            Log.d(TAG, "Auto-start disabled — skipping")
            return
        }

        Log.i(TAG, "Boot complete — starting SMS Gateway service")
        prefs.gatewayEnabled = true

        val serviceIntent = Intent(context, SmsGatewayService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}
