package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.ProgressiveViewPager;

public class EditKindnessEntryActivity extends AppCompatActivity {

    public static final String SELECTED_ENTRY_TIMESTAMP = "ENTRY_TIMESTAMP";
    public static final String EDIT_MODE = "EDIT_MODE";
    public static final String FROM_MAIN_ACTIVITY = "FROM_MAIN_ACTIVITY";

    private ProgressiveViewPager mViewPager;
    private EditKindnessViewPagerAdapter mViewPagerAdapter;
    private Button mNextButton, mBackButton, mCancelButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_editkindnessentry);

        mViewPager = findViewById(R.id.editkindnessentry_pager);
        mViewPagerAdapter = new EditKindnessViewPagerAdapter(this);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Set up buttons
        mNextButton = findViewById(R.id.editkindnessentry_next_button);
        mBackButton = findViewById(R.id.editkindnessentry_back_button);
        mCancelButton = findViewById(R.id.editkindnessentry_cancel_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem = mViewPager.getCurrentItem() + 1;
                if( nextItem < mViewPagerAdapter.getCount() ) {
                    mViewPager.setCurrentItem(nextItem);
                }
                else {
                    // Close edit activity and save
                    showKindnessView();
                }
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
        mBackButton.setVisibility(View.INVISIBLE);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close edit activity
                showKindnessView();
            }
        });

        // Add views in order of appearance
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if( getSupportActionBar() != null ) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        long entryTimestamp = getIntent().getLongExtra(SELECTED_ENTRY_TIMESTAMP, ~0);
    }

    public void showKindnessView() {
        setResult(Activity.RESULT_OK);
        if(getIntent().getBooleanExtra(FROM_MAIN_ACTIVITY, false)) {
            finish();
        }
        else {
            // Navigate up to MainActivity with a new intent to view the kindness tab
            Intent intent = NavUtils.getParentActivityIntent(this);
            intent.putExtra(MainActivity.REQUESTED_TAB_NAME, getResources().getString(R.string.kindness_tab_name));
            NavUtils.navigateUpTo(this, intent);
        }
    }

    private final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            // Set button text and visibility
            if(position == mViewPagerAdapter.getCount() - 1) {
                mNextButton.setText(R.string.kindness_done_button_text);
            }
            else
            {
                mNextButton.setText(R.string.kindness_next_button_text);
            }
            if(position == 0) {
                mBackButton.setVisibility(View.INVISIBLE);
            }
            else {
                mBackButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
