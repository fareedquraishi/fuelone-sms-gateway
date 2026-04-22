package com.fuelone.smsgateway.engine;

/**
 * AutoReplyEngine
 *
 * Checks an incoming SMS body against configured keyword rules.
 * Returns the matching rule (if any) so the caller can send the reply
 * and log keyword_matched + auto_reply_sent to Supabase.
 *
 * Match priority:
 *  1. EXACT full-message match (case-insensitive, trimmed)
 *  2. CONTAINS match (keyword appears anywhere in message)
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\bJ\u0010\u0010\f\u001a\u0004\u0018\u00010\n2\u0006\u0010\r\u001a\u00020\bR\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u000f"}, d2 = {"Lcom/fuelone/smsgateway/engine/AutoReplyEngine;", "", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "prefs", "Lcom/fuelone/smsgateway/data/AppPreferences;", "buildReplyText", "", "rule", "Lcom/fuelone/smsgateway/model/AutoReplyRule;", "senderPhone", "evaluate", "body", "Companion", "app_release"})
public final class AutoReplyEngine {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "AutoReplyEngine";
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.data.AppPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.fuelone.smsgateway.engine.AutoReplyEngine.Companion Companion = null;
    
    public AutoReplyEngine(@org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        super();
    }
    
    /**
     * Evaluate incoming message body.
     * @return Matched [AutoReplyRule] or null if no rule matched.
     */
    @org.jetbrains.annotations.Nullable()
    public final com.fuelone.smsgateway.model.AutoReplyRule evaluate(@org.jetbrains.annotations.NotNull()
    java.lang.String body) {
        return null;
    }
    
    /**
     * Build the auto-reply text, substituting {phone} placeholder if present.
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String buildReplyText(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.AutoReplyRule rule, @org.jetbrains.annotations.NotNull()
    java.lang.String senderPhone) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/fuelone/smsgateway/engine/AutoReplyEngine$Companion;", "", "()V", "TAG", "", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}