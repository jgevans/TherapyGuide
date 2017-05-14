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
                DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT
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
            String text = cursor.getString(
                    cursor.getColumnIndexOrThrow(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT));
            diaryEntries.add(new DiaryEntry(timestamp, text));
        }
        cursor.close();

        callback.onLoaded(diaryEntries);
    }

    @Override
    public void saveDiaryEntry(DiaryEntry entry) {

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP, entry.getCreationTimestamp());
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT, entry.getText());

        // Insert the new row, returning the primary key value of the new row
        db.insert(DiaryReaderContract.DiaryDbEntry.TABLE_NAME, null, values);
    }
}