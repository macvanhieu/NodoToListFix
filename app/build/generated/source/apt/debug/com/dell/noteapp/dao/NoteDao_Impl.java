package com.dell.noteapp.dao;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.dell.noteapp.entity.Note;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public final class NoteDao_Impl implements NoteDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter __insertionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter __deletionAdapterOfNote;

  private final EntityDeletionOrUpdateAdapter __updateAdapterOfNote;

  private final SharedSQLiteStatement __preparedStmtOfUpdateFV;

  private final SharedSQLiteStatement __preparedStmtOfInsertFinish;

  public NoteDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfNote = new EntityInsertionAdapter<Note>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `Note`(`id`,`title`,`content`,`date`,`favorite`,`finishTodo`,`time`,`repeat`,`setRepeat`,`repeatInterval`,`activityNotifi`,`mRepeat`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Note value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getContent() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getContent());
        }
        if (value.getDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDate());
        }
        final int _tmp;
        _tmp = value.isFavorite() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isFinishTodo() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        if (value.getTime() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getTime());
        }
        if (value.getRepeat() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getRepeat());
        }
        if (value.getSetRepeat() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSetRepeat());
        }
        if (value.getRepeatInterval() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getRepeatInterval());
        }
        if (value.getActivityNotifi() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getActivityNotifi());
        }
        final int _tmp_2;
        _tmp_2 = value.isCheckRepeat() ? 1 : 0;
        stmt.bindLong(12, _tmp_2);
      }
    };
    this.__deletionAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `Note` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Note value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfNote = new EntityDeletionOrUpdateAdapter<Note>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR REPLACE `Note` SET `id` = ?,`title` = ?,`content` = ?,`date` = ?,`favorite` = ?,`finishTodo` = ?,`time` = ?,`repeat` = ?,`setRepeat` = ?,`repeatInterval` = ?,`activityNotifi` = ?,`mRepeat` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Note value) {
        stmt.bindLong(1, value.getId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getContent() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getContent());
        }
        if (value.getDate() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getDate());
        }
        final int _tmp;
        _tmp = value.isFavorite() ? 1 : 0;
        stmt.bindLong(5, _tmp);
        final int _tmp_1;
        _tmp_1 = value.isFinishTodo() ? 1 : 0;
        stmt.bindLong(6, _tmp_1);
        if (value.getTime() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getTime());
        }
        if (value.getRepeat() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getRepeat());
        }
        if (value.getSetRepeat() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSetRepeat());
        }
        if (value.getRepeatInterval() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getRepeatInterval());
        }
        if (value.getActivityNotifi() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindString(11, value.getActivityNotifi());
        }
        final int _tmp_2;
        _tmp_2 = value.isCheckRepeat() ? 1 : 0;
        stmt.bindLong(12, _tmp_2);
        stmt.bindLong(13, value.getId());
      }
    };
    this.__preparedStmtOfUpdateFV = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE Note SET favorite = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfInsertFinish = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "UPDATE Note SET finishTodo = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public void insert(Note note) {
    __db.beginTransaction();
    try {
      __insertionAdapterOfNote.insert(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(Note note) {
    __db.beginTransaction();
    try {
      __deletionAdapterOfNote.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void update(Note note) {
    __db.beginTransaction();
    try {
      __updateAdapterOfNote.handle(note);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateFV(boolean isFV, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateFV.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      final int _tmp;
      _tmp = isFV ? 1 : 0;
      _stmt.bindLong(_argIndex, _tmp);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfUpdateFV.release(_stmt);
    }
  }

  @Override
  public void insertFinish(boolean finishTodo, int id) {
    final SupportSQLiteStatement _stmt = __preparedStmtOfInsertFinish.acquire();
    __db.beginTransaction();
    try {
      int _argIndex = 1;
      final int _tmp;
      _tmp = finishTodo ? 1 : 0;
      _stmt.bindLong(_argIndex, _tmp);
      _argIndex = 2;
      _stmt.bindLong(_argIndex, id);
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfInsertFinish.release(_stmt);
    }
  }

  @Override
  public List<Note> getAllNote() {
    final String _sql = "Select * from Note";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfContent = _cursor.getColumnIndexOrThrow("content");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfFavorite = _cursor.getColumnIndexOrThrow("favorite");
      final int _cursorIndexOfFinishTodo = _cursor.getColumnIndexOrThrow("finishTodo");
      final int _cursorIndexOfTime = _cursor.getColumnIndexOrThrow("time");
      final int _cursorIndexOfRepeat = _cursor.getColumnIndexOrThrow("repeat");
      final int _cursorIndexOfSetRepeat = _cursor.getColumnIndexOrThrow("setRepeat");
      final int _cursorIndexOfRepeatInterval = _cursor.getColumnIndexOrThrow("repeatInterval");
      final int _cursorIndexOfActivityNotifi = _cursor.getColumnIndexOrThrow("activityNotifi");
      final int _cursorIndexOfCheckRepeat = _cursor.getColumnIndexOrThrow("mRepeat");
      final List<Note> _result = new ArrayList<Note>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Note _item;
        _item = new Note();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _item.setTitle(_tmpTitle);
        final String _tmpContent;
        _tmpContent = _cursor.getString(_cursorIndexOfContent);
        _item.setContent(_tmpContent);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _item.setDate(_tmpDate);
        final boolean _tmpFavorite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfFavorite);
        _tmpFavorite = _tmp != 0;
        _item.setFavorite(_tmpFavorite);
        final boolean _tmpFinishTodo;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfFinishTodo);
        _tmpFinishTodo = _tmp_1 != 0;
        _item.setFinishTodo(_tmpFinishTodo);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item.setTime(_tmpTime);
        final String _tmpRepeat;
        _tmpRepeat = _cursor.getString(_cursorIndexOfRepeat);
        _item.setRepeat(_tmpRepeat);
        final String _tmpSetRepeat;
        _tmpSetRepeat = _cursor.getString(_cursorIndexOfSetRepeat);
        _item.setRepeat(_tmpSetRepeat);
        final String _tmpRepeatInterval;
        _tmpRepeatInterval = _cursor.getString(_cursorIndexOfRepeatInterval);
        _item.setRepeatInterval(_tmpRepeatInterval);
        final String _tmpActivityNotifi;
        _tmpActivityNotifi = _cursor.getString(_cursorIndexOfActivityNotifi);
        _item.setActivityNotifi(_tmpActivityNotifi);
        final boolean _tmpCheckRepeat;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfCheckRepeat);
        _tmpCheckRepeat = _tmp_2 != 0;
        _item.setCheckRepeat(_tmpCheckRepeat);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Note getDesception(int id) {
    final String _sql = "Select * from Note WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final Cursor _cursor = __db.query(_statement);
    try {
      final int _cursorIndexOfId = _cursor.getColumnIndexOrThrow("id");
      final int _cursorIndexOfTitle = _cursor.getColumnIndexOrThrow("title");
      final int _cursorIndexOfContent = _cursor.getColumnIndexOrThrow("content");
      final int _cursorIndexOfDate = _cursor.getColumnIndexOrThrow("date");
      final int _cursorIndexOfFavorite = _cursor.getColumnIndexOrThrow("favorite");
      final int _cursorIndexOfFinishTodo = _cursor.getColumnIndexOrThrow("finishTodo");
      final int _cursorIndexOfTime = _cursor.getColumnIndexOrThrow("time");
      final int _cursorIndexOfRepeat = _cursor.getColumnIndexOrThrow("repeat");
      final int _cursorIndexOfSetRepeat = _cursor.getColumnIndexOrThrow("setRepeat");
      final int _cursorIndexOfRepeatInterval = _cursor.getColumnIndexOrThrow("repeatInterval");
      final int _cursorIndexOfActivityNotifi = _cursor.getColumnIndexOrThrow("activityNotifi");
      final int _cursorIndexOfCheckRepeat = _cursor.getColumnIndexOrThrow("mRepeat");
      final Note _result;
      if(_cursor.moveToFirst()) {
        _result = new Note();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpTitle;
        _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        _result.setTitle(_tmpTitle);
        final String _tmpContent;
        _tmpContent = _cursor.getString(_cursorIndexOfContent);
        _result.setContent(_tmpContent);
        final String _tmpDate;
        _tmpDate = _cursor.getString(_cursorIndexOfDate);
        _result.setDate(_tmpDate);
        final boolean _tmpFavorite;
        final int _tmp;
        _tmp = _cursor.getInt(_cursorIndexOfFavorite);
        _tmpFavorite = _tmp != 0;
        _result.setFavorite(_tmpFavorite);
        final boolean _tmpFinishTodo;
        final int _tmp_1;
        _tmp_1 = _cursor.getInt(_cursorIndexOfFinishTodo);
        _tmpFinishTodo = _tmp_1 != 0;
        _result.setFinishTodo(_tmpFinishTodo);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _result.setTime(_tmpTime);
        final String _tmpRepeat;
        _tmpRepeat = _cursor.getString(_cursorIndexOfRepeat);
        _result.setRepeat(_tmpRepeat);
        final String _tmpSetRepeat;
        _tmpSetRepeat = _cursor.getString(_cursorIndexOfSetRepeat);
        _result.setRepeat(_tmpSetRepeat);
        final String _tmpRepeatInterval;
        _tmpRepeatInterval = _cursor.getString(_cursorIndexOfRepeatInterval);
        _result.setRepeatInterval(_tmpRepeatInterval);
        final String _tmpActivityNotifi;
        _tmpActivityNotifi = _cursor.getString(_cursorIndexOfActivityNotifi);
        _result.setActivityNotifi(_tmpActivityNotifi);
        final boolean _tmpCheckRepeat;
        final int _tmp_2;
        _tmp_2 = _cursor.getInt(_cursorIndexOfCheckRepeat);
        _tmpCheckRepeat = _tmp_2 != 0;
        _result.setCheckRepeat(_tmpCheckRepeat);
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
