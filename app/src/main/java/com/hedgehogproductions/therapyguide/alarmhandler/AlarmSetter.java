package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hedgehogproductions.therapyguide.MainActivity;
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
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        long diaryReminderTime = sharedPreferences.getLong(
                SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0);

        if (diaryReminderTime != ~0 ) {
            Calendar diaryAlertTime = Calendar.getInstance();
            diaryAlertTime.setTimeInMillis(diaryReminderTime);
            // Get a calendar for today and set the time to the alarm time
            Calendar alarmCalendar = Calendar.getInstance();
            alarmCalendar.setTimeInMillis(System.currentTimeMillis());
            alarmCalendar.set(Calendar.HOUR_OF_DAY, diaryAlertTime.get(Calendar.HOUR_OF_DAY));
            alarmCalendar.set(Calendar.MINUTE, diaryAlertTime.get(Calendar.MINUTE));

            // Compare with current time to see if the next alarm is today or tomorrow
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTimeInMillis(System.currentTimeMillis());

            long alarmTime = alarmCalendar.getTimeInMillis();
            // If alarm time is earlier in the day...
            if( alarmCalendar.getTimeInMillis() <= nowCalendar.getTimeInMillis() ){
                // ...then check if we already had today's
                long lastDiaryReminderTime = sharedPreferences.getLong(AlarmHandler.LAST_DIARY_NOTIFICATION_PREF, ~0);
                if (lastDiaryReminderTime != ~0 && lastDiaryReminderTime != 0) {
                    Calendar lastAlertTime = Calendar.getInstance();
                    lastAlertTime.setTimeInMillis(lastDiaryReminderTime);
                    // If last alert was in same year and day of year...
                    if (lastAlertTime.get(Calendar.YEAR) == nowCalendar.get(Calendar.YEAR) &&
                            lastAlertTime.get(Calendar.DAY_OF_YEAR) == nowCalendar.get(Calendar.DAY_OF_YEAR)) {
                        // ...set for tomorrow by adding one day
                        alarmTime = alarmTime + AlarmManager.INTERVAL_DAY;
                    }
                    // Otherwise, leave as is because we haven't done one today and it will trigger immediately
                }
                else {
                    // don't know when the last one was so set for tomorrow by adding one day
                    alarmTime = alarmTime + AlarmManager.INTERVAL_DAY;
                }
            }
            // Otherwise leave as is and it will trigger later today

            // Create the alarm
            if (android.os.Build.VERSION.SDK_INT < 19) {
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, alarmIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmIntent);
            }
        }
    }
}
