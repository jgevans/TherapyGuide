package com.hedgehogproductions.therapyguide.intro;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainScreenTest {

    private Intent mIntent;
    private SharedPreferences.Editor mPreferencesEditor;

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(
                    MainActivity.class,
                    true,
                    false); // Don't start activity immediately

    @SuppressLint("CommitPrefEdits")
    @Before
    public void setUp() {
        mIntent = new Intent();
        Context context = getInstrumentation().getTargetContext();

        // create a SharedPreferences editor
        mPreferencesEditor = PreferenceManager.getDefaultSharedPreferences(context).edit();
    }

    @Test
    public void firstTime_showsIntro() {

        // Set SharedPreferences data
        mPreferencesEditor.putBoolean("showIntro", true);
        mPreferencesEditor.commit();

        // Launch activity
        mActivityTestRule.launchActivity(mIntent);

        // Verify Intro screen is shown
        onView(withId(R.id.intro_image)).check(matches(isDisplayed()));
    }

    @Test
    public void secondTime_doesNotShowIntro() {

        // Set SharedPreferences data
        mPreferencesEditor.putBoolean("showIntro", false);
        mPreferencesEditor.commit();

        mActivityTestRule.launchActivity(mIntent);

        //Verify intro screen is not shown
        onView(withId(R.id.tabs)).check(matches(isDisplayed()));
    }
}
