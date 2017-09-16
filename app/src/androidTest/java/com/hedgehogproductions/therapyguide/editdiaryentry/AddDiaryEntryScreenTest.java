package com.hedgehogproductions.therapyguide.editdiaryentry;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.Matchers;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.hedgehogproductions.therapyguide.DiaryManipulators.deleteDiaryEntry;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.FROM_MAIN_ACTIVITY;
import static org.hamcrest.Matchers.not;


/**
 * Tests for the edit diary entry screen, in add mode, the screen which allows the creation
 *  of a new diary entry.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class AddDiaryEntryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<EditDiaryEntryActivity> mEntryActivityTestRule =
            new ActivityTestRule<EditDiaryEntryActivity>(EditDiaryEntryActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Intent editIntent = new Intent();
                    editIntent.putExtra(EDIT_MODE, false);
                    editIntent.putExtra(FROM_MAIN_ACTIVITY, false);
                    return editIntent;
                }
            };

    @Test
    public void showNoDeleteOptionForAddMode() {
        // Verify the correct fields and buttons are shown
        onView(withId(R.id.editdiaryentry_entry_text1)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text2)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text3)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text4)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_entry_text5)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void saveDiaryEntry_showsDiaryWithEntry() {
        String newDiaryText1 = "I added this entry after a notification";
        String newDiaryText2 = newDiaryText1 + " - pt2";
        String newDiaryText3 = newDiaryText1 + " - pt3";
        String newDiaryText4 = newDiaryText1 + " - pt4";
        String newDiaryText5 = newDiaryText1 + " - pt5";

        onView(withId(R.id.editdiaryentry_entry_text1)).perform(typeText(newDiaryText1));
        onView(withId(R.id.editdiaryentry_entry_text2)).perform(typeText(newDiaryText2));
        onView(withId(R.id.editdiaryentry_entry_text3)).perform(typeText(newDiaryText3));
        onView(withId(R.id.editdiaryentry_entry_text4)).perform(typeText(newDiaryText4));
        onView(withId(R.id.editdiaryentry_entry_text5)).perform(typeText(newDiaryText5),
                closeSoftKeyboard());

        onView(withId(R.id.editdiaryentry_save_button)).perform(click());

        // Verify entry is displayed on screen
        onView(Matchers.withItemText(newDiaryText1)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText2)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText3)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText4)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(newDiaryText5)).check(matches(isDisplayed()));

        // Delete it to clean up
        deleteDiaryEntry(newDiaryText1);
    }

    @Test
    public void cancelEntry_showsDiary() {
        onView(withId(R.id.editdiaryentry_cancel_button)).perform(click());

        onView(withId(R.id.diary_view)).check(matches(isDisplayed()));
    }
}
