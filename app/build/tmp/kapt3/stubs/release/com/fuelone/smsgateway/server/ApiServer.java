package com.fuelone.smsgateway.server;

/**
 * ApiServer — NanoHTTPD-based HTTP API server
 *
 * Endpoints:
 *  POST /send    — Send SMS (Bearer token auth)
 *  GET  /status  — Gateway status
 *  GET  /inbox   — Last 50 received messages
 *  POST /reply   — Send a reply to a phone number
 *
 * All responses are JSON. Bearer token verified on write endpoints.
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000V\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\t\u0018\u0000 %2\u00020\u0001:\u0003%&\'B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u00a2\u0006\u0002\u0010\fJ\b\u0010\u0011\u001a\u00020\u0012H\u0002J\b\u0010\u0013\u001a\u00020\u0012H\u0002J\u0010\u0010\u0014\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0012H\u0002J\u0010\u0010\u0018\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0019\u001a\u00020\u0012H\u0002J\u0010\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0018\u0010\u001c\u001a\u00020\u00122\u0006\u0010\u001d\u001a\u00020\u00032\u0006\u0010\u001e\u001a\u00020\u001fH\u0002J\u0010\u0010 \u001a\u00020\u00122\u0006\u0010!\u001a\u00020\u001fH\u0002J\u0010\u0010\"\u001a\u00020\u001f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010#\u001a\u00020\u00122\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\b\u0010$\u001a\u00020\u0012H\u0002R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006("}, d2 = {"Lcom/fuelone/smsgateway/server/ApiServer;", "Lfi/iki/elonen/NanoHTTPD;", "port", "", "prefs", "Lcom/fuelone/smsgateway/data/AppPreferences;", "db", "Lcom/fuelone/smsgateway/data/GatewayDatabase;", "smsSender", "Lcom/fuelone/smsgateway/server/ApiServer$SmsSenderCallback;", "statusProvider", "Lcom/fuelone/smsgateway/server/ApiServer$StatusProvider;", "(ILcom/fuelone/smsgateway/data/AppPreferences;Lcom/fuelone/smsgateway/data/GatewayDatabase;Lcom/fuelone/smsgateway/server/ApiServer$SmsSenderCallback;Lcom/fuelone/smsgateway/server/ApiServer$StatusProvider;)V", "gson", "Lcom/google/gson/Gson;", "scope", "Lkotlinx/coroutines/CoroutineScope;", "handleInbox", "Lfi/iki/elonen/NanoHTTPD$Response;", "handleOptions", "handleReply", "session", "Lfi/iki/elonen/NanoHTTPD$IHTTPSession;", "handleRoot", "handleSend", "handleStatus", "isAuthorized", "", "jsonError", "code", "message", "", "jsonOk", "json", "readBody", "serve", "unauthorizedResponse", "Companion", "SmsSenderCallback", "StatusProvider", "app_release"})
public final class ApiServer extends fi.iki.elonen.NanoHTTPD {
    private final int port = 0;
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.data.AppPreferences prefs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.data.GatewayDatabase db = null;
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.server.ApiServer.SmsSenderCallback smsSender = null;
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.server.ApiServer.StatusProvider statusProvider = null;
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "ApiServer";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String JSON_MIME = "application/json";
    @org.jetbrains.annotations.NotNull()
    private static final java.text.SimpleDateFormat ISO_FORMAT = null;
    @org.jetbrains.annotations.NotNull()
    private final com.google.gson.Gson gson = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.CoroutineScope scope = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.fuelone.smsgateway.server.ApiServer.Companion Companion = null;
    
    public ApiServer(int port, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.data.AppPreferences prefs, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.data.GatewayDatabase db, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.server.ApiServer.SmsSenderCallback smsSender, @org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.server.ApiServer.StatusProvider statusProvider) {
        super(0);
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public fi.iki.elonen.NanoHTTPD.Response serve(@org.jetbrains.annotations.NotNull()
    fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleSend(fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleStatus() {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleInbox() {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleReply(fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleOptions() {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response handleRoot() {
        return null;
    }
    
    private final boolean isAuthorized(fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        return false;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response unauthorizedResponse() {
        return null;
    }
    
    private final java.lang.String readBody(fi.iki.elonen.NanoHTTPD.IHTTPSession session) {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response jsonOk(java.lang.String json) {
        return null;
    }
    
    private final fi.iki.elonen.NanoHTTPD.Response jsonError(int code, java.lang.String message) {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\b"}, d2 = {"Lcom/fuelone/smsgateway/server/ApiServer$Companion;", "", "()V", "ISO_FORMAT", "Ljava/text/SimpleDateFormat;", "JSON_MIME", "", "TAG", "app_release"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&\u00a8\u0006\u0006"}, d2 = {"Lcom/fuelone/smsgateway/server/ApiServer$SmsSenderCallback;", "", "sendSms", "", "request", "Lcom/fuelone/smsgateway/model/SendRequest;", "app_release"})
    public static abstract interface SmsSenderCallback {
        
        public abstract boolean sendSms(@org.jetbrains.annotations.NotNull()
        com.fuelone.smsgateway.model.SendRequest request);
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H&\u00a8\u0006\u0004"}, d2 = {"Lcom/fuelone/smsgateway/server/ApiServer$StatusProvider;", "", "getStatus", "Lcom/fuelone/smsgateway/model/GatewayStatus;", "app_release"})
    public static abstract interface StatusProvider {
        
        @org.jetbrains.annotations.NotNull()
        public abstract com.fuelone.smsgateway.model.GatewayStatus getStatus();
    }
}