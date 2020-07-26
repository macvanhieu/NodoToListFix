package com.dell.noteapp.entity;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Blob;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
// Khởi tạo trường dữ liệu cho đối tượng
@Entity
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "content")
    private String content;
    @ColumnInfo(name = "date")
    private String date;
    @ColumnInfo(name = "favorite")
    private boolean favorite;
    @ColumnInfo(name = "finishTodo")
    private boolean finishTodo;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "repeat")
    private String repeat;
    @ColumnInfo(name = "setRepeat")
    private String setRepeat;
    @ColumnInfo(name = "repeatInterval")
    private String repeatInterval;
    @ColumnInfo(name = "activityNotifi")
    private String activityNotifi;
    @ColumnInfo(name = "mRepeat")
    private boolean checkRepeat;

    public Note(String title, String content, String date, boolean favorite) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.favorite = favorite;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isFavorite() {
        return favorite;
    }
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    public void setFinishTodo(boolean finishTodo) {
        this.finishTodo = finishTodo;
    }

    public boolean isFinishTodo() {
        return finishTodo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getSetRepeat() {
        return setRepeat;
    }



    public String getRepeatInterval() {
        return repeatInterval;
    }

    public void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public String getActivityNotifi() {
        return activityNotifi;
    }

    public void setActivityNotifi(String activityNotifi) {
        this.activityNotifi = activityNotifi;
    }

    public boolean isCheckRepeat() {
        return checkRepeat;
    }

    public void setCheckRepeat(boolean checkRepeat) {
        this.checkRepeat = checkRepeat;
    }

    public Note() {
    }
}
