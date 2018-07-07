package com.hedgehogproductions.therapyguide.alarmhandler;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.settings.SettingsFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Calendar;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link AlarmSetter}
 */

@SuppressWarnings("CanBeFinal")
public class AlarmSetterTest {

    @Mock
    private Context mContext;

    @Mock
    private AlarmManager mAlarmManager;

    @Mock
    private SharedPreferences mSharedPrefs;

    @Captor
    private ArgumentCaptor<Long> mSetAlarmCallbackTimeCaptor;

    @Before
    public void setupAlarmSetter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        try {
            setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 20);
        } catch (Exception e) {
            e.printStackTrace();
        }

        when(mContext.getSharedPreferences(anyString(), anyInt())).thenReturn(mSharedPrefs);
    }


    @TargetApi(20)
    @Test
    public void setNextAlarm_AlarmForLaterToday() {
        // Calculate the time for the next Alarm
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis()+3600000);
        when(mContext.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE)).thenReturn(mSharedPrefs);
        when(mSharedPrefs.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0)).thenReturn(alarm.getTimeInMillis());
        when(mContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mAlarmManager);

        AlarmSetter.setNextAlarm(mContext);

        verify(mAlarmManager).setExact(eq(AlarmManager.RTC_WAKEUP), mSetAlarmCallbackTimeCaptor.capture(), isNull(PendingIntent.class));

        // Create a calendar for the set alarm
        Calendar newAlarm = Calendar.getInstance();
        newAlarm.setTimeInMillis(mSetAlarmCallbackTimeCaptor.getValue());
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        assertEquals(now.get(Calendar.DAY_OF_YEAR), newAlarm.get(Calendar.DAY_OF_YEAR));
        assertEquals(alarm.get(Calendar.HOUR_OF_DAY), newAlarm.get(Calendar.HOUR_OF_DAY));
        assertEquals(alarm.get(Calendar.MINUTE), newAlarm.get(Calendar.MINUTE));
    }

    @TargetApi(20)
    @Test
    public void setNextAlarm_AlarmEarlierTodayAlreadyHappenedToday() {
        // Calculate the time for the next Alarm
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis()-3600000);
        // Set time of last alarm to earlier today
        Calendar lastAlarm = Calendar.getInstance();
        lastAlarm.setTimeInMillis(System.currentTimeMillis()-1800000);
        when(mContext.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE)).thenReturn(mSharedPrefs);
        when(mSharedPrefs.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0)).thenReturn(alarm.getTimeInMillis());
        when(mSharedPrefs.getLong(AlarmHandler.LAST_DIARY_NOTIFICATION_PREF, ~0)).thenReturn(lastAlarm.getTimeInMillis());
        when(mContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mAlarmManager);

        AlarmSetter.setNextAlarm(mContext);

        verify(mAlarmManager).setExact(eq(AlarmManager.RTC_WAKEUP), mSetAlarmCallbackTimeCaptor.capture(), isNull(PendingIntent.class));

        // Create a calendar for the set alarm
        Calendar newAlarm = Calendar.getInstance();
        newAlarm.setTimeInMillis(mSetAlarmCallbackTimeCaptor.getValue());
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        assertEquals(now.get(Calendar.DAY_OF_YEAR)+1, newAlarm.get(Calendar.DAY_OF_YEAR));
        assertEquals(alarm.get(Calendar.HOUR_OF_DAY), newAlarm.get(Calendar.HOUR_OF_DAY));
        assertEquals(alarm.get(Calendar.MINUTE), newAlarm.get(Calendar.MINUTE));
    }

    @TargetApi(20)
    @Test
    public void setNextAlarm_AlarmEarlierTodayNotYetHappenedToday() {
        // Calculate the time for the next Alarm
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis()-3600000);
        // Set time of last alarm to yesterday
        Calendar lastAlarm = Calendar.getInstance();
        lastAlarm.setTimeInMillis(System.currentTimeMillis()-86400000);
        when(mContext.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE)).thenReturn(mSharedPrefs);
        when(mSharedPrefs.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0)).thenReturn(alarm.getTimeInMillis());
        when(mSharedPrefs.getLong(AlarmHandler.LAST_DIARY_NOTIFICATION_PREF, ~0)).thenReturn(lastAlarm.getTimeInMillis());
        when(mContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mAlarmManager);

        AlarmSetter.setNextAlarm(mContext);

        verify(mAlarmManager).setExact(eq(AlarmManager.RTC_WAKEUP), mSetAlarmCallbackTimeCaptor.capture(), isNull(PendingIntent.class));

        // Create a calendar for the set alarm
        Calendar newAlarm = Calendar.getInstance();
        newAlarm.setTimeInMillis(mSetAlarmCallbackTimeCaptor.getValue());
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        assertEquals(now.get(Calendar.DAY_OF_YEAR), newAlarm.get(Calendar.DAY_OF_YEAR));
        assertEquals(alarm.get(Calendar.HOUR_OF_DAY), newAlarm.get(Calendar.HOUR_OF_DAY));
        assertEquals(alarm.get(Calendar.MINUTE), newAlarm.get(Calendar.MINUTE));
    }

    @TargetApi(20)
    @Test
    public void setNextAlarm_AlarmEarlierTodayDoNotKnowIfHappened() {
        // Calculate the time for the next Alarm
        Calendar alarm = Calendar.getInstance();
        alarm.setTimeInMillis(System.currentTimeMillis()-3600000);
        when(mContext.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE)).thenReturn(mSharedPrefs);
        when(mSharedPrefs.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0)).thenReturn(alarm.getTimeInMillis());
        when(mContext.getSystemService(Context.ALARM_SERVICE)).thenReturn(mAlarmManager);

        AlarmSetter.setNextAlarm(mContext);

        verify(mAlarmManager).setExact(eq(AlarmManager.RTC_WAKEUP), mSetAlarmCallbackTimeCaptor.capture(), isNull(PendingIntent.class));

        // Create a calendar for the set alarm
        Calendar newAlarm = Calendar.getInstance();
        newAlarm.setTimeInMillis(mSetAlarmCallbackTimeCaptor.getValue());
        Calendar now = Calendar.getInstance();
        now.setTimeInMillis(System.currentTimeMillis());

        assertEquals(now.get(Calendar.DAY_OF_YEAR)+1, newAlarm.get(Calendar.DAY_OF_YEAR));
        assertEquals(alarm.get(Calendar.HOUR_OF_DAY), newAlarm.get(Calendar.HOUR_OF_DAY));
        assertEquals(alarm.get(Calendar.MINUTE), newAlarm.get(Calendar.MINUTE));
    }

    private static void setFinalStatic(Field field, Object newValue) throws Exception {
        field.setAccessible(true);

        @SuppressWarnings("JavaReflectionMemberAccess") Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, newValue);
    }

}
