package com.fuelone.smsgateway.server

import android.util.Log
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.data.GatewayDatabase
import com.fuelone.smsgateway.model.*
import com.google.gson.Gson
import fi.iki.elonen.NanoHTTPD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * ApiServer — NanoHTTPD-based HTTP API server
 *
 * Endpoints:
 *   POST /send    — Send SMS (Bearer token auth)
 *   GET  /status  — Gateway status
 *   GET  /inbox   — Last 50 received messages
 *   POST /reply   — Send a reply to a phone number
 *
 * All responses are JSON. Bearer token verified on write endpoints.
 */
class ApiServer(
    private val port: Int,
    private val prefs: AppPreferences,
    private val db: GatewayDatabase,
    private val smsSender: SmsSenderCallback,
    private val statusProvider: StatusProvider
) : NanoHTTPD(port) {

    companion object {
        private const val TAG = "ApiServer"
        private val JSON_MIME = "application/json"
        private val ISO_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    }

    interface SmsSenderCallback {
        fun sendSms(request: SendRequest): Boolean
    }

    interface StatusProvider {
        fun getStatus(): GatewayStatus
    }

    private val gson = Gson()
    private val scope = CoroutineScope(Dispatchers.IO)

    // ─── Request Router ───────────────────────────────────────────────────────
    override fun serve(session: IHTTPSession): Response {
        val uri    = session.uri
        val method = session.method

        Log.d(TAG, "${method.name} $uri")

        return try {
            when {
                method == Method.OPTIONS                  -> handleOptions()
                method == Method.POST && uri == "/send"   -> handleSend(session)
                method == Method.GET  && uri == "/status" -> handleStatus()
                method == Method.GET  && uri == "/inbox"  -> handleInbox()
                method == Method.POST && uri == "/reply"  -> handleReply(session)
                method == Method.GET  && uri == "/"       -> handleRoot()
                else -> jsonError(404, "Not found")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Server error: ${e.message}", e)
            jsonError(500, "Internal server error: ${e.message}")
        }
    }

    // ─── POST /send ───────────────────────────────────────────────────────────
    private fun handleSend(session: IHTTPSession): Response {
        if (!isAuthorized(session)) return unauthorizedResponse()

        val body = readBody(session)
        val req  = try {
            gson.fromJson(body, SendRequest::class.java)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON: ${e.message}")
        }

        if (req.phone.isNullOrBlank() || req.message.isNullOrBlank()) {
            return jsonError(400, "phone and message are required")
        }

        val success   = smsSender.sendSms(req)
        val timestamp = System.currentTimeMillis()
        val response  = SendResponse(
            success   = success,
            messageId = req.messageId,
            timestamp = timestamp,
            error     = if (success) null else "SMS send failed"
        )

        val status = if (success) 200 else 500
        return newFixedLengthResponse(
            Response.Status.lookup(status),
            JSON_MIME,
            gson.toJson(response)
        )
    }

    // ─── GET /status ──────────────────────────────────────────────────────────
    private fun handleStatus(): Response {
        val status = statusProvider.getStatus()
        return jsonOk(gson.toJson(status))
    }

    // ─── GET /inbox ───────────────────────────────────────────────────────────
    private fun handleInbox(): Response {
        val messages = runBlocking { db.messageDao().getInbox() }
        val inbox = messages.map { msg ->
            InboxSms(
                from          = msg.phone,
                message       = msg.message,
                timestamp     = msg.createdAt,
                read          = msg.readAt != null,
                keywordMatched = msg.keywordMatched,
                autoReplySent  = msg.autoReplySent
            )
        }
        return jsonOk(gson.toJson(inbox))
    }

    // ─── POST /reply ──────────────────────────────────────────────────────────
    private fun handleReply(session: IHTTPSession): Response {
        if (!isAuthorized(session)) return unauthorizedResponse()

        val body = readBody(session)
        val req  = try {
            gson.fromJson(body, SendRequest::class.java)
        } catch (e: Exception) {
            return jsonError(400, "Invalid JSON")
        }

        val success = smsSender.sendSms(req)
        return jsonOk(gson.toJson(SendResponse(
            success   = success,
            messageId = req.messageId,
            timestamp = System.currentTimeMillis(),
            error     = if (success) null else "Reply send failed"
        )))
    }

    // ─── OPTIONS (CORS Preflight) ─────────────────────────────────────────────
    private fun handleOptions(): Response {
        val response = newFixedLengthResponse(Response.Status.OK, JSON_MIME, "{}")
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        response.addHeader("Access-Control-Max-Age", "3600")
        return response
    }

    // ─── GET / ────────────────────────────────────────────────────────────────
    private fun handleRoot(): Response {
        val info = """{
            "service": "FuelOne SMS Gateway",
            "version": "1.0.0",
            "status": "running",
            "endpoints": ["/send", "/status", "/inbox", "/reply"]
        }""".trimIndent()
        return jsonOk(info)
    }

    // ─── Auth ─────────────────────────────────────────────────────────────────
    private fun isAuthorized(session: IHTTPSession): Boolean {
        val secretKey = prefs.secretKey
        if (secretKey.isBlank()) return true // No key set → open (dev mode)
        val authHeader = session.headers["authorization"] ?: return false
        return authHeader == "Bearer $secretKey"
    }

    private fun unauthorizedResponse(): Response {
        val response = newFixedLengthResponse(
            Response.Status.UNAUTHORIZED,
            JSON_MIME,
            """{"error":"Unauthorized — invalid or missing Bearer token"}"""
        )
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────
    private fun readBody(session: IHTTPSession): String {
        val contentLength = session.headers["content-length"]?.toIntOrNull() ?: 0
        val buf = ByteArray(contentLength)
        session.inputStream.read(buf, 0, contentLength)
        return String(buf, Charsets.UTF_8)
    }

    private fun jsonOk(json: String): Response {
        val response = newFixedLengthResponse(Response.Status.OK, JSON_MIME, json)
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }

    private fun jsonError(code: Int, message: String): Response {
        val response = newFixedLengthResponse(
            Response.Status.lookup(code),
            JSON_MIME,
            """{"error":"$message"}"""
        )
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type")
        return response
    }
}
