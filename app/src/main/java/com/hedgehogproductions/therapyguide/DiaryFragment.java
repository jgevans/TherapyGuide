package com.hedgehogproductions.therapyguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class DiaryFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diary_tab, container, false);

        // Set up create button
        FloatingActionButton createButton = (FloatingActionButton) view.findViewById(R.id.create_button);
        createButton.setOnClickListener(this);

        // Update diary entries view
        updateDiaryView(view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update diary entries view
        updateDiaryView(this.getView());
    }

    @Override
    public void onClick(View view) {
        // Switch to diary entry activity
        Intent intent = new Intent(getContext(), DiaryEntryActivity.class);
        startActivity(intent);
    }

    private void updateDiaryView(View view) {

        String text = "";
        // Get current entries
        List diaryEntries = new ArrayList<>();
        getAllDiaryEntries(diaryEntries);

        // Display diary entries
        for (DiaryEntry entry : (Iterable<DiaryEntry>) diaryEntries) {
            text += entry.getTimestamp();
            text += ": ";
            text += entry.getText();
            text += "\n";
        }

        TextView diaryView = (TextView) view.findViewById(R.id.diary_view);
        diaryView.setText(text);
    }

    private void getAllDiaryEntries(List store) {
        DiaryReaderDbHelper mDbHelper = new DiaryReaderDbHelper(getContext());
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
            store.add(new DiaryEntry(timestamp, text));
        }
        cursor.close();

    }
}
