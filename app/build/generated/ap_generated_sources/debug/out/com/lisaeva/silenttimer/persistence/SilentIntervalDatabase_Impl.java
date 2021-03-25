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
public final class SilentIntervalDatabase_Impl extends SilentIntervalDatabase {
  private volatile SilentIntervalDao _silentIntervalDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `silent_timer_data` (`id` TEXT NOT NULL, `title` TEXT, `description` TEXT, `start_time` TEXT, `end_time` TEXT, `weekdays` TEXT, `repeat` INTEGER NOT NULL, `show_description` INTEGER NOT NULL, `position` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '16fc6642bcb1ffb1ddc01778ad469c9f')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `silent_timer_data`");
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
        final HashMap<String, TableInfo.Column> _columnsSilentTimerData = new HashMap<String, TableInfo.Column>(9);
        _columnsSilentTimerData.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("description", new TableInfo.Column("description", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("start_time", new TableInfo.Column("start_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("end_time", new TableInfo.Column("end_time", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("weekdays", new TableInfo.Column("weekdays", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("repeat", new TableInfo.Column("repeat", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("show_description", new TableInfo.Column("show_description", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSilentTimerData.put("position", new TableInfo.Column("position", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSilentTimerData = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSilentTimerData = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSilentTimerData = new TableInfo("silent_timer_data", _columnsSilentTimerData, _foreignKeysSilentTimerData, _indicesSilentTimerData);
        final TableInfo _existingSilentTimerData = TableInfo.read(_db, "silent_timer_data");
        if (! _infoSilentTimerData.equals(_existingSilentTimerData)) {
          return new RoomOpenHelper.ValidationResult(false, "silent_timer_data(com.lisaeva.silenttimer.persistence.SilentIntervalData).\n"
                  + " Expected:\n" + _infoSilentTimerData + "\n"
                  + " Found:\n" + _existingSilentTimerData);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "16fc6642bcb1ffb1ddc01778ad469c9f", "f47b56a16f12114a25dc267a33926556");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "silent_timer_data");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `silent_timer_data`");
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
  public SilentIntervalDao silentIntervalDao() {
    if (_silentIntervalDao != null) {
      return _silentIntervalDao;
    } else {
      synchronized(this) {
        if(_silentIntervalDao == null) {
          _silentIntervalDao = new SilentIntervalDao_Impl(this);
        }
        return _silentIntervalDao;
      }
    }
  }
}
