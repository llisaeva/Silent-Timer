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
public final class SilentAlarmDao_Impl implements SilentAlarmDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SilentAlarmData> __insertionAdapterOfSilentAlarmData;

  private final EntityDeletionOrUpdateAdapter<SilentAlarmData> __deletionAdapterOfSilentAlarmData;

  private final EntityDeletionOrUpdateAdapter<SilentAlarmData> __updateAdapterOfSilentAlarmData;

  public SilentAlarmDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSilentAlarmData = new EntityInsertionAdapter<SilentAlarmData>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `silent_alarms` (`id`,`title`,`description`,`start_date`,`end_date`,`weekdays`,`repeat`,`show_description`,`active`,`started`) VALUES (?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentAlarmData value) {
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
        if (value.getStartDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStartDate());
        }
        if (value.getEndDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEndDate());
        }
        if (value.getWeekdays() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getWeekdays());
        }
        stmt.bindLong(7, value.getRepeat());
        stmt.bindLong(8, value.getShowDescription());
        stmt.bindLong(9, value.getActive());
        stmt.bindLong(10, value.getStarted());
      }
    };
    this.__deletionAdapterOfSilentAlarmData = new EntityDeletionOrUpdateAdapter<SilentAlarmData>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `silent_alarms` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentAlarmData value) {
        if (value.getUuid() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getUuid());
        }
      }
    };
    this.__updateAdapterOfSilentAlarmData = new EntityDeletionOrUpdateAdapter<SilentAlarmData>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `silent_alarms` SET `id` = ?,`title` = ?,`description` = ?,`start_date` = ?,`end_date` = ?,`weekdays` = ?,`repeat` = ?,`show_description` = ?,`active` = ?,`started` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SilentAlarmData value) {
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
        if (value.getStartDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getStartDate());
        }
        if (value.getEndDate() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getEndDate());
        }
        if (value.getWeekdays() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getWeekdays());
        }
        stmt.bindLong(7, value.getRepeat());
        stmt.bindLong(8, value.getShowDescription());
        stmt.bindLong(9, value.getActive());
        stmt.bindLong(10, value.getStarted());
        if (value.getUuid() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getUuid());
        }
      }
    };
  }

  @Override
  public void insert(final SilentAlarmData silentAlarmData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSilentAlarmData.insert(silentAlarmData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final SilentAlarmData silentAlarmData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSilentAlarmData.handle(silentAlarmData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(final SilentAlarmData silentAlarmData) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSilentAlarmData.handle(silentAlarmData);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<SilentAlarmData> getAll() {
    final String _sql = "SELECT * FROM silent_alarms";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfUuid = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfWeekdays = CursorUtil.getColumnIndexOrThrow(_cursor, "weekdays");
      final int _cursorIndexOfRepeat = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat");
      final int _cursorIndexOfShowDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "show_description");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final List<SilentAlarmData> _result = new ArrayList<SilentAlarmData>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SilentAlarmData _item;
        final String _tmpUuid;
        _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpStartDate;
        _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        final String _tmpEndDate;
        _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        final String _tmpWeekdays;
        _tmpWeekdays = _cursor.getString(_cursorIndexOfWeekdays);
        final int _tmpRepeat;
        _tmpRepeat = _cursor.getInt(_cursorIndexOfRepeat);
        final int _tmpShowDescription;
        _tmpShowDescription = _cursor.getInt(_cursorIndexOfShowDescription);
        final int _tmpActive;
        _tmpActive = _cursor.getInt(_cursorIndexOfActive);
        final int _tmpStarted;
        _tmpStarted = _cursor.getInt(_cursorIndexOfStarted);
        _item = new SilentAlarmData(_tmpUuid,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpWeekdays,_tmpRepeat,_tmpShowDescription,_tmpActive,_tmpStarted);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SilentAlarmData get(final String uuid) {
    final String _sql = "SELECT * FROM silent_alarms WHERE id = ?";
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
      final int _cursorIndexOfStartDate = CursorUtil.getColumnIndexOrThrow(_cursor, "start_date");
      final int _cursorIndexOfEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "end_date");
      final int _cursorIndexOfWeekdays = CursorUtil.getColumnIndexOrThrow(_cursor, "weekdays");
      final int _cursorIndexOfRepeat = CursorUtil.getColumnIndexOrThrow(_cursor, "repeat");
      final int _cursorIndexOfShowDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "show_description");
      final int _cursorIndexOfActive = CursorUtil.getColumnIndexOrThrow(_cursor, "active");
      final int _cursorIndexOfStarted = CursorUtil.getColumnIndexOrThrow(_cursor, "started");
      final SilentAlarmData _result;
      if(_cursor.moveToFirst()) {
        final String _tmpUuid;
        _tmpUuid = _cursor.getString(_cursorIndexOfUuid);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        final String _tmpDescription;
        _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
        final String _tmpStartDate;
        _tmpStartDate = _cursor.getString(_cursorIndexOfStartDate);
        final String _tmpEndDate;
        _tmpEndDate = _cursor.getString(_cursorIndexOfEndDate);
        final String _tmpWeekdays;
        _tmpWeekdays = _cursor.getString(_cursorIndexOfWeekdays);
        final int _tmpRepeat;
        _tmpRepeat = _cursor.getInt(_cursorIndexOfRepeat);
        final int _tmpShowDescription;
        _tmpShowDescription = _cursor.getInt(_cursorIndexOfShowDescription);
        final int _tmpActive;
        _tmpActive = _cursor.getInt(_cursorIndexOfActive);
        final int _tmpStarted;
        _tmpStarted = _cursor.getInt(_cursorIndexOfStarted);
        _result = new SilentAlarmData(_tmpUuid,_tmpTitle,_tmpDescription,_tmpStartDate,_tmpEndDate,_tmpWeekdays,_tmpRepeat,_tmpShowDescription,_tmpActive,_tmpStarted);
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
