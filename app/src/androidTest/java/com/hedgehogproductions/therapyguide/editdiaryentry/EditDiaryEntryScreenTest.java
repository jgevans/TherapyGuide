package com.hedgehogproductions.therapyguide.editdiaryentry;


import android.content.Intent;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.Matchers.showsToast;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.SELECTED_ENTRY_TIMESTAMP;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditDiaryEntryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<EditDiaryEntryActivity> mEntryActivityTestRule =
            new ActivityTestRule<EditDiaryEntryActivity>(EditDiaryEntryActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Intent editIntent = new Intent();
                    editIntent.putExtra(SELECTED_ENTRY_TIMESTAMP, ~0);
                    editIntent.putExtra(EDIT_MODE, true);
                    return editIntent;
                }
            };

    @Test
    public void showAllOptionsForEditMode() {
        // Verify the save, cancel and delete buttons are shown
        onView(withId(R.id.editdiaryentry_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(isDisplayed()));
    }

    @Test
    public void errorShownOnEmptyMessage() {
        // TODO Fix brittle test by using IdlingResource to handle Toast waits
        //Pause to allow toast to disappear
        try {Thread.sleep(1500);}
        catch(Exception e) {}

        // Entry diary entry text and close the keyboard
        onView(withId(R.id.editdiaryentry_entry_text)).perform(typeText(""),
                closeSoftKeyboard());

        // Attempt to save the entry
        onView(withId(R.id.editdiaryentry_save_button)).perform(click());

        // Verify empty entry toast is shown
        String emptyNoteMessageText =
                getTargetContext().getString(R.string.empty_entry_error_toast_text);
        onView(withText(emptyNoteMessageText)).inRoot(showsToast()).check(matches(isDisplayed()));
    }

    @Test
    public void pressDelete_ShowsConformation() {
        // Attempt to delete the entry
        onView(withId(R.id.editdiaryentry_delete_button)).perform(click());

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_diary_entry)).check(matches(isDisplayed()));
    }
}

