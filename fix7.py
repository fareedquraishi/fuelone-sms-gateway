code = '''package com.fuelone.smsgateway.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("MmsReceiver", "MMS received")
    }
}'''

with open('app/src/main/java/com/fuelone/smsgateway/receiver/MmsReceiver.kt', 'w') as f:
    f.write(code)
print('Done!')
