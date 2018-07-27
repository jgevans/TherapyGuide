package com.hedgehogproductions.therapyguide;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.swipeRight;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.hasSibling;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.Matchers.kindnessWithText;
import static org.hamcrest.Matchers.allOf;

public class KindnessManipulators {

    /**
     * Convenience method that deletes an entry from the kindness diary
     */
    public static void deleteKindnessEntry(String entryText) {
        // Scroll diary to entry, by finding its text
        onView(withId(R.id.kindness_view)).perform(scrollTo(hasDescendant(withText(entryText))));

        // Swipe the entry
        onView(withText(entryText)).perform(swipeRight());

        // Click confirm
        onView(withText(R.string.ok_delete_entry)).perform(click());

        // Verify that kindness diary is in view and entry has been removed
        onView(withId(R.id.kindness_view)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(entryText)).check(doesNotExist());
    }

    /**
     * Convenience method that creates a default kindness entry
     * Assumption: You are already in the kindness screen
     */
    public static void createKindnessEntry(int kindnessWords, int kindnessThoughts,
                                           int kindnessActions, int kindnessSelf) {
        // Click on the add kindness entry button
        onView(withId(R.id.create_kindness_button)).perform(click());

        onData(kindnessWithText(getTargetContext().getResources().getString(kindnessWords)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_words_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(kindnessThoughts)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_thoughts_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(kindnessActions)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_actions_message))))
                .perform(click());
        onData(kindnessWithText(getTargetContext().getResources().getString(kindnessSelf)))
                .inAdapterView(allOf(withId(R.id.kindness_list), hasSibling(withText(R.string.kindness_create_self_message))))
                .perform(click());

        onView(withText(R.string.kindness_done_button_text)).perform(click());
    }
}
