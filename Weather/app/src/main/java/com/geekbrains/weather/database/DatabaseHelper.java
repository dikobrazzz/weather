package com.geekbrains.weather.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "notess.db";
    public static final int DATABASE_VERSION = 2;
    public static final String TABLE_NOTES = "notes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_NOTE_TITLE = "title";
    public static final String COLUMN_NOTE_DATE = "date";
    public static final String COLUMN_NOTE_EVENT = "event";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NOTES + " (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_NOTE + " TEXT," +
                COLUMN_NOTE_TITLE + " TEXT, " + COLUMN_NOTE_DATE + " TEXT, " + COLUMN_NOTE_EVENT + " TEXT);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        try{
        if ((oldVersion == 1) && newVersion == 2){
            String upgradeQuery = "ALTER TABLE " + TABLE_NOTES + " ADD COLUMN" +
                    COLUMN_NOTE_TITLE + " TEXT DEFAULT Title" + COLUMN_NOTE_DATE + " TEXT DEFAULT Date" + COLUMN_NOTE_EVENT + " TEXT DEFAULT Event";
            sqLiteDatabase.execSQL(upgradeQuery);
        }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
