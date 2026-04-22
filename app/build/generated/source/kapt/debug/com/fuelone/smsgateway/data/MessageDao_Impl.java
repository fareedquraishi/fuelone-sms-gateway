package com.fuelone.smsgateway.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MessageDao_Impl implements MessageDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MessageEntity> __insertionAdapterOfMessageEntity;

  private final EntityDeletionOrUpdateAdapter<MessageEntity> __updateAdapterOfMessageEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStatus;

  private final SharedSQLiteStatement __preparedStmtOfMarkRead;

  private final SharedSQLiteStatement __preparedStmtOfMarkSupabaseSynced;

  private final SharedSQLiteStatement __preparedStmtOfDeleteOlderThan;

  public MessageDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMessageEntity = new EntityInsertionAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `messages` (`id`,`direction`,`phone`,`message`,`status`,`customerName`,`messageId`,`gatewayMode`,`keywordMatched`,`autoReplySent`,`createdAt`,`deliveredAt`,`readAt`,`supabaseLogged`,`attempts`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDirection() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDirection());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhone());
        }
        if (entity.getMessage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMessage());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getCustomerName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCustomerName());
        }
        if (entity.getMessageId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMessageId());
        }
        if (entity.getGatewayMode() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGatewayMode());
        }
        if (entity.getKeywordMatched() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getKeywordMatched());
        }
        final int _tmp = entity.getAutoReplySent() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getDeliveredAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getDeliveredAt());
        }
        if (entity.getReadAt() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getReadAt());
        }
        final int _tmp_1 = entity.getSupabaseLogged() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        statement.bindLong(15, entity.getAttempts());
      }
    };
    this.__updateAdapterOfMessageEntity = new EntityDeletionOrUpdateAdapter<MessageEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `messages` SET `id` = ?,`direction` = ?,`phone` = ?,`message` = ?,`status` = ?,`customerName` = ?,`messageId` = ?,`gatewayMode` = ?,`keywordMatched` = ?,`autoReplySent` = ?,`createdAt` = ?,`deliveredAt` = ?,`readAt` = ?,`supabaseLogged` = ?,`attempts` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MessageEntity entity) {
        statement.bindLong(1, entity.getId());
        if (entity.getDirection() == null) {
          statement.bindNull(2);
        } else {
          statement.bindString(2, entity.getDirection());
        }
        if (entity.getPhone() == null) {
          statement.bindNull(3);
        } else {
          statement.bindString(3, entity.getPhone());
        }
        if (entity.getMessage() == null) {
          statement.bindNull(4);
        } else {
          statement.bindString(4, entity.getMessage());
        }
        if (entity.getStatus() == null) {
          statement.bindNull(5);
        } else {
          statement.bindString(5, entity.getStatus());
        }
        if (entity.getCustomerName() == null) {
          statement.bindNull(6);
        } else {
          statement.bindString(6, entity.getCustomerName());
        }
        if (entity.getMessageId() == null) {
          statement.bindNull(7);
        } else {
          statement.bindString(7, entity.getMessageId());
        }
        if (entity.getGatewayMode() == null) {
          statement.bindNull(8);
        } else {
          statement.bindString(8, entity.getGatewayMode());
        }
        if (entity.getKeywordMatched() == null) {
          statement.bindNull(9);
        } else {
          statement.bindString(9, entity.getKeywordMatched());
        }
        final int _tmp = entity.getAutoReplySent() ? 1 : 0;
        statement.bindLong(10, _tmp);
        statement.bindLong(11, entity.getCreatedAt());
        if (entity.getDeliveredAt() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getDeliveredAt());
        }
        if (entity.getReadAt() == null) {
          statement.bindNull(13);
        } else {
          statement.bindLong(13, entity.getReadAt());
        }
        final int _tmp_1 = entity.getSupabaseLogged() ? 1 : 0;
        statement.bindLong(14, _tmp_1);
        statement.bindLong(15, entity.getAttempts());
        statement.bindLong(16, entity.getId());
      }
    };
    this.__preparedStmtOfUpdateStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET status = ?, deliveredAt = ? WHERE messageId = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkRead = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET readAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfMarkSupabaseSynced = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE messages SET supabaseLogged = 1 WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteOlderThan = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM messages WHERE createdAt < ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final MessageEntity message, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMessageEntity.insertAndReturnId(message);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object update(final MessageEntity message, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMessageEntity.handle(message);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStatus(final String messageId, final String status, final Long deliveredAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStatus.acquire();
        int _argIndex = 1;
        if (status == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, status);
        }
        _argIndex = 2;
        if (deliveredAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, deliveredAt);
        }
        _argIndex = 3;
        if (messageId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, messageId);
        }
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markRead(final long id, final long readAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkRead.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, readAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkRead.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object markSupabaseSynced(final long id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfMarkSupabaseSynced.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfMarkSupabaseSynced.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteOlderThan(final long cutoff, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteOlderThan.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, cutoff);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteOlderThan.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecent(final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages ORDER BY createdAt DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getInbox(final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE direction = 'inbound' ORDER BY createdAt DESC LIMIT 50";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getUnreadInbox(final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE direction = 'inbound' AND readAt IS NULL";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object countSentToday(final long startOfDay,
      final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM messages WHERE direction = 'outbound' AND createdAt > ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startOfDay);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final Integer _tmp;
            if (_cursor.isNull(0)) {
              _tmp = null;
            } else {
              _tmp = _cursor.getInt(0);
            }
            _result = _tmp;
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object findByMessageId(final String messageId,
      final Continuation<? super MessageEntity> $completion) {
    final String _sql = "SELECT * FROM messages WHERE messageId = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (messageId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, messageId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<MessageEntity>() {
      @Override
      @Nullable
      public MessageEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final MessageEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _result = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingSupabaseSync(
      final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE supabaseLogged = 0 ORDER BY createdAt ASC LIMIT 20";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getPendingOutbound(final Continuation<? super List<MessageEntity>> $completion) {
    final String _sql = "SELECT * FROM messages WHERE direction = 'outbound' AND status = 'pending' ORDER BY createdAt ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<MessageEntity>> observeInbox() {
    final String _sql = "SELECT * FROM messages WHERE direction = 'inbound' ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"messages"}, new Callable<List<MessageEntity>>() {
      @Override
      @NonNull
      public List<MessageEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDirection = CursorUtil.getColumnIndexOrThrow(_cursor, "direction");
          final int _cursorIndexOfPhone = CursorUtil.getColumnIndexOrThrow(_cursor, "phone");
          final int _cursorIndexOfMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "message");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfCustomerName = CursorUtil.getColumnIndexOrThrow(_cursor, "customerName");
          final int _cursorIndexOfMessageId = CursorUtil.getColumnIndexOrThrow(_cursor, "messageId");
          final int _cursorIndexOfGatewayMode = CursorUtil.getColumnIndexOrThrow(_cursor, "gatewayMode");
          final int _cursorIndexOfKeywordMatched = CursorUtil.getColumnIndexOrThrow(_cursor, "keywordMatched");
          final int _cursorIndexOfAutoReplySent = CursorUtil.getColumnIndexOrThrow(_cursor, "autoReplySent");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfDeliveredAt = CursorUtil.getColumnIndexOrThrow(_cursor, "deliveredAt");
          final int _cursorIndexOfReadAt = CursorUtil.getColumnIndexOrThrow(_cursor, "readAt");
          final int _cursorIndexOfSupabaseLogged = CursorUtil.getColumnIndexOrThrow(_cursor, "supabaseLogged");
          final int _cursorIndexOfAttempts = CursorUtil.getColumnIndexOrThrow(_cursor, "attempts");
          final List<MessageEntity> _result = new ArrayList<MessageEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MessageEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDirection;
            if (_cursor.isNull(_cursorIndexOfDirection)) {
              _tmpDirection = null;
            } else {
              _tmpDirection = _cursor.getString(_cursorIndexOfDirection);
            }
            final String _tmpPhone;
            if (_cursor.isNull(_cursorIndexOfPhone)) {
              _tmpPhone = null;
            } else {
              _tmpPhone = _cursor.getString(_cursorIndexOfPhone);
            }
            final String _tmpMessage;
            if (_cursor.isNull(_cursorIndexOfMessage)) {
              _tmpMessage = null;
            } else {
              _tmpMessage = _cursor.getString(_cursorIndexOfMessage);
            }
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpCustomerName;
            if (_cursor.isNull(_cursorIndexOfCustomerName)) {
              _tmpCustomerName = null;
            } else {
              _tmpCustomerName = _cursor.getString(_cursorIndexOfCustomerName);
            }
            final String _tmpMessageId;
            if (_cursor.isNull(_cursorIndexOfMessageId)) {
              _tmpMessageId = null;
            } else {
              _tmpMessageId = _cursor.getString(_cursorIndexOfMessageId);
            }
            final String _tmpGatewayMode;
            if (_cursor.isNull(_cursorIndexOfGatewayMode)) {
              _tmpGatewayMode = null;
            } else {
              _tmpGatewayMode = _cursor.getString(_cursorIndexOfGatewayMode);
            }
            final String _tmpKeywordMatched;
            if (_cursor.isNull(_cursorIndexOfKeywordMatched)) {
              _tmpKeywordMatched = null;
            } else {
              _tmpKeywordMatched = _cursor.getString(_cursorIndexOfKeywordMatched);
            }
            final boolean _tmpAutoReplySent;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfAutoReplySent);
            _tmpAutoReplySent = _tmp != 0;
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final Long _tmpDeliveredAt;
            if (_cursor.isNull(_cursorIndexOfDeliveredAt)) {
              _tmpDeliveredAt = null;
            } else {
              _tmpDeliveredAt = _cursor.getLong(_cursorIndexOfDeliveredAt);
            }
            final Long _tmpReadAt;
            if (_cursor.isNull(_cursorIndexOfReadAt)) {
              _tmpReadAt = null;
            } else {
              _tmpReadAt = _cursor.getLong(_cursorIndexOfReadAt);
            }
            final boolean _tmpSupabaseLogged;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfSupabaseLogged);
            _tmpSupabaseLogged = _tmp_1 != 0;
            final int _tmpAttempts;
            _tmpAttempts = _cursor.getInt(_cursorIndexOfAttempts);
            _item = new MessageEntity(_tmpId,_tmpDirection,_tmpPhone,_tmpMessage,_tmpStatus,_tmpCustomerName,_tmpMessageId,_tmpGatewayMode,_tmpKeywordMatched,_tmpAutoReplySent,_tmpCreatedAt,_tmpDeliveredAt,_tmpReadAt,_tmpSupabaseLogged,_tmpAttempts);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
