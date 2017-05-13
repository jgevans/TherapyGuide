package com.hedgehogproductions.therapyguide.diary;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hedgehogproductions.therapyguide.MainActivity;
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
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.allOf;

/**
 * Tests for the diary screen, the screen which contains a list of all
 *  diary entries.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DiaryScreenTest {

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private Matcher<View> withItemText(final String itemText) {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty");
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View item) {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView.class)),
                        withText(itemText)).matches(item);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("is isDescendantOfA RV with text " + itemText);
            }
        };
    }


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
        onView(withItemText(newDiaryText)).check(matches(isDisplayed()));
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
}
