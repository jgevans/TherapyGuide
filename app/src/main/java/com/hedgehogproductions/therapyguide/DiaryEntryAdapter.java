package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DiaryEntryAdapter extends RecyclerView.Adapter<DiaryEntryAdapter.ViewHolder> {

    private final Context context;
    private final List<DiaryEntry> diaryEntries;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView text;
        public final TextView time;


        public ViewHolder(View view) {
            super(view);

            text = (TextView) view.findViewById(R.id.diary_text);
            time = (TextView) view.findViewById(R.id.diary_time);

        }
    }

    public DiaryEntryAdapter(Context context){
        this.context = context;
        diaryEntries = new ArrayList<>();

        reloadDiary();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.diary_card, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DiaryEntry entry = diaryEntries.get(position);
        holder.text.setText(entry.getText());

        // Convert timestamp to useful string description based on age
        long now = System.currentTimeMillis();
        CharSequence ago = DateUtils.getRelativeTimeSpanString(
                entry.getCreationTimestamp(),
                now,
                DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL);

        holder.time.setText(ago);
    }

    @Override
    public int getItemCount() {
        return diaryEntries.size();
    }

    void reloadDiary() {

        diaryEntries.clear();
        DiaryReaderDbHelper mDbHelper = new DiaryReaderDbHelper(context);
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
