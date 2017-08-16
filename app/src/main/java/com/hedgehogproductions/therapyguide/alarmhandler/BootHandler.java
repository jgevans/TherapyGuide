package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hedgehogproductions.therapyguide.settings.SettingsFragment;

public class BootHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            // Re-enable the Diary reminder
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

            // Prepare alarm
            Intent diaryAlertIntent = new Intent(context, AlarmHandler.class);
            diaryAlertIntent.setAction(AlarmHandler.DIARY_ALERT);
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, diaryAlertIntent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if(sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_DIARY_ALERT, false)) {
                long diaryReminderTime = sharedPreferences.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0);
                if (diaryReminderTime != ~0 ) {
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, diaryReminderTime,
                            AlarmManager.INTERVAL_DAY, alarmIntent);
                }
            }
        }
    }
}
