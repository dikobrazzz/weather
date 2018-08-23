package com.geekbrains.weather.model.note;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import com.geekbrains.weather.database.DatabaseHelper;

import java.io.Closeable;
import java.io.IOException;

public class NoteDataSourse implements Closeable{

    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private NoteDataReader dataReader;

    public NoteDataSourse(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
        dataReader = new NoteDataReader(database);
        dataReader.open();
    }

    @Override
    public void close() throws IOException {
       dataReader.close();
       databaseHelper.close();
    }

    public Note addNote(String title, String description, String date, String event){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.COLUMN_NOTE, description);
        contentValues.put(DatabaseHelper.COLUMN_NOTE_TITLE, title);
        contentValues.put(DatabaseHelper.COLUMN_NOTE_DATE, date);
        contentValues.put(DatabaseHelper.COLUMN_NOTE_EVENT,event);

        long insertId = database.insert(DatabaseHelper.TABLE_NOTES, null, contentValues);

        Note newNote = new Note();
        newNote.setDescription(description);
        newNote.setId(insertId);
        newNote.setTitle(title);
        newNote.setDate(date);
        newNote.setEvent(event);

        return newNote;
    }

    public void editNote(Note note, String description, String title){
        ContentValues editValues = new ContentValues();
        editValues.put(databaseHelper.COLUMN_ID, note.getId());
        editValues.put(databaseHelper.COLUMN_NOTE, note.getDescription());
        editValues.put(databaseHelper.COLUMN_NOTE_TITLE, note.getTitle());
        editValues.put(databaseHelper.COLUMN_NOTE_DATE, note.getDate());
        editValues.put(databaseHelper.COLUMN_NOTE_EVENT, note.getEvent());

        database.update(databaseHelper.TABLE_NOTES, editValues, databaseHelper.COLUMN_ID + "=" + note.getId(), null);
    }
    public void delete(Note note){
        long id = note.getId();
        database.delete(DatabaseHelper.TABLE_NOTES,DatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    public void deleteAll(){
        database.delete(DatabaseHelper.TABLE_NOTES, null, null);
    }

    public NoteDataReader getDataReader() {
        return dataReader;
    }
}
