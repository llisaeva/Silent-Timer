package com.lisaeva.silenttimer.persistence;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SilentIntervalDao_Impl implements SilentIntervalDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SilentIntervalData> __insertionAdapterOfSilentIntervalData;

  private final EntityDeletionOrUpdateAdapter<SilentIntervalData> __deletionAdapterOfSilentIntervalData;

  private final EntityDeletionOrUpdateAdapter<SilentIntervalData> __updateAdapterOfSilentIntervalData;

  public SilentIntervalDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSilentIntervalData = new EntityInsertionAdapter<SilentIntervalData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `silent_timer_data` (`id`,`title`,`description`,`start_time`,`end_time`,`weekdays`,`repeat`,`show_description`,`position`) VALUES (?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentIntervalData value) {
        if (value.getUuid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUuid());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        if (value.getStartTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStartTime());
        }
        if (value.getEndTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEndTime());
        }
        if (value.getWeekdays() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getWeekdays());
        }
        stmt.bindLong(7, value.getRepeat());
        stmt.bindLong(8, value.getShowDescription());
        stmt.bindLong(9, value.getPosition());
      }
    };
    this.__deletionAdapterOfSilentIntervalData = new EntityDeletionOrUpdateAdapter<SilentIntervalData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `silent_timer_data` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentIntervalData value) {
        if (value.getUuid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUuid());
        }
      }
    };
    this.__updateAdapterOfSilentIntervalData = new EntityDeletionOrUpdateAdapter<SilentIntervalData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `silent_timer_data` SET `id` = ?,`title` = ?,`description` = ?,`start_time` = ?,`end_time` = ?,`weekdays` = ?,`repeat` = ?,`show_description` = ?,`position` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentIntervalData value) {
        if (value.getUuid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUuid());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getDescription() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDescription());
        }
        if (value.getStartTime() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStartTime());
        }
        if (value.getEndTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEndTime());
        }
        if (value.getWeekdays() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getWeekdays());
        }
        stmt.bindLong(7, value.getRepeat());
        stmt.bindLong(8, value.getShowDescription());
        stmt.bindLong(9, value.getPosition());
        if (value.getUuid() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getUuid());
        }
      }
    };
  }

  @Override
  public void insert(final SilentIntervalData silentIntervalData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSilentIntervalData.insert(silentIntervalData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final SilentIntervalData silentIntervalData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSilentIntervalData.handle(silentIntervalData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final SilentIntervalData silentIntervalData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSilentIntervalData.handle(silentIntervalData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SilentIntervalData> getAll() {
    final String _sql = "SELECT * FROM silent_timer_data";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
      final int _cursorIndexOfWeekdays = CursorUtil.getColumnIndexOrThrow(_cursor, "weekdays");
      final int _cursorIndexOfRepeat = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat");
      final int _cursorIndexOfShowDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "show_description");
      final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
      final List<SilentIntervalData> _result = new ArrayList<SilentIntervalData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SilentIntervalData _item;
        final String _tmpUuid;
        _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpStartTime;
        _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
        final String _tmpEndTime;
        _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
        final String _tmpWeekdays;
        _tmpWeekdays = _cursor.getString(_cursorIndexOfWeekdays);
        final int _tmpRepeat;
        _tmpRepeat = _cursor.getInt(_cursorIndexOfRepeat);
        final int _tmpShowDescription;
        _tmpShowDescription = _cursor.getInt(_cursorIndexOfShowDescription);
        final int _tmpPosition;
        _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
        _item = new SilentIntervalData(_tmpUuid,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpWeekdays,_tmpRepeat,_tmpShowDescription,_tmpPosition);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SilentIntervalData get(final String uuid) {
    final String _sql = "SELECT * FROM silent_timer_data WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (uuid == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, uuid);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartTime = CursorUtil.getColumnIndexOrThrow(_cursor, "start_time");
      final int _cursorIndexOfEndTime = CursorUtil.getColumnIndexOrThrow(_cursor, "end_time");
      final int _cursorIndexOfWeekdays = CursorUtil.getColumnIndexOrThrow(_cursor, "weekdays");
      final int _cursorIndexOfRepeat = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat");
      final int _cursorIndexOfShowDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "show_description");
      final int _cursorIndexOfPosition = CursorUtil.getColumnIndexOrThrow(_cursor, "position");
      final SilentIntervalData _result;
      if(_cursor.moveToFirst()) {
        final String _tmpUuid;
        _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpStartTime;
        _tmpStartTime = _cursor.getString(_cursorIndexOfStartTime);
        final String _tmpEndTime;
        _tmpEndTime = _cursor.getString(_cursorIndexOfEndTime);
        final String _tmpWeekdays;
        _tmpWeekdays = _cursor.getString(_cursorIndexOfWeekdays);
        final int _tmpRepeat;
        _tmpRepeat = _cursor.getInt(_cursorIndexOfRepeat);
        final int _tmpShowDescription;
        _tmpShowDescription = _cursor.getInt(_cursorIndexOfShowDescription);
        final int _tmpPosition;
        _tmpPosition = _cursor.getInt(_cursorIndexOfPosition);
        _result = new SilentIntervalData(_tmpUuid,_tmpTitle,_tmpDescription,_tmpStartTime,_tmpEndTime,_tmpWeekdays,_tmpRepeat,_tmpShowDescription,_tmpPosition);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
