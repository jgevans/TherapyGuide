package com.hedgehogproductions.therapyguide.intro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hedgehogproductions.therapyguide.R;

public class IntroActivity extends AppCompatActivity {

    public static final String PREFERENCES = "TherapyGuideSettings";
    public static final String SHOW_INTRO_PREF = "showIntro";

    private static final String TAG = "IntroActivity";
    private ViewPager mViewPager;
    private IntroViewPagerAdapter mViewPagerAdapter;
    private Button mNextButton, mSkipButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mViewPager = (ViewPager) findViewById(R.id.intro_pager);
        mViewPagerAdapter = new IntroViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Set up buttons
        mNextButton = (Button) findViewById(R.id.intro_next_button);
        mSkipButton = (Button) findViewById(R.id.intro_skip_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem = mViewPager.getCurrentItem() + 1;
                Log.d( TAG, "Next button clicked on page " + String.valueOf(nextItem) + " of " + mViewPagerAdapter.getCount() );
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
    }

    private void finishIntroduction() {
        SharedPreferences settings = getSharedPreferences(PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(SHOW_INTRO_PREF, false);
        editor.apply();

        finish();
    }

    private final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // Set button text and visibility
            if(position == mViewPagerAdapter.getCount() - 1) {
                mNextButton.setText(R.string.intro_done_button_text);
                mSkipButton.setVisibility(View.GONE);
            }
            else
            {
                mNextButton.setText(R.string.intro_next_button_text);
                mSkipButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}