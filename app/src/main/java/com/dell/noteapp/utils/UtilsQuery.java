package com.dell.noteapp.utils;

import android.content.Context;

import com.dell.noteapp.entity.Note;

public abstract class UtilsQuery  {
    private Context context;

    public UtilsQuery(Context context){
        this.context=context;
    }

    public abstract void deleteNote(final Note note, final String m);
    public abstract void save(final Note note, final String title,
                     final String content,
                     final String txtdate, final String txtTime,
                     final String repeat, final String repeatInterval,
                     final boolean isRepeat, final String option);

    public abstract void updateFV(final Note note);

    public abstract void finishTodo(final Note note);
}
