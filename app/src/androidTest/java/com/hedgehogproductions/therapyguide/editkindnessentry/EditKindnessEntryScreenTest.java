package com.hedgehogproductions.therapyguide.editkindnessentry;

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

import java.util.Date;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withChild;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static com.hedgehogproductions.therapyguide.Matchers.kindnessWithText;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.SELECTED_ENTRY_DATE;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class EditKindnessEntryScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<EditKindnessEntryActivity> mEntryActivityTestRule =
            new ActivityTestRule<EditKindnessEntryActivity>(EditKindnessEntryActivity.class) {

                @Override
                protected Intent getActivityIntent() {
                    Intent editIntent = new Intent();
                    editIntent.putExtra(SELECTED_ENTRY_DATE, new Date());
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
                EditKindnessEntryFragment.getIdlingResource());
    }

    /**
     * Unregister Idling Resource so it can be garbage collected and does not leak any memory.
     */
    @After
    public void unregisterIdlingResource() {
        Espresso.unregisterIdlingResources(
                EditKindnessEntryFragment.getIdlingResource());
    }

    @Test
    public void showNextAndCancelOnFirstPage() {
        // Verify only the next and cancel buttons are shown
        onView(withId(R.id.editkindnessentry_next_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_back_button)).check(matches(not(isDisplayed())));
    }

    @Test
    public void showAllButtonsOnOtherPages() {
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Verify all buttons are shown
        onView(withId(R.id.editkindnessentry_next_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_back_button)).check(matches(isDisplayed()));

        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Verify all buttons are shown
        onView(withId(R.id.editkindnessentry_next_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_back_button)).check(matches(isDisplayed()));

    }

    @Test
    public void showDoneAndBackOnLastPage() {
        // Verify the next button is shown
        onView(withText(R.string.kindness_next_button_text)).check(matches(isDisplayed()));

        onView(withId(R.id.editkindnessentry_next_button)).perform(click());
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Verify the done, cancel and back buttons are shown
        onView(withText(R.string.kindness_done_button_text)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_cancel_button)).check(matches(isDisplayed()));
        onView(withId(R.id.editkindnessentry_back_button)).check(matches(isDisplayed()));
    }

    @Test
    public void errorShownOnEmptyMessage() {
        // Move through without selecting entries
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Attempt to save the entry
        onView(withId(R.id.editkindnessentry_next_button)).perform(click());

        // Verify empty entry toast is shown
        String emptyNoteMessageText =
                getTargetContext().getString(R.string.empty_kindness_error_toast_text);
        onView(withText(emptyNoteMessageText)).inRoot(withDecorView(
                not(mEntryActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void selectingAnOptionMovesToNextScreen() {
        // Verify words screen is shown
        onView(withText(R.string.kindness_create_words_message)).check(matches(isDisplayed()));

        // Select an entry
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_words_call)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_words_message))))
                .perform(click());
        // Verify thoughts screen is shown
        onView(withText(R.string.kindness_create_thoughts_message)).check(matches(isDisplayed()));

        // Select an entry
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_thoughts_gossip)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_thoughts_message))))
                .perform(click());
        // Verify actions screen is shown
        onView(withText(R.string.kindness_create_actions_message)).check(matches(isDisplayed()));

        // Select an entry
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_actions_cake)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_actions_message))))
                .perform(click());
        // Verify self screen is shown
        onView(withText(R.string.kindness_create_self_message)).check(matches(isDisplayed()));

        // Select an entry
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_self_draw)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_self_message))))
                .perform(click());
        // Verify self screen is still shown
        onView(withText(R.string.kindness_create_self_message)).check(matches(isDisplayed()));
    }

    @Test
    public void selectingAnActionShowsRadioButtonSelected() {
        // Select entries for each screen
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_words_call)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_words_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_thoughts_gossip)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_thoughts_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_actions_cake)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_actions_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(R.string.kindness_self_draw)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_self_message))))
                .perform(click());

        // Go back through to check
        onView(allOf(withId(R.id.kindness_item_selector), withParent(withChild(withText(R.string.kindness_self_draw))))).check(matches(isChecked()));
        onView(withId(R.id.editkindnessentry_back_button)).perform(click());
        onView(allOf(withId(R.id.kindness_item_selector), withParent(withChild(withText(R.string.kindness_actions_cake))))).check(matches(isChecked()));
        onView(withId(R.id.editkindnessentry_back_button)).perform(click());
        onView(allOf(withId(R.id.kindness_item_selector), withParent(withChild(withText(R.string.kindness_thoughts_gossip))))).check(matches(isChecked()));
        onView(withId(R.id.editkindnessentry_back_button)).perform(click());
        onView(allOf(withId(R.id.kindness_item_selector), withParent(withChild(withText(R.string.kindness_words_call))))).check(matches(isChecked()));
    }
}
