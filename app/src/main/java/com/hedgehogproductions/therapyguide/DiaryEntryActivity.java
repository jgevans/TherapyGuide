package com.hedgehogproductions.therapyguide;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class DiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // When user selects Up, finish the activity
                // (which will take the user back to the previous activity)
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveDiaryEntry(View view) {
        // Get the data to be stored
        long timestamp = System.currentTimeMillis();
        EditText diaryText = (EditText) findViewById(R.id.diary_entry);
        String text = diaryText.getText().toString();


        DiaryReaderDbHelper mDbHelper = new DiaryReaderDbHelper(this);

        // Get the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TIMESTAMP, timestamp);
        values.put(DiaryReaderContract.DiaryDbEntry.COLUMN_NAME_TEXT, text);

        // Insert the new row, returning the primary key value of the new row
        db.insert(DiaryReaderContract.DiaryDbEntry.TABLE_NAME, null, values);

        // Having saved the new entry, finish the activity
        // (which will take the user back to the previous activity)
        finish();
    }
}
