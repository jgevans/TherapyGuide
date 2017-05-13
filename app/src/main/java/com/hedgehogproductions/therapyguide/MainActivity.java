package com.hedgehogproductions.therapyguide;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

import com.hedgehogproductions.therapyguide.diary.DiaryFragment;

public class MainActivity extends AppCompatActivity {

    private final String SAVED_TAB_KEY = "SavedTab";
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // Create the toolbar

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter and add tabs

        TabPagerAdapter mTabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        mTabPagerAdapter.addFragment(new ListenFragment(), getString(R.string.listen_tab_name));
        mTabPagerAdapter.addFragment(DiaryFragment.newInstance(), getString(R.string.diary_tab_name));

        // Create the pager

        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTabPagerAdapter);

        // Create tab layout

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // If saved state exists, restore it

        if(savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_TAB_KEY));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save current tab when activity is likely to be recreated
        outState.putInt(SAVED_TAB_KEY, mViewPager.getCurrentItem());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Set last used tab when activity is reloading state
        mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_TAB_KEY));
    }

}
