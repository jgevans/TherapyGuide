package com.hedgehogproductions.therapyguide;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;

public class MainActivity extends AppCompatActivity {

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
        mTabPagerAdapter.addFragment(new DiaryFragment(), getString(R.string.diary_tab_name));

        // Create the pager

        ViewPager mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mTabPagerAdapter);

        // Create tab layout

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
