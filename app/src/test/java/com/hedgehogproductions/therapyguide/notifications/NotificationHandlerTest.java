package com.hedgehogproductions.therapyguide.notifications;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.hedgehogproductions.therapyguide.R;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link NotificationHandler}
 */

@SuppressWarnings("CanBeFinal")
public class NotificationHandlerTest {

    @Mock
    private Context mContext;

    @Mock
    private NotificationManager mNotificationManager;

    @Before
    public void setupNotificationHandler() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);
    }

    @TargetApi(26)
    @Test
    public void createNotificationChannels_createsCorrectChannels() {

        try {
            setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 26);
        } catch (Exception e) {
            e.printStackTrace();
        }
        when(mContext.getSystemService(Context.NOTIFICATION_SERVICE)).thenReturn(mNotificationManager);
        when(mContext.getString(R.string.diary_reminder_notification_channel_name)).thenReturn("Diary Reminder");
        when(mContext.getString(R.string.player_notification_channel_name)).thenReturn("Player");

        NotificationHandler.createNotificationChannels(mContext);

        verify(mNotificationManager, times(2)).createNotificationChannel(any(NotificationChannel.class));
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        @SuppressWarnings("JavaReflectionMemberAccess") Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
