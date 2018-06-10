package com.hedgehogproductions.therapyguide.kindness;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.intro.IntroActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the kindness screen.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class KindnessScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setSharedPrefs() {
        SharedPreferences prefs = getInstrumentation().getTargetContext()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(IntroActivity.SHOW_INTRO_PREF, false);
        editor.apply();
    }


    @Test
    public void openKindnessTabOnNewDay_showsSelector() {
        // Click on the diary tab
        onView(withText(R.string.kindness_tab_name)).perform(click());

        // Check that the kindness selector screen is displayed
        //onView(withId(R.id.kindness_selector_instruction1)).check(matches(isDisplayed()));

    }

    @Test
    public void markKindnessInstructionsAsDoNotShow_doesNotShowInstructions() {

    }

    @Test
    public void openKindnessTabOnOldDay_doesNotShowSelector() {

    }

    @Test
    public void selectKindnessOptions_createsKindnessEntry() {

    }

    @Test
    public void completeKindnessEntry_marksDayComplete() {

    }


}
