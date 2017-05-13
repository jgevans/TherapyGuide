package com.hedgehogproductions.therapyguide.adddiaryentry;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.hedgehogproductions.therapyguide.R;


public class AddDiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adddiaryentry);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (null == savedInstanceState) {
            initFragment(AddDiaryEntryFragment.newInstance());
        }

    }

    private void initFragment(Fragment detailFragment) {
        // Add the AddDiaryEntryFragment to the layout
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, detailFragment);
        transaction.commit();
    }
}
