package com.hedgehogproductions.therapyguide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.hedgehogproductions.therapyguide.about.AboutActivity;
import com.hedgehogproductions.therapyguide.diary.DiaryFragment;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;
import com.hedgehogproductions.therapyguide.kindness.KindnessFragment;
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

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setPopupTheme(R.style.ToolbarThemeBase);

        // Create the adapter and add tabs

        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager());
        tabPagerAdapter.addFragment(ListenFragment.newInstance(), getString(R.string.listen_tab_name));
        tabPagerAdapter.addFragment(DiaryFragment.newInstance(), getString(R.string.diary_tab_name));
        tabPagerAdapter.addFragment(KindnessFragment.newInstance(), getString(R.string.kindness_tab_name));

        // Create the pager

        mViewPager = findViewById(R.id.pager);
        mViewPager.setAdapter(tabPagerAdapter);

        // Create tab layout

        final TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        // Set colours etc. for tabs
        LinearLayout tabs = (LinearLayout) tabLayout.getChildAt(0);
        LinearLayout sleepTab = (LinearLayout) tabs.getChildAt(0);
        LinearLayout positivityTab = (LinearLayout) tabs.getChildAt(1);
        LinearLayout kindnessTab = (LinearLayout) tabs.getChildAt(2);

        LinearLayout tabView = (LinearLayout) sleepTab.getChildAt(0).getParent();
        tabView.setBackgroundColor(getResources().getColor(R.color.colourSleep));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
        layoutParams.weight = 1;
        tabView.setLayoutParams(layoutParams);

        tabView = (LinearLayout) positivityTab.getChildAt(0).getParent();
        tabView.setBackgroundColor(getResources().getColor(R.color.colourPositivity));
        layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
        layoutParams.weight = 1;
        tabView.setLayoutParams(layoutParams);

        tabView = (LinearLayout) kindnessTab.getChildAt(0).getParent();
        tabView.setBackgroundColor(getResources().getColor(R.color.colourKindness));
        layoutParams = (LinearLayout.LayoutParams) tabView.getLayoutParams();
        layoutParams.weight = 1;
        tabView.setLayoutParams(layoutParams);

        // Set up colour changes

        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        setTheme(R.style.AppThemeSleep);
                        changeColours(toolbar, tabLayout,
                                R.color.colourSleep, R.color.colourSleepAccent,
                                R.color.colourSleepDark);
                        break;
                    case 1:
                        setTheme(R.style.AppThemePositivity);
                        changeColours(toolbar, tabLayout,
                                R.color.colourPositivity, R.color.colourPositivityAccent,
                                R.color.colourPositivityDark);
                        break;
                    case 2:
                        setTheme(R.style.AppThemeKindness);
                        changeColours(toolbar, tabLayout,
                                R.color.colourKindness, R.color.colourKindnessAccent,
                                R.color.colourKindnessDark);
                        break;
                    default:
                        setTheme(R.style.AppThemeBase);
                        changeColours(toolbar, tabLayout,
                                R.color.colorPrimary, R.color.colorPrimaryAccent,
                                R.color.colorPrimaryDark);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) { }
        };
        if( Build.VERSION.SDK_INT >= 24 ) {
            mViewPager.addOnPageChangeListener(onPageChangeListener);
        }
        else {
            //noinspection deprecation
            mViewPager.setOnPageChangeListener(onPageChangeListener);
        }

        // If saved state exists, restore it

        if(savedInstanceState != null) {
            mViewPager.setCurrentItem(savedInstanceState.getInt(SAVED_TAB_KEY));
        }
        if(getIntent().hasExtra(REQUESTED_TAB_NAME)) {
            mViewPager.setCurrentItem(tabPagerAdapter.getPosition(
                    getIntent().getExtras().getString(REQUESTED_TAB_NAME)));
        }
        switch (mViewPager.getCurrentItem()) {
            case 0:
                setTheme(R.style.AppThemeSleep);
                changeColours(toolbar, tabLayout,
                        R.color.colourSleep, R.color.colourSleepAccent,
                        R.color.colourSleepDark);
                break;
            case 1:
                setTheme(R.style.AppThemePositivity);
                changeColours(toolbar, tabLayout,
                        R.color.colourPositivity, R.color.colourPositivityAccent,
                        R.color.colourPositivityDark);
                break;
            case 2:
                setTheme(R.style.AppThemeKindness);
                changeColours(toolbar, tabLayout,
                        R.color.colourKindness, R.color.colourKindnessAccent,
                        R.color.colourKindnessDark);
                break;
            default:
                setTheme(R.style.AppThemeBase);
                changeColours(toolbar, tabLayout,
                        R.color.colorPrimary, R.color.colorPrimaryAccent,
                        R.color.colorPrimaryDark);
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
        changeMenuColours(menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        changeMenuColours(menu);
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
            case R.id.show_about:
                intent = new Intent(getApplicationContext(), AboutActivity.class);
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

    private void changeColours(Toolbar toolbar, TabLayout tabLayout, int colour, int colourAccent,
                               int colourDark) {
        toolbar.setBackgroundColor(getResources().getColor(colour));
        toolbar.setTitleTextColor(getResources().getColor(colourAccent));
        toolbar.setSubtitleTextColor(getResources().getColor(colourDark));
        tabLayout.setBackgroundColor(getResources().getColor(colour));
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(colourAccent));
        if( Build.VERSION.SDK_INT >= 21 ) {
            getWindow().setStatusBarColor(getResources().getColor(colourDark));
        }
        invalidateOptionsMenu();
    }

    private void changeMenuColours(Menu menu) {
        int colour;
        switch (mViewPager.getCurrentItem()) {
            case 0:
                colour = R.color.colourSleepDark;
                break;
            case 1:
                colour = R.color.colourPositivityDark;
                break;
            case 2:
                colour = R.color.colourKindnessDark;
                break;
            default:
                colour = R.color.colorPrimaryDark;
        }

        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                if(Build.VERSION.SDK_INT >= 23) {
                    drawable.setColorFilter(getResources().getColor(colour, this.getTheme()), PorterDuff.Mode.SRC_ATOP);
                }
                else {
                    //noinspection deprecation
                    drawable.setColorFilter(getResources().getColor(colour), PorterDuff.Mode.SRC_ATOP);
                }
            }
        }
    }

}
