package com.fuelone.smsgateway.data

import android.util.Log
import com.fuelone.smsgateway.model.MessageLog
import com.fuelone.smsgateway.model.WebhookPayload
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * SupabaseRepository
 *
 * Handles all direct REST API calls to Supabase PostgREST:
 *   - Insert message_log rows (outbound/inbound)
 *   - Update status (delivered)
 *   - POST to webhook URL
 *
 * Uses OkHttp directly (no Supabase SDK needed — plain REST is simpler
 * and avoids dependency conflicts with NanoHTTPD).
 */
class SupabaseRepository(private val prefs: AppPreferences) {

    companion object {
        private const val TAG = "SupabaseRepository"
        private val JSON_TYPE = "application/json; charset=utf-8".toMediaType()
    }

    private val gson = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    // ─── Insert Message Log Row ───────────────────────────────────────────────
    suspend fun logMessage(log: MessageLog): Boolean = withContext(Dispatchers.IO) {
        val url  = prefs.supabaseUrl
        val key  = prefs.supabaseAnonKey
        if (url.isBlank() || key.isBlank()) {
            Log.w(TAG, "Supabase not configured — skipping log")
            return@withContext false
        }

        val endpoint = "${url.trimEnd('/')}/rest/v1/message_log"
        val body = gson.toJson(log).toRequestBody(JSON_TYPE)

        val request = Request.Builder()
            .url(endpoint)
            .addHeader("apikey", key)
            .addHeader("Authorization", "Bearer $key")
            .addHeader("Content-Type", "application/json")
            .addHeader("Prefer", "return=minimal")
            .post(body)
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            val ok = response.isSuccessful
            if (!ok) Log.e(TAG, "Supabase insert failed: ${response.code} — ${response.body?.string()}")
            response.close()
            ok
        } catch (e: Exception) {
            Log.e(TAG, "Supabase insert error: ${e.message}")
            false
        }
    }

    // ─── Update Delivery Status ───────────────────────────────────────────────
    suspend fun updateDeliveryStatus(messageId: String, status: String, deliveredAt: String): Boolean =
        withContext(Dispatchers.IO) {
            val url = prefs.supabaseUrl
            val key = prefs.supabaseAnonKey
            if (url.isBlank() || key.isBlank()) return@withContext false

            val endpoint = "${url.trimEnd('/')}/rest/v1/message_log?message_id=eq.$messageId"
            val payload  = """{"status":"$status","delivered_at":"$deliveredAt"}"""
            val body     = payload.toRequestBody(JSON_TYPE)

            val request = Request.Builder()
                .url(endpoint)
                .addHeader("apikey", key)
                .addHeader("Authorization", "Bearer $key")
                .addHeader("Content-Type", "application/json")
                .patch(body)
                .build()

            return@withContext try {
                val response = client.newCall(request).execute()
                response.close()
                response.isSuccessful
            } catch (e: Exception) {
                Log.e(TAG, "Status update error: ${e.message}")
                false
            }
        }

    // ─── POST to Webhook ──────────────────────────────────────────────────────
    suspend fun postWebhook(payload: WebhookPayload): Boolean = withContext(Dispatchers.IO) {
        val webhookUrl = prefs.webhookUrl
        if (webhookUrl.isBlank()) return@withContext true // no-op if not configured

        val body = gson.toJson(payload).toRequestBody(JSON_TYPE)
        val request = Request.Builder()
            .url(webhookUrl)
            .addHeader("Content-Type", "application/json")
            .addHeader("X-Gateway-Key", prefs.secretKey)
            .post(body)
            .build()

        return@withContext try {
            val response = client.newCall(request).execute()
            val ok = response.isSuccessful
            if (!ok) Log.w(TAG, "Webhook POST failed: ${response.code}")
            response.close()
            ok
        } catch (e: Exception) {
            Log.e(TAG, "Webhook POST error: ${e.message}")
            false
        }
    }
}
