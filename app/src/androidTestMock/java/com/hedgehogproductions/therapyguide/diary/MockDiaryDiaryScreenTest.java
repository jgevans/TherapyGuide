package com.hedgehogproductions.therapyguide.diary;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anyOf;


/**
 * Tests for the diary screen, the screen which contains a list of all
 *  diary entries.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MockDiaryDiaryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void diaryEntries_DisplayedInUi() throws Exception {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Check that the Fake diary entry is displayed in the UI
        onView(withId(R.id.diary_card_view)).check(matches(isDisplayed()));

        // Check the values are correct
        onView(withId(R.id.diary_time)).check(matches(anyOf(withText("0 mins ago"),withText("0 min ago"))));
        onView(withId(R.id.diary_text)).check(matches(withText("Entry One")));
    }

}
