package com.hedgehogproductions.therapyguide.editdiaryentry;

import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.EDIT_MODE;
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
}
