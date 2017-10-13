package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.notifications.NotificationHandler;

public class AlarmHandler extends BroadcastReceiver {

    public static final String DIARY_ALERT = "diaryAlert";
    public static final String LAST_DIARY_NOTIFICATION_PREF = "last_diary_notification";

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DIARY_ALERT)){
            Notification diaryReminderNotification = NotificationHandler.getDiaryReminderNotification(context);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(MainActivity.DIARY_REMINDER_NOTIFICATION_ID, diaryReminderNotification);

            // Save the time of the notification so we know it when we set up a new one
            SharedPreferences settings = context.getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putLong(LAST_DIARY_NOTIFICATION_PREF, System.currentTimeMillis());
            editor.apply();

            // Create the new alarm for tomorrow
            AlarmSetter.setNextAlarm(context);
        }
    }
}
