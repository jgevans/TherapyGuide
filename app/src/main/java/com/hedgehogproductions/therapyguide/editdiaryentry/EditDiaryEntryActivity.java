package com.hedgehogproductions.therapyguide.editdiaryentry;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hedgehogproductions.therapyguide.R;

public class EditDiaryEntryActivity extends AppCompatActivity {

    public static final String SELECTED_ENTRY_TIMESTAMP = "ENTRY_TIMESTAMP";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdiaryentry);

        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.colourPositivity));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colourPositivityAccent));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colourPositivityDark));
        setTheme(R.style.AppThemePositivity);
        if( Build.VERSION.SDK_INT >= 21 ) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colourPositivityDark));
        }

        long entryTimestamp = getIntent().getLongExtra(SELECTED_ENTRY_TIMESTAMP, ~0);

        if (null == savedInstanceState) {
            initFragment(EditDiaryEntryFragment.newInstance(entryTimestamp));
        }

    }

    private void initFragment(Fragment detailFragment) {
        // Add the EditDiaryEntryFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, detailFragment);
        transaction.commit();
    }
}
