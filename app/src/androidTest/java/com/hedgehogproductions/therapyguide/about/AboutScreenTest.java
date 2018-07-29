package com.hedgehogproductions.therapyguide.about;

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
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class AboutScreenTest {

    /* Rule to launch activity under test */
    @Rule
    public final ActivityTestRule<AboutActivity> mAboutActivityTestRule =
            new ActivityTestRule<>(AboutActivity.class);


    @Test
    public void intro_showsIntroduction() {
        // Verify the correct fields are shown
        onView(withId(R.id.about_image)).check(matches(isDisplayed()));
        onView(withText(R.string.about_text1)).check(matches(isDisplayed()));
    }
}
