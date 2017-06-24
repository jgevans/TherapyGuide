package com.hedgehogproductions.therapyguide.diarydata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class DiaryReaderDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "DiaryReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " (" +
                    DiaryReaderContract.DiaryDbEntry._ID + " INTEGER PRIMARY KEY," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2 + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3 + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4 + " TEXT," +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5 + " TEXT)";

    private static final String SQL_ADD_VERSION2_COLUMN_TEXT2 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " ADD " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2 + " TEXT;";
    private static final String SQL_ADD_VERSION2_COLUMN_TEXT3 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " ADD " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3 + " TEXT;";
    private static final String SQL_ADD_VERSION2_COLUMN_TEXT4 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " ADD " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4 + " TEXT;";
    private static final String SQL_ADD_VERSION2_COLUMN_TEXT5 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " ADD " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5 + " TEXT;";

    private static final String SQL_REMOVE_VERSION2_COLUMN_TEXT2 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " DROP COLUMN " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2 + ";";
    private static final String SQL_REMOVE_VERSION2_COLUMN_TEXT3 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " DROP COLUMN " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3 + ";";
    private static final String SQL_REMOVE_VERSION2_COLUMN_TEXT4 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " DROP COLUMN " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4 + ";";
    private static final String SQL_REMOVE_VERSION2_COLUMN_TEXT5 =
            "ALTER TABLE " + DiaryReaderContract.DiaryDbEntry.TABLE_NAME + " DROP COLUMN " +
                    DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5 + ";";

    public DiaryReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively upgrade each new version
        if( oldVersion < 2 ){
            db.execSQL(SQL_ADD_VERSION2_COLUMN_TEXT2);
            db.execSQL(SQL_ADD_VERSION2_COLUMN_TEXT3);
            db.execSQL(SQL_ADD_VERSION2_COLUMN_TEXT4);
            db.execSQL(SQL_ADD_VERSION2_COLUMN_TEXT5);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively downgrade each version
        if( newVersion < 2 && oldVersion >=2 ){
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_TEXT5);
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_TEXT4);
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_TEXT3);
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_TEXT2);
        }
    }
}
