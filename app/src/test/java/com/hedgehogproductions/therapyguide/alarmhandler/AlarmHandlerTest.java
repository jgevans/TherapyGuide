package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AlarmHandler}
 */
@SuppressWarnings("CanBeFinal")
public class AlarmHandlerTest {

    @Mock
    Context mContext;

    @Mock
    Intent mIntent;

    @Mock
    NotificationManager mNotificationManager;

    private AlarmHandler mAlarmHandler;

    @Before
    public void setupDiaryPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mAlarmHandler = new AlarmHandler();
    }

    @Test
    public void createNotificationForDiaryAlarm() {

        when(mIntent.getAction()).thenReturn(AlarmHandler.DIARY_ALERT);
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        when(mContext.getText(R.string.diary_reminder_notification_title)).thenReturn("");
        when(mContext.getText(R.string.diary_reminder_notification_message)).thenReturn("");
        when(mContext.getText(R.string.diary_reminder_notification_ticker_text)).thenReturn("");

        // TODO get MockContext to behave correctly for this
        //mAlarmHandler.onReceive(mContext, mIntent);
        //verify(mNotificationManager).notify(MainActivity.DIARY_REMINDER_NOTIFICATION_ID, any(Notification.class));

    }
}
