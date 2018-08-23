package com.geekbrains.weather.model.note;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.geekbrains.weather.database.DatabaseHelper;

import java.io.Closeable;

public class NoteDataReader implements Closeable{

    private Cursor cursor;
    private SQLiteDatabase database;
    private String[] notesAllColumn = {
            DatabaseHelper.COLUMN_ID,
            DatabaseHelper.COLUMN_NOTE,
            DatabaseHelper.COLUMN_NOTE_TITLE,
            DatabaseHelper.COLUMN_NOTE_DATE,
            DatabaseHelper.COLUMN_NOTE_EVENT
    };

    public NoteDataReader(SQLiteDatabase database) {
        this.database = database;
    }

    public void open() {
        query();
        cursor.moveToFirst();
    }

    private void query() {
        cursor = database.query(DatabaseHelper.TABLE_NOTES, notesAllColumn, null,null,null,null,null);
    }

    public void close() {
        cursor.close();
    }

    public Note getPosition(int pos){
        cursor.moveToPosition(pos);
        return cursorToNote();
    }

    public int getCount(){
        return cursor.getCount();
    }

    private Note cursorToNote() {
        Note note = new Note();
        note.setId(cursor.getLong(0));
        note.setDescription(cursor.getString(1));
        note.setTitle(cursor.getString(2));
        note.setDate(cursor.getString(3));
        note.setEvent(cursor.getString(4));
        return note;
    }

    public void refresh() {
        int pos = cursor.getPosition();
        query();
        cursor.moveToPosition(pos);
    }
}
