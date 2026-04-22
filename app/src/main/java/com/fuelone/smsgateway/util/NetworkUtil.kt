package com.fuelone.smsgateway.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtil {
    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps    = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun getNetworkType(context: Context): String {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return "none"
        val caps    = cm.getNetworkCapabilities(network) ?: return "none"
        return when {
            caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_WIFI)     -> "WiFi"
            caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile"
            caps.hasTransport(android.net.NetworkCapabilities.TRANSPORT_USB)      -> "USB"
            else -> "unknown"
        }
    }
}
