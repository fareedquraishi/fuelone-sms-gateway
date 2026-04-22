package com.fuelone.smsgateway

import android.app.Application
import android.util.Log

class GatewayApplication : Application() {

    companion object {
        private const val TAG = "GatewayApplication"
        lateinit var instance: GatewayApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Log.i(TAG, "FuelOne SMS Gateway Application started")
    }
}
