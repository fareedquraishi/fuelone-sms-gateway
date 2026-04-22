package com.fuelone.smsgateway.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

// ─── Room Entity ─────────────────────────────────────────────────────────────
@Entity(tableName = "messages")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val direction: String,          // "inbound" | "outbound"
    val phone: String,
    val message: String,
    val status: String,             // sent|failed|delivered|received|pending
    val customerName: String? = "",
    val messageId: String = "",
    val gatewayMode: String = "",
    val keywordMatched: String? = null,
    val autoReplySent: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val deliveredAt: Long? = null,
    val readAt: Long? = null,
    val supabaseLogged: Boolean = false,
    val attempts: Int = 0
)

// ─── DAO ──────────────────────────────────────────────────────────────────────
@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: MessageEntity): Long

    @Update
    suspend fun update(message: MessageEntity)

    @Query("SELECT * FROM messages ORDER BY createdAt DESC LIMIT 50")
    suspend fun getRecent(): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE direction = 'inbound' ORDER BY createdAt DESC LIMIT 50")
    suspend fun getInbox(): List<MessageEntity>

    @Query("SELECT * FROM messages WHERE direction = 'inbound' AND readAt IS NULL")
    suspend fun getUnreadInbox(): List<MessageEntity>

    @Query("SELECT COUNT(*) FROM messages WHERE direction = 'outbound' AND createdAt > :startOfDay")
    suspend fun countSentToday(startOfDay: Long): Int

    @Query("SELECT * FROM messages WHERE messageId = :messageId LIMIT 1")
    suspend fun findByMessageId(messageId: String): MessageEntity?

    @Query("UPDATE messages SET status = :status, deliveredAt = :deliveredAt WHERE messageId = :messageId")
    suspend fun updateStatus(messageId: String, status: String, deliveredAt: Long? = null)

    @Query("UPDATE messages SET readAt = :readAt WHERE id = :id")
    suspend fun markRead(id: Long, readAt: Long = System.currentTimeMillis())

    @Query("SELECT * FROM messages WHERE supabaseLogged = 0 ORDER BY createdAt ASC LIMIT 20")
    suspend fun getPendingSupabaseSync(): List<MessageEntity>

    @Query("UPDATE messages SET supabaseLogged = 1 WHERE id = :id")
    suspend fun markSupabaseSynced(id: Long)

    @Query("SELECT * FROM messages WHERE direction = 'outbound' AND status = 'pending' ORDER BY createdAt ASC")
    suspend fun getPendingOutbound(): List<MessageEntity>

    @Query("DELETE FROM messages WHERE createdAt < :cutoff")
    suspend fun deleteOlderThan(cutoff: Long)

    @Query("SELECT * FROM messages WHERE direction = 'inbound' ORDER BY createdAt DESC")
    fun observeInbox(): Flow<List<MessageEntity>>
}

// ─── Room Database ────────────────────────────────────────────────────────────
@Database(entities = [MessageEntity::class], version = 2, exportSchema = false)
abstract class GatewayDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        @Volatile private var INSTANCE: GatewayDatabase? = null

        fun getInstance(context: android.content.Context): GatewayDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    GatewayDatabase::class.java,
                    "gateway_messages.db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { INSTANCE = it }
            }
    }
}
