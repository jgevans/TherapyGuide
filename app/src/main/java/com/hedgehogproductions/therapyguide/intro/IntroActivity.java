package com.hedgehogproductions.therapyguide.intro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.settings.SettingsActivity;

public class IntroActivity extends AppCompatActivity {

    public static final String SHOW_INTRO_PREF = "showIntro";

    private ViewPager mViewPager;
    private IntroViewPagerAdapter mViewPagerAdapter;
    private Button mNextButton, mSkipButton;
    private LinearLayout mBottomDotsLayout;
    private int[] mDotColoursActive, mDotColoursInactive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Hide the status bar
        goFullscreen();

        setContentView(R.layout.activity_intro);

        mViewPager = findViewById(R.id.intro_pager);
        mViewPagerAdapter = new IntroViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Set up buttons
        mNextButton = findViewById(R.id.intro_next_button);
        mSkipButton = findViewById(R.id.intro_skip_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem = mViewPager.getCurrentItem() + 1;
                if( nextItem < mViewPagerAdapter.getCount() ) {
                    mViewPager.setCurrentItem(nextItem);
                }
                else {
                    // Close intro
                    finishIntroduction();
                }
            }
        });
        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close intro
                finishIntroduction();
            }
        });

        // Add views in order of appearance
        mViewPagerAdapter.addView(R.layout.intro_intro);
        mViewPagerAdapter.addView(R.layout.intro_sleep);
        mViewPagerAdapter.addView(R.layout.intro_positivity);
        mViewPagerAdapter.addView(R.layout.intro_kindness);

        mBottomDotsLayout = findViewById(R.id.intro_dots);
        mDotColoursActive = getResources().getIntArray(R.array.intro_active_dots);
        mDotColoursInactive = getResources().getIntArray(R.array.intro_inactive_dots);
        // Set up nav dots
        for(int position = 0; position < mViewPagerAdapter.getCount(); ++position) {
            TextView newDot = new TextView(this);
            newDot.setText("\u2022");
            newDot.setTextSize(35);
            newDot.setTextColor(mDotColoursInactive[position]);
            mBottomDotsLayout.addView(newDot);
        }
        colourDots(0);
    }

    @Override
    protected void onResume() {
        super.onResume();

        goFullscreen();
    }

    private void colourDots(int newPosition) {
        for(int position = 0; position < mViewPagerAdapter.getCount(); ++position) {
            TextView dotView = (TextView) mBottomDotsLayout.getChildAt(position);
            if(position == newPosition) {
                dotView.setTextColor(mDotColoursActive[newPosition]);
            }
            else {
                dotView.setTextColor(mDotColoursInactive[newPosition]);
            }
        }
    }

    private void finishIntroduction() {
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SHOW_INTRO_PREF, false);
        editor.apply();

        finish();
    }

    @SuppressLint("InlinedApi")
    private void goFullscreen() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else if (Build.VERSION.SDK_INT < 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener
                    (new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int visibility) {
                            if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                                // The system bars are visible. Hide them again
                                getWindow().getDecorView().setSystemUiVisibility(
                                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN
                                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                            }
                        }
                    });
        }
        else {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
    }

    private final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            colourDots(position);
            // Set button text and visibility
            if(position == mViewPagerAdapter.getCount() - 1) {
                mNextButton.setText(R.string.intro_done_button_text);
                mSkipButton.setVisibility(View.INVISIBLE);
            }
            else
            {
                mNextButton.setText(R.string.intro_next_button_text);
                mSkipButton.setVisibility(View.VISIBLE);
            }
            // Set up settings button if found in view
            Button setReminderButton = findViewById(R.id.intro_set_reminder_button);
            if(null != setReminderButton){
                setReminderButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Start settings activity
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}