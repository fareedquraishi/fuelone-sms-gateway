package com.fuelone.smsgateway.model;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0013\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B+\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0007\u00a2\u0006\u0002\u0010\tJ\t\u0010\u0015\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\u0016\u001a\u00020\u0005H\u00c6\u0003J\t\u0010\u0017\u001a\u00020\u0007H\u00c6\u0003J\t\u0010\u0018\u001a\u00020\u0007H\u00c6\u0003J1\u0010\u0019\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u00072\b\b\u0002\u0010\b\u001a\u00020\u0007H\u00c6\u0001J\u0013\u0010\u001a\u001a\u00020\u001b2\b\u0010\u001c\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u001d\u001a\u00020\u0005H\u00d6\u0001J\t\u0010\u001e\u001a\u00020\u001fH\u00d6\u0001R\u001a\u0010\u0004\u001a\u00020\u0005X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u0011\u0010\u0006\u001a\u00020\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u001a\u0010\b\u001a\u00020\u0007X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0010\u0010\u000f\"\u0004\b\u0011\u0010\u0012R\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006 "}, d2 = {"Lcom/fuelone/smsgateway/model/QueuedSms;", "", "sendRequest", "Lcom/fuelone/smsgateway/model/SendRequest;", "attempts", "", "createdAt", "", "nextRetryAt", "(Lcom/fuelone/smsgateway/model/SendRequest;IJJ)V", "getAttempts", "()I", "setAttempts", "(I)V", "getCreatedAt", "()J", "getNextRetryAt", "setNextRetryAt", "(J)V", "getSendRequest", "()Lcom/fuelone/smsgateway/model/SendRequest;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app_release"})
public final class QueuedSms {
    @org.jetbrains.annotations.NotNull()
    private final com.fuelone.smsgateway.model.SendRequest sendRequest = null;
    private int attempts;
    private final long createdAt = 0L;
    private long nextRetryAt;
    
    public QueuedSms(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.SendRequest sendRequest, int attempts, long createdAt, long nextRetryAt) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.SendRequest getSendRequest() {
        return null;
    }
    
    public final int getAttempts() {
        return 0;
    }
    
    public final void setAttempts(int p0) {
    }
    
    public final long getCreatedAt() {
        return 0L;
    }
    
    public final long getNextRetryAt() {
        return 0L;
    }
    
    public final void setNextRetryAt(long p0) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.SendRequest component1() {
        return null;
    }
    
    public final int component2() {
        return 0;
    }
    
    public final long component3() {
        return 0L;
    }
    
    public final long component4() {
        return 0L;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.fuelone.smsgateway.model.QueuedSms copy(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.model.SendRequest sendRequest, int attempts, long createdAt, long nextRetryAt) {
        return null;
    }
    
    @java.lang.Override()
    public boolean equals(@org.jetbrains.annotations.Nullable()
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override()
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }
}