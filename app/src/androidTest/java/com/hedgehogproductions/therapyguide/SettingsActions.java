package com.hedgehogproductions.therapyguide;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.view.View;
import android.widget.TimePicker;

import org.hamcrest.Matcher;

public class SettingsActions {
    @SuppressWarnings("SameParameterValue")
    public static ViewAction setTime(final int hour, final int minute) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                TimePicker tp = (TimePicker) view;
                if(android.os.Build.VERSION.SDK_INT < 23) {
                    //noinspection deprecation
                    tp.setCurrentHour(hour);
                    //noinspection deprecation
                    tp.setCurrentMinute(minute);
                }
                else {
                    tp.setHour(hour);
                    tp.setMinute(minute);
                }
            }
            @Override
            public String getDescription() {
                return "Set the passed time into the TimePicker";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(TimePicker.class);
            }
        };
    }
}