package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class KindnessReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "KindnessReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " (" +
                    KindnessReaderContract.KindnessDbEntry._ID + " INTEGER PRIMARY KEY," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_VALUE + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE + " INTEGER)";

    private static final String SQL_ADD_VERSION2_COLUMN_CATEGORY =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY + " TEXT;";

    private static final String SQL_ADD_VERSION2_COLUMN_VALUE =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_VALUE + " TEXT;";

    private static final String SQL_REMOVE_VERSION1_COLUMN_WORDS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS + ";";

    private static final String SQL_REMOVE_VERSION1_COLUMN_THOUGHTS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_THOUGHTS + ";";

    private static final String SQL_REMOVE_VERSION1_COLUMN_ACTIONS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_ACTIONS + ";";

    private static final String SQL_REMOVE_VERSION1_COLUMN_SELF =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_SELF + ";";

    private static final String SQL_REMOVE_VERSION2_COLUMN_CATEGORY =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY + ";";

    private static final String SQL_REMOVE_VERSION2_COLUMN_VALUE =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " DROP COLUMN " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_VALUE + ";";

    private static final String SQL_ADD_VERSION1_COLUMN_WORDS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS + " TEXT;";

    private static final String SQL_ADD_VERSION1_COLUMN_THOUGHTS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_THOUGHTS + " TEXT;";

    private static final String SQL_ADD_VERSION1_COLUMN_ACTIONS =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_ACTIONS + " TEXT;";

    private static final String SQL_ADD_VERSION1_COLUMN_SELF =
            "ALTER TABLE " + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + " ADD " +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_SELF + " TEXT;";


    KindnessReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively upgrade each new version
        if( oldVersion < 2 ){
            db.execSQL(SQL_ADD_VERSION2_COLUMN_CATEGORY);
            db.execSQL(SQL_ADD_VERSION2_COLUMN_VALUE);
            db.execSQL(SQL_REMOVE_VERSION1_COLUMN_WORDS);
            db.execSQL(SQL_REMOVE_VERSION1_COLUMN_THOUGHTS);
            db.execSQL(SQL_REMOVE_VERSION1_COLUMN_ACTIONS);
            db.execSQL(SQL_REMOVE_VERSION1_COLUMN_SELF);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively downgrade each version
        if( newVersion < 2 && oldVersion >=2 ){
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_CATEGORY);
            db.execSQL(SQL_REMOVE_VERSION2_COLUMN_VALUE);
            db.execSQL(SQL_ADD_VERSION1_COLUMN_WORDS);
            db.execSQL(SQL_ADD_VERSION1_COLUMN_THOUGHTS);
            db.execSQL(SQL_ADD_VERSION1_COLUMN_ACTIONS);
            db.execSQL(SQL_ADD_VERSION1_COLUMN_SELF);

        }
    }

}
