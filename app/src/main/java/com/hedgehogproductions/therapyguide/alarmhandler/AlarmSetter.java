package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hedgehogproductions.therapyguide.settings.SettingsFragment;

import java.util.Calendar;

public class AlarmSetter {

    public static void setNextAlarm(Context context) {
        // Prepare alarm
        Intent diaryAlertIntent = new Intent(context, AlarmHandler.class);
        diaryAlertIntent.setAction(AlarmHandler.DIARY_ALERT);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, diaryAlertIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // Get alarm time
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long diaryReminderTime = sharedPreferences.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0);

        if (diaryReminderTime != ~0 ) {
            Calendar diaryAlertTime = Calendar.getInstance();
            diaryAlertTime.setTimeInMillis(diaryReminderTime);
            Calendar alarmCalendar = Calendar.getInstance();
            alarmCalendar.setTimeInMillis(System.currentTimeMillis());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, diaryAlertTime.get(Calendar.HOUR_OF_DAY));
            alarmCalendar.set(Calendar.MINUTE, diaryAlertTime.get(Calendar.MINUTE));

            // Compare with current time to see if the next alarm is today or tomorrow
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTimeInMillis(System.currentTimeMillis());

            long alarmTime = alarmCalendar.getTimeInMillis();
            if( alarmCalendar.getTimeInMillis() <= nowCalendar.getTimeInMillis() ){
                // If tomorrow then add one day
                alarmTime = alarmTime + AlarmManager.INTERVAL_DAY;
            }

            // Create the alarm
            if (android.os.Build.VERSION.SDK_INT < 19) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, alarmIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmIntent);
            }
        }
    }
}
