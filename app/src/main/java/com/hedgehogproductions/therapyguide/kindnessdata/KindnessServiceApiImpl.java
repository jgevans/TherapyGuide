package com.hedgehogproductions.therapyguide.kindnessdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KindnessServiceApiImpl implements KindnessServiceApi {

    @NonNull
    private final KindnessReaderDbHelper mDbHelper;

    public KindnessServiceApiImpl(Context context) {
        mDbHelper = new KindnessReaderDbHelper(context);
    }

    @Override
    public void getAllKindnessEntries(KindnessServiceCallback<List<KindnessEntry>> callback) {
        List<KindnessEntry> kindnessEntries = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // The columns that will be used
        String[] projection = {
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE
        };

        // Sort results, newest first
        String sortOrder =
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                KindnessReaderContract.KindnessDbEntry.TABLE_NAME,// The table to query
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
            KindnessCategories kindnessCategory = KindnessCategories.valueOf(cursor.getString(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY)));
            String kindnessValue = cursor.getString(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS));
            boolean complete = cursor.getInt(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE)) == 1;
            kindnessEntries.add(new KindnessEntry(new Date(date), kindnessCategory, kindnessValue, complete));
        }
        cursor.close();

        callback.onLoaded(kindnessEntries);
    }

    @Override
    public void getKindnessEntry(Date date, KindnessServiceCallback<KindnessEntry> callback) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // The columns that will be used
        String[] projection = {
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE
        };

        // Sort results, newest first
        String sortOrder =
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + " DESC";

        Cursor cursor = db.query(
                KindnessReaderContract.KindnessDbEntry.TABLE_NAME,// The table to query
                projection,                                 // The columns to return
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + "='" + date.getTime() + "'", // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        KindnessEntry loadedEntry = null;
        if( cursor.getCount() == 1) {
            cursor.moveToNext();
            KindnessCategories kindnessCategory = KindnessCategories.valueOf(cursor.getString(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY)));
            String kindnessValue = cursor.getString(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS));
            boolean complete = cursor.getInt(
                    cursor.getColumnIndexOrThrow(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE)) == 1;
            loadedEntry = new KindnessEntry(date, kindnessCategory, kindnessValue, complete);
        }
        cursor.close();

        callback.onLoaded(loadedEntry);
    }

    @Override
    public void saveKindnessEntry(KindnessEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE, entry.getCreationDate().getTime());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY, entry.getCategory().name());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS, entry.getValue());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE, entry.isComplete());

        // Insert the new row, returning the primary key value of the new row
        db.insert(KindnessReaderContract.KindnessDbEntry.TABLE_NAME, null, values);
    }

    @Override
    public void updateKindnessEntry(KindnessEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE, entry.getCreationDate().getTime());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_CATEGORY, entry.getCategory().name());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_KINDNESS_WORDS, entry.getValue());
        values.put(KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_COMPLETE, entry.isComplete());

        db.update(KindnessReaderContract.KindnessDbEntry.TABLE_NAME,
                values,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + "='" + entry.getCreationDate().getTime() + "'",
                null);
    }

    @Override
    public void deleteKindnessEntry(KindnessEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(KindnessReaderContract.KindnessDbEntry.TABLE_NAME,
                KindnessReaderContract.KindnessDbEntry.COLUMN_NAME_DATE + "='" + entry.getCreationDate().getTime() + "'",
                null);
    }

}
