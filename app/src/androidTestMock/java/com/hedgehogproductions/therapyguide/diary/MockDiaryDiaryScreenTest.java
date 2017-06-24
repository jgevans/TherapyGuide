package com.hedgehogproductions.therapyguide.diary;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.addNewDiaryEntry;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.deleteDiaryEntry;
import static org.hamcrest.Matchers.anyOf;


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
        String newDiaryText1 = "I'm checking this entry is displayed";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Add a diary entry
        addNewDiaryEntry(newDiaryText1, newDiaryText2, newDiaryText3, newDiaryText4, newDiaryText5);

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText1))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));

        // Check that the Fake diary entry is displayed in the UI
        onView(withId(R.id.diary_card_view)).check(matches(isDisplayed()));

        // Check the values are correct
        onView(withId(R.id.diary_time)).check(matches(anyOf(withText("0 mins ago"),withText("0 min ago"))));
        onView(withId(R.id.diary_text1)).check(matches(withText(newDiaryText1)));
        onView(withId(R.id.diary_text2)).check(matches(withText(newDiaryText2)));
        onView(withId(R.id.diary_text3)).check(matches(withText(newDiaryText3)));
        onView(withId(R.id.diary_text4)).check(matches(withText(newDiaryText4)));
        onView(withId(R.id.diary_text5)).check(matches(withText(newDiaryText5)));

        // Delete the entry to clear up
        deleteDiaryEntry(newDiaryText1);
    }
}
