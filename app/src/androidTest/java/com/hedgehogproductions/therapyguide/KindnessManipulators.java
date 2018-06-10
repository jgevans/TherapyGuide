package com.hedgehogproductions.therapyguide;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class KindnessManipulators {

    /**
     * Convenience method that deletes an entry from the kindness diary
     */
    public static void deleteKindnessEntry(String entryText) {
        // Scroll diary to entry, by finding its text
        onView(withId(R.id.kindness_view)).perform(scrollTo(hasDescendant(withText(entryText))));

        // Click on the entry
        onView(withText(entryText)).perform(click());

        // Click delete
        onView(withId(R.id.editkindnessentry_delete_button)).perform(click());

        // Click confirm
        onView(withText(R.string.ok_delete_entry)).perform(click());

        // Verify that kindness diary is in view and entry has been removed
        onView(withId(R.id.kindness_view)).check(matches(isDisplayed()));
        onView(Matchers.withItemText(entryText)).check(doesNotExist());
    }
}
