package com.hedgehogproductions.therapyguide.adddiaryentry;

import android.os.IBinder;
import android.support.test.espresso.Root;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.WindowManager;

import com.hedgehogproductions.therapyguide.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
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


/**
 * Tests for the new diary entry screen, the screen which allows the creation
 *  of a new diary entry.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest

public class AddDiaryEntryScreenTest {

    // Custom matcher to match a toast
    private Matcher<Root> showsToast() {
        return new TypeSafeMatcher<Root>() {

            @Override
            public boolean matchesSafely(Root root) {
                int type = root.getWindowLayoutParams().get().type;
                if (type == WindowManager.LayoutParams.TYPE_TOAST) {
                    IBinder windowToken = root.getDecorView().getWindowToken();
                    IBinder appToken = root.getDecorView().getApplicationWindowToken();
                    if (windowToken == appToken) {
                        // windowToken == appToken means this window isn't contained by any other windows.
                        // if it was a window for an activity, it would have TYPE_BASE_APPLICATION.
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is toast");
            }
        };
    }

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<AddDiaryEntryActivity> mEntryActivityTestRule =
            new ActivityTestRule<>(AddDiaryEntryActivity.class);

    @Test
    public void errorShownOnEmptyMessage() {
        // Add diary entry text and close the keyboard
        onView(withId(R.id.adddiaryentry_entry_text)).perform(typeText(""),
                closeSoftKeyboard());

        // Attempt to save the entry
        onView(withId(R.id.adddiaryentry_save_button)).perform(click());

        // Verify empty entry toast is shown
        String emptyNoteMessageText =
                getTargetContext().getString(R.string.empty_entry_error_toast_text);
        onView(withText(emptyNoteMessageText)).inRoot(showsToast()).check(matches(isDisplayed()));
    }
}
