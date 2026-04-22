package com.fuelone.smsgateway.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.fuelone.smsgateway.model.AutoReplyRule
import com.fuelone.smsgateway.model.ConnectionMode

class AppPreferences(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val PREFS_NAME = "gateway_prefs"

        // Keys
        const val KEY_CONNECTION_MODE   = "connection_mode"
        const val KEY_SECRET_KEY        = "secret_key"
        const val KEY_PORT              = "port"
        const val KEY_SUPABASE_URL      = "supabase_url"
        const val KEY_SUPABASE_ANON_KEY = "supabase_anon_key"
        const val KEY_WEBHOOK_URL       = "webhook_url"
        const val KEY_AUTO_REPLY_RULES  = "auto_reply_rules"
        const val KEY_GATEWAY_ENABLED   = "gateway_enabled"
        const val KEY_AUTO_START        = "auto_start"
        const val KEY_SIM_NUMBER        = "sim_number"
        const val KEY_BT_DEVICE_NAME    = "bt_device_name"

        // Defaults
        const val DEFAULT_PORT          = 8080
        const val DEFAULT_SUPABASE_URL  = "https://iageuvrhveeptvyjiavy.supabase.co"
    }

    var connectionMode: ConnectionMode
        get() = ConnectionMode.valueOf(prefs.getString(KEY_CONNECTION_MODE, ConnectionMode.WIFI.name)!!)
        set(v) = prefs.edit().putString(KEY_CONNECTION_MODE, v.name).apply()

    var secretKey: String
        get() = prefs.getString(KEY_SECRET_KEY, "") ?: ""
        set(v) = prefs.edit().putString(KEY_SECRET_KEY, v).apply()

    var port: Int
        get() = prefs.getInt(KEY_PORT, DEFAULT_PORT)
        set(v) = prefs.edit().putInt(KEY_PORT, v).apply()

    var supabaseUrl: String
        get() = prefs.getString(KEY_SUPABASE_URL, DEFAULT_SUPABASE_URL) ?: DEFAULT_SUPABASE_URL
        set(v) = prefs.edit().putString(KEY_SUPABASE_URL, v).apply()

    var supabaseAnonKey: String
        get() = prefs.getString(KEY_SUPABASE_ANON_KEY, "") ?: ""
        set(v) = prefs.edit().putString(KEY_SUPABASE_ANON_KEY, v).apply()

    var webhookUrl: String
        get() = prefs.getString(KEY_WEBHOOK_URL, "") ?: ""
        set(v) = prefs.edit().putString(KEY_WEBHOOK_URL, v).apply()

    var gatewayEnabled: Boolean
        get() = prefs.getBoolean(KEY_GATEWAY_ENABLED, false)
        set(v) = prefs.edit().putBoolean(KEY_GATEWAY_ENABLED, v).apply()

    var autoStart: Boolean
        get() = prefs.getBoolean(KEY_AUTO_START, true)
        set(v) = prefs.edit().putBoolean(KEY_AUTO_START, v).apply()

    var simNumber: String
        get() = prefs.getString(KEY_SIM_NUMBER, "Not detected") ?: "Not detected"
        set(v) = prefs.edit().putString(KEY_SIM_NUMBER, v).apply()

    var btDeviceName: String
        get() = prefs.getString(KEY_BT_DEVICE_NAME, "FuelOne-Gateway") ?: "FuelOne-Gateway"
        set(v) = prefs.edit().putString(KEY_BT_DEVICE_NAME, v).apply()

    // ─── Auto-Reply Rules ─────────────────────────────────────────────────────
    var autoReplyRules: List<AutoReplyRule>
        get() {
            val json = prefs.getString(KEY_AUTO_REPLY_RULES, null) ?: return defaultRules()
            return try {
                val type = object : TypeToken<List<AutoReplyRule>>() {}.type
                gson.fromJson(json, type)
            } catch (e: Exception) {
                defaultRules()
            }
        }
        set(v) = prefs.edit().putString(KEY_AUTO_REPLY_RULES, gson.toJson(v)).apply()

    private fun defaultRules(): List<AutoReplyRule> = listOf(
        AutoReplyRule("1", "Great! Your Engine Oil service is booked. Our BA will contact you shortly. — Euro AEC"),
        AutoReplyRule("2", "Great! Your Gear Oil service is booked. Our BA will contact you shortly. — Euro AEC"),
        AutoReplyRule("3", "Great! Your Full Service is booked. Our BA will contact you shortly. — Euro AEC"),
        AutoReplyRule("YES", "Thank you! We have noted your interest. Our BA will be in touch. — Euro AEC"),
        AutoReplyRule("NO", "Thank you for your reply. We hope to serve you next time! — Euro AEC")
    )
}
