package com.hedgehogproductions.therapyguide.listen;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static com.hedgehogproductions.therapyguide.Matchers.showsToast;

/**
 * Tests for the listen screen, the screen which contains the track player
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class ListenScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public ActivityTestRule<MainActivity> mMainActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);


    @Test
    public void loop_showsLoopMessage() {
        // Click on the listen tab
        onView(withText(R.string.listen_tab_name)).perform(click());

        // Click on the loop button
        onView(withId(R.id.loop_button)).perform(click());

        // Check the toast is shown
        String loopingMessageText =
                getTargetContext().getString(R.string.looping_toast_text);
        onView(withText(loopingMessageText)).inRoot(showsToast()).check(matches(isDisplayed()));
    }

}
