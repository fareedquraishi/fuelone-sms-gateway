package com.fuelone.smsgateway.model

import com.google.gson.annotations.SerializedName

// ─── Connection Mode ───────────────────────────────────────────────────────────
enum class ConnectionMode { WIFI, USB, BLUETOOTH }

// ─── Outbound Send Request (from dashboard → app) ─────────────────────────────
data class SendRequest(
    @SerializedName("phone")         val phone: String,
    @SerializedName("message")       val message: String,
    @SerializedName("message_id")    val messageId: String,
    @SerializedName("customer_name") val customerName: String = ""
)

// ─── Send Response (app → dashboard) ─────────────────────────────────────────
data class SendResponse(
    @SerializedName("success")    val success: Boolean,
    @SerializedName("message_id") val messageId: String,
    @SerializedName("timestamp")  val timestamp: Long = System.currentTimeMillis(),
    @SerializedName("error")      val error: String? = null
)

// ─── Gateway Status ───────────────────────────────────────────────────────────
data class GatewayStatus(
    @SerializedName("mode")               val mode: String,
    @SerializedName("ip")                 val ip: String,
    @SerializedName("port")               val port: Int,
    @SerializedName("messages_sent_today") val messagesSentToday: Int,
    @SerializedName("queue_size")         val queueSize: Int,
    @SerializedName("battery")           val battery: Int,
    @SerializedName("signal_strength")   val signalStrength: Int
)

// ─── Message Log Entry (Supabase row) ────────────────────────────────────────
data class MessageLog(
    @SerializedName("id")               val id: String? = null,
    @SerializedName("direction")        val direction: String,         // "inbound" | "outbound"
    @SerializedName("phone")            val phone: String,
    @SerializedName("message")          val message: String,
    @SerializedName("status")           val status: String,            // sent|failed|delivered|received
    @SerializedName("customer_name")    val customerName: String = "",
    @SerializedName("message_id")       val messageId: String = "",
    @SerializedName("gateway_mode")     val gatewayMode: String = "",
    @SerializedName("keyword_matched")  val keywordMatched: String? = null,
    @SerializedName("auto_reply_sent")  val autoReplySent: Boolean = false,
    @SerializedName("created_at")       val createdAt: String? = null,
    @SerializedName("delivered_at")     val deliveredAt: String? = null,
    @SerializedName("thread_id")        val threadId: String? = null
)

// ─── Auto-Reply Rule ──────────────────────────────────────────────────────────
data class AutoReplyRule(
    val keyword: String,
    val reply: String,
    val matchType: MatchType = MatchType.EXACT
) {
    enum class MatchType { EXACT, CONTAINS }
}

// ─── Queued SMS ───────────────────────────────────────────────────────────────
data class QueuedSms(
    val sendRequest: SendRequest,
    var attempts: Int = 0,
    val createdAt: Long = System.currentTimeMillis(),
    var nextRetryAt: Long = 0L
)

// ─── Webhook Payload (app → Supabase Edge Function) ──────────────────────────
data class WebhookPayload(
    @SerializedName("from")            val from: String,
    @SerializedName("message")        val message: String,
    @SerializedName("timestamp")      val timestamp: Long,
    @SerializedName("keyword_matched") val keywordMatched: String?,
    @SerializedName("auto_reply_sent") val autoReplySent: Boolean,
    @SerializedName("gateway_id")     val gatewayId: String = "fuelone_gateway"
)

// ─── Inbox SMS (returned by GET /inbox) ──────────────────────────────────────
data class InboxSms(
    @SerializedName("from")      val from: String,
    @SerializedName("message")   val message: String,
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("read")      val read: Boolean,
    @SerializedName("keyword_matched") val keywordMatched: String?,
    @SerializedName("auto_reply_sent") val autoReplySent: Boolean
)
