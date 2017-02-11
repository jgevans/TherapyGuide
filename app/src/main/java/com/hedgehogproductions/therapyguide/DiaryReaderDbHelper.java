package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class DiaryReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "DiaryReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " (" +
                    DiaryReaderContract.DiaryDbEntry._ID + " INTEGER PRIMARY KEY," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT + " TEXT)";


    public DiaryReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Make no change to database on upgrade
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
