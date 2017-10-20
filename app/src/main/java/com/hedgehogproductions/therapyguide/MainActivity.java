package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hedgehogproductions.therapyguide.diary.DiaryFragment;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;
import com.hedgehogproductions.therapyguide.listen.ListenFragment;
import com.hedgehogproductions.therapyguide.notifications.NotificationHandler;
import com.hedgehogproductions.therapyguide.settings.SettingsActivity;

public class MainActivity extends AppCompatActivity {

    public static final int DIARY_REMINDER_NOTIFICATION_ID = 2;

    private static final String SAVED_TAB_KEY = "SavedTab";
    public static final String REQUESTED_TAB_NAME = "RequestedTabName";
    public static final String PREFERENCES = "TherapyGuideSettings";
    private SmartSwipePager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
        if(sharedPreferences.getBoolean(IntroActivity.SHOW_INTRO_PREF, true)) {
            // Launch the introduction
            startActivity(new Intent(this, IntroActivity.class));
        }

        setContentView(R.layout.activity_main);

        // Create the toolbar

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter and add tabs

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tabPagerAdapter.addFragment(ListenFragment.newInstance(), getString(R.string.listen_tab_name));
        tabPagerAdapter.addFragment(DiaryFragment.newInstance(), getString(R.string.diary_tab_name));

        // Create the pager

        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(tabPagerAdapter);

        // Create tab layout

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // If saved state exists, restore it

        if(savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_TAB_KEY));
        }
        if(getIntent().hasExtra(REQUESTED_TAB_NAME)) {
            mViewPager.setCurrentItem(tabPagerAdapter.getPosition(
                    getIntent().getExtras().getString(REQUESTED_TAB_NAME)));
        }

        // Set up notification channels
        NotificationHandler.createNotificationChannels(this);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if(intent.hasExtra(REQUESTED_TAB_NAME)) {
            TabPagerAdapter pagerAdapter = (TabPagerAdapter) mViewPager.getAdapter();
            mViewPager.setCurrentItem(pagerAdapter.getPosition(
                    intent.getExtras().getString(REQUESTED_TAB_NAME)));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                if(Build.VERSION.SDK_INT >= 23) {
                    drawable.setColorFilter(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    //noinspection deprecation
                    drawable.setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        Intent intent;
        switch (item.getItemId()) {
            case R.id.settings:
                intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.show_intro:
                intent = new Intent(getApplicationContext(), IntroActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        super.onRestoreInstanceState(savedInstanceState);

        // Set last used tab when activity is reloading state
        mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_TAB_KEY));
    }

}
