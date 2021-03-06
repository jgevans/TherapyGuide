package com.hedgehogproductions.therapyguide.diarydata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DiaryServiceApiImpl implements DiaryServiceApi {

    @NonNull
    private final DiaryReaderDbHelper mDbHelper;

    public DiaryServiceApiImpl(Context context) {
        mDbHelper = new DiaryReaderDbHelper(context);
    }

    @Override
    public void getAllDiaryEntries(final DiaryServiceCallback<List<DiaryEntry>> callback) {
        List<DiaryEntry> diaryEntries = new ArrayList<>();

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // The columns that will be used
        String[] projection = {
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5
        };

        // Sort results, newest first
        String sortOrder =
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                DiaryReaderContract.DiaryDbEntry.TABLE_NAME,// The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        while(cursor.moveToNext()) {
            long timestamp = cursor.getLong(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP));
            String text1 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT));
            String text2 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2));
            String text3 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3));
            String text4 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4));
            String text5 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5));
            diaryEntries.add(new DiaryEntry(timestamp, text1, text2, text3, text4, text5));
        }
        cursor.close();

        callback.onLoaded(diaryEntries);
    }

    @Override
    public void getDiaryEntry(long timestamp, DiaryServiceCallback<DiaryEntry> callback) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // The columns that will be used
        String[] projection = {
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5
        };

        // The value to search for
        ContentValues values = new ContentValues();
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP, String.valueOf(timestamp));

        // Sort results, newest first
        String sortOrder =
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + " DESC";

        Cursor cursor = db.query(
                DiaryReaderContract.DiaryDbEntry.TABLE_NAME,// The table to query
                projection,                                 // The columns to return
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + "='" + timestamp + "'", // The columns for the WHERE clause
                null,                                       // The values for the WHERE clause
                null,                                       // don't group the rows
                null,                                       // don't filter by row groups
                sortOrder                                   // The sort order
        );

        DiaryEntry loadedEntry = null;
        if( cursor.getCount() == 1) {
            cursor.moveToNext();
            String text1 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT));
            String text2 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2));
            String text3 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3));
            String text4 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4));
            String text5 = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5));
            loadedEntry = new DiaryEntry(timestamp, text1, text2, text3, text4, text5);
        }
        cursor.close();
        callback.onLoaded(loadedEntry);
    }

    @Override
    public void saveDiaryEntry(DiaryEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP, entry.getCreationTimestamp());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT, entry.getText1());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2, entry.getText2());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3, entry.getText3());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4, entry.getText4());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5, entry.getText5());

        // Insert the new row, returning the primary key value of the new row
        db.insert(DiaryReaderContract.DiaryDbEntry.TABLE_NAME, null, values);
    }

    @Override
    public void updateDiaryEntry(DiaryEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP, entry.getCreationTimestamp());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT, entry.getText1());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT2, entry.getText2());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT3, entry.getText3());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT4, entry.getText4());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT5, entry.getText5());

        db.update(DiaryReaderContract.DiaryDbEntry.TABLE_NAME,
                values,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + "='" + String.valueOf(entry.getCreationTimestamp()) + "'",
                null);
    }

    @Override
    public void deleteDiaryEntry(DiaryEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(DiaryReaderContract.DiaryDbEntry.TABLE_NAME,
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP + "='" + String.valueOf(entry.getCreationTimestamp()) + "'",
                null);
    }
}
