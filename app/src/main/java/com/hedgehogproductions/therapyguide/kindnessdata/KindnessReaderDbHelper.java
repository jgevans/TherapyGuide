package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class KindnessReaderDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "KindnessReader.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE '" + KindnessReaderContract.KindnessDbEntry.TABLE_NAME + "' (" +
                    KindnessReaderContract.KindnessDbEntry._ID + " INTEGER PRIMARY KEY," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_VALUE + " TEXT," +
                    KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE + " INTEGER)";

    private static final String SQL_DELETE_VERSION1_DB =
            "DROP TABLE IF EXISTS '" + KindnessReaderContract.VERSION1_TABLE_NAME + "'";

    private static final String SQL_DELETE_VERSION2_DB =
            "DROP TABLE IF EXISTS '" + KindnessReaderContract.VERSION2_TABLE_NAME + "'";

    KindnessReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively upgrade each new version
        if( oldVersion < 2 ){
            // Copy records out of old database
            List<KindnessEntry> kindnessEntries = getVersion1EntriesAsVersion2();

            // Delete version 1 database and create new one.
            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_DELETE_VERSION1_DB);

            // Add records back in with any necessary adaptations
            saveEntriesVersion2(kindnessEntries);
        }
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Progressively downgrade each version
        if( newVersion < 2 && oldVersion >=2 ){
            // Loose records

            // Delete version 2 database and create new one.
            db.execSQL(SQL_CREATE_ENTRIES);
            db.execSQL(SQL_DELETE_VERSION2_DB);
        }
    }

    private List<KindnessEntry> getVersion1EntriesAsVersion2() {
        List<KindnessEntry> kindnessEntries = new ArrayList<>();

        // The columns that will be used
        String[] projection = {
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE,
                KindnessReaderContract.COLUMN_NAME_KINDNESS_WORDS,
                KindnessReaderContract.COLUMN_NAME_KINDNESS_THOUGHTS,
                KindnessReaderContract.COLUMN_NAME_KINDNESS_ACTIONS,
                KindnessReaderContract.COLUMN_NAME_KINDNESS_SELF,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE
        };

        // Sort results, newest first
        String sortOrder =
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = getReadableDatabase().query(
                KindnessReaderContract.VERSION1_TABLE_NAME,// The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            long date = cursor.getLong(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE));
            String kindnessSelf = cursor.getString(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.COLUMN_NAME_KINDNESS_SELF));
            boolean complete = cursor.getInt(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE)) == 1;
            kindnessEntries.add(new KindnessEntry(new Date(date), KindnessCategories.SELF, kindnessSelf, complete));
        }
        cursor.close();

        return kindnessEntries;

    }

    private void saveEntriesVersion2(List<KindnessEntry> kindnessEntries)
    {
        // Get the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        for (KindnessEntry entry : kindnessEntries) {
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE, entry.getCreationDate().getTime());
            values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY, entry.getCategory().name());
            values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_VALUE, entry.getValue());
            values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE, entry.isComplete());

            // Insert the new row, returning the primary key value of the new row
            db.insert(KindnessReaderContract.KindnessDbEntry.TABLE_NAME, null, values);
        }
    }

}
