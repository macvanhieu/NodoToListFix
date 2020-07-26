package com.dell.noteapp.database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.dell.noteapp.dao.NoteDao;
import com.dell.noteapp.dao.NoteDao_Impl;
import java.lang.IllegalStateException;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;

@SuppressWarnings("unchecked")
public final class NoteDatabase_Impl extends NoteDatabase {
  private volatile NoteDao _noteDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Note` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `title` TEXT, `content` TEXT, `date` TEXT, `favorite` INTEGER NOT NULL, `finishTodo` INTEGER NOT NULL, `time` TEXT, `repeat` TEXT, `setRepeat` TEXT, `repeatInterval` TEXT, `activityNotifi` TEXT, `mRepeat` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, \"0f093761667039b82cf541c43b5f4ca7\")");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `Note`");
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
      protected void validateMigration(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsNote = new HashMap<String, TableInfo.Column>(12);
        _columnsNote.put("id", new TableInfo.Column("id", "INTEGER", true, 1));
        _columnsNote.put("title", new TableInfo.Column("title", "TEXT", false, 0));
        _columnsNote.put("content", new TableInfo.Column("content", "TEXT", false, 0));
        _columnsNote.put("date", new TableInfo.Column("date", "TEXT", false, 0));
        _columnsNote.put("favorite", new TableInfo.Column("favorite", "INTEGER", true, 0));
        _columnsNote.put("finishTodo", new TableInfo.Column("finishTodo", "INTEGER", true, 0));
        _columnsNote.put("time", new TableInfo.Column("time", "TEXT", false, 0));
        _columnsNote.put("repeat", new TableInfo.Column("repeat", "TEXT", false, 0));
        _columnsNote.put("setRepeat", new TableInfo.Column("setRepeat", "TEXT", false, 0));
        _columnsNote.put("repeatInterval", new TableInfo.Column("repeatInterval", "TEXT", false, 0));
        _columnsNote.put("activityNotifi", new TableInfo.Column("activityNotifi", "TEXT", false, 0));
        _columnsNote.put("mRepeat", new TableInfo.Column("mRepeat", "INTEGER", true, 0));
        final HashSet<TableInfo.ForeignKey> _foreignKeysNote = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesNote = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoNote = new TableInfo("Note", _columnsNote, _foreignKeysNote, _indicesNote);
        final TableInfo _existingNote = TableInfo.read(_db, "Note");
        if (! _infoNote.equals(_existingNote)) {
          throw new IllegalStateException("Migration didn't properly handle Note(com.dell.noteapp.entity.Note).\n"
                  + " Expected:\n" + _infoNote + "\n"
                  + " Found:\n" + _existingNote);
        }
      }
    }, "0f093761667039b82cf541c43b5f4ca7", "d7b041b930a655bfa42349a37baaf732");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    return new InvalidationTracker(this, "Note");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `Note`");
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
  public NoteDao getNoteDao() {
    if (_noteDao != null) {
      return _noteDao;
    } else {
      synchronized(this) {
        if(_noteDao == null) {
          _noteDao = new NoteDao_Impl(this);
        }
        return _noteDao;
      }
    }
  }
}
