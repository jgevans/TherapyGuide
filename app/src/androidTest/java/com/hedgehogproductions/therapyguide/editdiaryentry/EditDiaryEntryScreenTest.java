package com.hedgehogproductions.therapyguide.editdiaryentry;


import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.R;

import org.junit.After;
import org.junit.Before;
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

    /**
     * Register Idling Resource so tests wait for toast to disappear before continuing.
     */
    @Before
    public void registerIdlingResource() {
        Espresso.registerIdlingResources(
                EditDiaryEntryFragment.getIdlingResource());
    }

    /**
     * Unregister Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                EditDiaryEntryFragment.getIdlingResource());
    }

    @Test
    public void showAllOptionsForEditMode() {
        // Verify the save, cancel and delete buttons are shown
        onView(withId(R.id.editdiaryentry_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editdiaryentry_delete_button)).check(matches(isDisplayed()));
    }

    @Test
    public void errorShownOnEmptyMessage() {
        String newDiaryText = "I'm not going to fill this in properly";
        // Entry diary entry text and close the keyboard
        onView(withId(R.id.editdiaryentry_entry_text1)).perform(typeText(newDiaryText));
        onView(withId(R.id.editdiaryentry_entry_text2)).perform(typeText(newDiaryText));
        onView(withId(R.id.editdiaryentry_entry_text3)).perform(typeText(""));
        onView(withId(R.id.editdiaryentry_entry_text4)).perform(typeText(newDiaryText));
        onView(withId(R.id.editdiaryentry_entry_text5)).perform(typeText(newDiaryText),
                closeSoftKeyboard());

        // Attempt to save the entry
        onView(withId(R.id.editdiaryentry_save_button)).perform(click());

        // Verify empty entry toast is shown
        String emptyNoteMessageText =
                getTargetContext().getString(R.string.empty_entry_error_toast_text);
        onView(withText(emptyNoteMessageText)).inRoot(showsToast()).check(matches(isDisplayed()));
    }

    @Test
    public void pressDelete_ShowsConfirmation() {
        // Attempt to delete the entry
        onView(withId(R.id.editdiaryentry_delete_button)).perform(click());

        // Verify deletion dialog message is shown
        onView(withText(R.string.dialog_delete_diary_entry)).check(matches(isDisplayed()));
    }
}

