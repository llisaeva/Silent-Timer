package com.lisaeva.silenttimer.persistence;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SilentAlarmDatabase_Impl extends SilentAlarmDatabase {
  private volatile SilentAlarmDao _silentAlarmDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `silent_alarms` (`id` TEXT NOT NULL, `title` TEXT, `description` TEXT, `start_date` TEXT, `end_date` TEXT, `weekdays` TEXT, `repeat` INTEGER NOT NULL, `show_description` INTEGER NOT NULL, `active` INTEGER NOT NULL, `started` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '46604c6f7d8daa9149b8577370f8a3c9')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `silent_alarms`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSilentAlarms = new HashMap<String, TableInfo.Column>(10);
        _columnsSilentAlarms.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("start_date", new TableInfo.Column("start_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("end_date", new TableInfo.Column("end_date", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("weekdays", new TableInfo.Column("weekdays", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("repeat", new TableInfo.Column("repeat", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("show_description", new TableInfo.Column("show_description", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("active", new TableInfo.Column("active", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentAlarms.put("started", new TableInfo.Column("started", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSilentAlarms = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSilentAlarms = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSilentAlarms = new TableInfo("silent_alarms", _columnsSilentAlarms, _foreignKeysSilentAlarms, _indicesSilentAlarms);
        final TableInfo _existingSilentAlarms = TableInfo.read(_db, "silent_alarms");
        if (! _infoSilentAlarms.equals(_existingSilentAlarms)) {
          return new RoomOpenHelper.ValidationResult(false, "silent_alarms(com.lisaeva.silenttimer.persistence.SilentAlarmData).\n"
                  + " Expected:\n" + _infoSilentAlarms + "\n"
                  + " Found:\n" + _existingSilentAlarms);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "46604c6f7d8daa9149b8577370f8a3c9", "7de7dbd8fbd37c0b5c543ac2326d69d5");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "silent_alarms");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `silent_alarms`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public SilentAlarmDao silentAlarmDao() {
    if (_silentAlarmDao != null) {
      return _silentAlarmDao;
    } else {
      synchronized(this) {
        if(_silentAlarmDao == null) {
          _silentAlarmDao = new SilentAlarmDao_Impl(this);
        }
        return _silentAlarmDao;
      }
    }
  }
}
