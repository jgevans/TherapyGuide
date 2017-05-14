package com.hedgehogproductions.therapyguide.diary;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Tests for the diary screen, the screen which contains a list of all
 *  diary entries.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiaryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);



    @Test
    public void clickAddDiaryEntryButton_opensAddDiaryEntryUi() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Check if the add diary entry screen is displayed
        onView(withId(R.id.adddiaryentry_entry_text)).check(matches(isDisplayed()));
    }

    @Test
    public void addEntryToDiary() throws Exception {
        String newDiaryText = "I executed an Espresso test";

        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Add diary entry text and close the keyboard
        onView(withId(R.id.adddiaryentry_entry_text)).perform(typeText(newDiaryText),
                closeSoftKeyboard());

        // Save the entry
        onView(withId(R.id.adddiaryentry_save_button)).perform(click());

        // Scroll diary to added entry, by finding its text
        onView(withId(R.id.diary_view)).perform(
                scrollTo(hasDescendant(withText(newDiaryText))));

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText)).check(matches(isDisplayed()));
    }

    @Test
    public void goBackFromAddDiaryEntryScreen_EndsAtDiary() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Click back
        onView(withContentDescription("Navigate up")).perform(click());

        // Verify Diary is displayed
        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
    }

    @Test
    public void largeDiary_LoadsFully() {
        // Click on the diary tab
        onView(withText(R.string.diary_tab_name)).perform(click());

        for(int entryNumber = 1; entryNumber <= 15; ++entryNumber) {
            addNewDiaryEntry("New Diary Entry " + String.valueOf(entryNumber) );
        }

        for(int entryNumber = 1; entryNumber <= 15; ++entryNumber) {
            // Scroll diary to added entry, by finding its text
            onView(withId(R.id.diary_view)).perform(
                    scrollTo(hasDescendant(withText(
                            "New Diary Entry " + String.valueOf(entryNumber)))));

            // Verify entry is displayed on screen
            onView(Matchers.withItemText("New Diary Entry " + String.valueOf(entryNumber)))
                    .check(matches(isDisplayed()));
        }

        //TODO Remove all new items (requires entry deletion functionality)
    }


    /**
     * Convenience method that adds an entry into the diary
     */
    private void addNewDiaryEntry(String entryText) {
        // Click on the add diary entry button
        onView(withId(R.id.create_button)).perform(click());

        // Add diary entry text and close the keyboard
        onView(withId(R.id.adddiaryentry_entry_text)).perform(typeText(entryText),
                closeSoftKeyboard());

        // Save the entry
        onView(withId(R.id.adddiaryentry_save_button)).perform(click());
    }

}
