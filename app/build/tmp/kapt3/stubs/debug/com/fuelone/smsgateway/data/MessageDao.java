package com.fuelone.smsgateway.data;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\u0006\bg\u0018\u00002\u00020\u0001J\u0016\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0016\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0018\u0010\n\u001a\u0004\u0018\u00010\u000b2\u0006\u0010\f\u001a\u00020\rH\u00a7@\u00a2\u0006\u0002\u0010\u000eJ\u0014\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0014\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0010H\u00a7@\u00a2\u0006\u0002\u0010\u0011J\u0016\u0010\u0016\u001a\u00020\u00052\u0006\u0010\u0017\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0018J \u0010\u0019\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u00052\b\b\u0002\u0010\u001b\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u001cJ\u0016\u0010\u001d\u001a\u00020\b2\u0006\u0010\u001a\u001a\u00020\u0005H\u00a7@\u00a2\u0006\u0002\u0010\u0006J\u0014\u0010\u001e\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000b0\u00100\u001fH\'J\u0016\u0010 \u001a\u00020\b2\u0006\u0010\u0017\u001a\u00020\u000bH\u00a7@\u00a2\u0006\u0002\u0010\u0018J*\u0010!\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\"\u001a\u00020\r2\n\b\u0002\u0010#\u001a\u0004\u0018\u00010\u0005H\u00a7@\u00a2\u0006\u0002\u0010$\u00a8\u0006%"}, d2 = {"Lcom/fuelone/smsgateway/data/MessageDao;", "", "countSentToday", "", "startOfDay", "", "(JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "deleteOlderThan", "", "cutoff", "findByMessageId", "Lcom/fuelone/smsgateway/data/MessageEntity;", "messageId", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getInbox", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getPendingOutbound", "getPendingSupabaseSync", "getRecent", "getUnreadInbox", "insert", "message", "(Lcom/fuelone/smsgateway/data/MessageEntity;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markRead", "id", "readAt", "(JJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "markSupabaseSynced", "observeInbox", "Lkotlinx/coroutines/flow/Flow;", "update", "updateStatus", "status", "deliveredAt", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@androidx.room.Dao()
public abstract interface MessageDao {
    
    @androidx.room.Insert(onConflict = 1)
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object insert(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.data.MessageEntity message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Long> $completion);
    
    @androidx.room.Update()
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object update(@org.jetbrains.annotations.NotNull()
    com.fuelone.smsgateway.data.MessageEntity message, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages ORDER BY createdAt DESC LIMIT 50")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getRecent(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fuelone.smsgateway.data.MessageEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE direction = \'inbound\' ORDER BY createdAt DESC LIMIT 50")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getInbox(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fuelone.smsgateway.data.MessageEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE direction = \'inbound\' AND readAt IS NULL")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getUnreadInbox(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fuelone.smsgateway.data.MessageEntity>> $completion);
    
    @androidx.room.Query(value = "SELECT COUNT(*) FROM messages WHERE direction = \'outbound\' AND createdAt > :startOfDay")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object countSentToday(long startOfDay, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Integer> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE messageId = :messageId LIMIT 1")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object findByMessageId(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.fuelone.smsgateway.data.MessageEntity> $completion);
    
    @androidx.room.Query(value = "UPDATE messages SET status = :status, deliveredAt = :deliveredAt WHERE messageId = :messageId")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object updateStatus(@org.jetbrains.annotations.NotNull()
    java.lang.String messageId, @org.jetbrains.annotations.NotNull()
    java.lang.String status, @org.jetbrains.annotations.Nullable()
    java.lang.Long deliveredAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "UPDATE messages SET readAt = :readAt WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markRead(long id, long readAt, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE supabaseLogged = 0 ORDER BY createdAt ASC LIMIT 20")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingSupabaseSync(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fuelone.smsgateway.data.MessageEntity>> $completion);
    
    @androidx.room.Query(value = "UPDATE messages SET supabaseLogged = 1 WHERE id = :id")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object markSupabaseSynced(long id, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE direction = \'outbound\' AND status = \'pending\' ORDER BY createdAt ASC")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getPendingOutbound(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<com.fuelone.smsgateway.data.MessageEntity>> $completion);
    
    @androidx.room.Query(value = "DELETE FROM messages WHERE createdAt < :cutoff")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object deleteOlderThan(long cutoff, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion);
    
    @androidx.room.Query(value = "SELECT * FROM messages WHERE direction = \'inbound\' ORDER BY createdAt DESC")
    @org.jetbrains.annotations.NotNull()
    public abstract kotlinx.coroutines.flow.Flow<java.util.List<com.fuelone.smsgateway.data.MessageEntity>> observeInbox();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}