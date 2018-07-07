package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class KindnessReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "KindnessReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " (" +
                    KindnessReaderContract.KindnessDbEntry._ID + " INTEGER PRIMARY KEY," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_TIMESTAMP + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_THOUGHTS + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_ACTIONS + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_SELF + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE + " INTEGER)";

    KindnessReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively upgrade each new version
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively downgrade each version
    }

}
