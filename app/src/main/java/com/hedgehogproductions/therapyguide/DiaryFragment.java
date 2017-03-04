package com.hedgehogproductions.therapyguide;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


public class DiaryFragment extends Fragment implements View.OnClickListener {

    private List<DiaryEntry> diaryEntries;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diary_tab, container, false);

        // Set up create button
        FloatingActionButton createButton = (FloatingActionButton) view.findViewById(R.id.create_button);
        createButton.setOnClickListener(this);

        // Create the RecyclerView for Diary cards
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.diary_view);
        diaryEntries = new ArrayList<>();
        DiaryEntryAdapter adapter = new DiaryEntryAdapter(this.getContext(), diaryEntries);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        loadDiary();

        return view;
    }

    @Override
    public void onClick(View view) {
        // Switch to diary entry activity when floating action button is pressed
        Intent intent = new Intent(getContext(), DiaryEntryActivity.class);
        startActivity(intent);
    }

    private void loadDiary() {
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
            diaryEntries.add(new DiaryEntry(timestamp, text));
        }
        cursor.close();

    }
}
