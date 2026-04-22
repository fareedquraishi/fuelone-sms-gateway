package com.fuelone.smsgateway.data;

/**
 * SupabaseRepository
 *
 * Handles all direct REST API calls to Supabase PostgREST:
 *  - Insert message_log rows (outbound/inbound)
 *  - Update status (delivered)
 *  - POST to webhook URL
 *
 * Uses OkHttp directly (no Supabase SDK needed — plain REST is simpler
 * and avoids dependency conflicts with NanoHTTPD).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0005\u0018\u0000 \u00182\u00020\u0001:\u0001\u0018B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0016\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0086@\u00a2\u0006\u0002\u0010\rJ\u0016\u0010\u000e\u001a\u00020\n2\u0006\u0010\u000f\u001a\u00020\u0010H\u0086@\u00a2\u0006\u0002\u0010\u0011J&\u0010\u0012\u001a\u00020\n2\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0014H\u0086@\u00a2\u0006\u0002\u0010\u0017R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Lcom/fuelone/smsgateway/data/SupabaseRepository;", "", "prefs", "Lcom/fuelone/smsgateway/data/AppPreferences;", "(Lcom/fuelone/smsgateway/data/AppPreferences;)V", "client", "Lokhttp3/OkHttpClient;", "gson", "Lcom/google/gson/Gson;", "logMessage", "", "log", "Lcom/fuelone/smsgateway/model/MessageLog;", "(Lcom/fuelone/smsgateway/model/MessageLog;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "postWebhook", "payload", "Lcom/fuelone/smsgateway/model/WebhookPayload;", "(Lcom/fuelone/smsgateway/model/WebhookPayload;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "updateDeliveryStatus", "messageId", "", "status", "deliveredAt", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "Companion", "app_debug"})
public final class SupabaseRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.data.AppPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "SupabaseRepository";
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.MediaType JSON_TYPE = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final okhttp3.OkHttpClient client = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.fuelone.smsgateway.data.SupabaseRepository.Companion Companion = null;
    
    public SupabaseRepository(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.data.AppPreferences prefs) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object logMessage(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.MessageLog log, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object updateDeliveryStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.NotNull()
    java.lang.String deliveredAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object postWebhook(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.WebhookPayload payload, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0007"}, d2 = {"Lcom/fuelone/smsgateway/data/SupabaseRepository$Companion;", "", "()V", "JSON_TYPE", "Lokhttp3/MediaType;", "TAG", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}