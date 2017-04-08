package com.hedgehogproductions.therapyguide;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;


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
        // Get entry text and create new DiaryEntry
        EditText diaryText = (EditText) findViewById(R.id.diary_entry);
        DiaryEntry entry = new DiaryEntry(System.currentTimeMillis(), diaryText.getText().toString());

        // Save entry into database
        //TODO this needs access to the repository to store new diary entries
        //mDiaryRepository.saveDiaryEntry(entry);

        // Having saved the new entry, finish the activity
        // (which will take the user back to the previous activity)
        finish();
    }
}
