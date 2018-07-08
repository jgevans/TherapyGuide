package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.hedgehogproductions.therapyguide.R;

import java.util.Date;

public class EditKindnessEntryActivity extends AppCompatActivity {

    public static final String SELECTED_ENTRY_TIMESTAMP = "ENTRY_TIMESTAMP";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editkindnessentry);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Date entryDate = (Date) getIntent().getSerializableExtra(SELECTED_ENTRY_TIMESTAMP);

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
