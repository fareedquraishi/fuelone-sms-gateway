package com.fuelone.smsgateway.engine

import android.content.Context
import android.util.Log
import com.fuelone.smsgateway.data.AppPreferences
import com.fuelone.smsgateway.model.AutoReplyRule

/**
 * AutoReplyEngine
 *
 * Checks an incoming SMS body against configured keyword rules.
 * Returns the matching rule (if any) so the caller can send the reply
 * and log keyword_matched + auto_reply_sent to Supabase.
 *
 * Match priority:
 *   1. EXACT full-message match (case-insensitive, trimmed)
 *   2. CONTAINS match (keyword appears anywhere in message)
 */
class AutoReplyEngine(context: Context) {

    companion object {
        private const val TAG = "AutoReplyEngine"
    }

    private val prefs = AppPreferences(context)

    /**
     * Evaluate incoming message body.
     * @return Matched [AutoReplyRule] or null if no rule matched.
     */
    fun evaluate(body: String): AutoReplyRule? {
        val rules = prefs.autoReplyRules
        val trimmed = body.trim()

        // 1. Try exact match first (higher priority)
        rules.forEach { rule ->
            if (trimmed.equals(rule.keyword, ignoreCase = true)) {
                Log.d(TAG, "EXACT match → keyword='${rule.keyword}'")
                return rule
            }
        }

        // 2. Try contains match
        rules.forEach { rule ->
            if (trimmed.contains(rule.keyword, ignoreCase = true)) {
                Log.d(TAG, "CONTAINS match → keyword='${rule.keyword}'")
                return rule
            }
        }

        Log.d(TAG, "No rule matched for: '$trimmed'")
        return null
    }

    /**
     * Build the auto-reply text, substituting {phone} placeholder if present.
     */
    fun buildReplyText(rule: AutoReplyRule, senderPhone: String): String {
        return rule.reply.replace("{phone}", senderPhone)
                        .replace("{their number}", senderPhone)
    }
}
