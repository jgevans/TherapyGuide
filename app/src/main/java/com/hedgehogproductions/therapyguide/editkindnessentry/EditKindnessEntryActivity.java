package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hedgehogproductions.therapyguide.R;

import java.util.Date;

public class EditKindnessEntryActivity extends AppCompatActivity {

    public static final String SELECTED_ENTRY_DATE = "ENTRY_DATE";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editkindnessentry);

        final Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        toolbar.setBackgroundColor(getResources().getColor(R.color.colourKindness));
        toolbar.setTitleTextColor(getResources().getColor(R.color.colourKindnessAccent));
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.colourKindnessDark));
        setTheme(R.style.AppThemeKindness);
        if( Build.VERSION.SDK_INT >= 21 ) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colourKindnessDark));
        }

        Date entryDate = (Date) getIntent().getSerializableExtra(SELECTED_ENTRY_DATE);

        if (null == savedInstanceState) {
            initFragment(EditKindnessEntryFragment.newInstance(entryDate));
        }

    }

    private void initFragment(Fragment detailFragment) {
        // Add the EditKindnessEntryFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, detailFragment);
        transaction.commit();
    }
}
