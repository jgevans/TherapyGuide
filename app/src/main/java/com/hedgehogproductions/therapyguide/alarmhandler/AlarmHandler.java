package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.notifications.NotificationHandler;

public class AlarmHandler extends BroadcastReceiver {

    public static final String DIARY_ALERT = "diaryAlert";

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DIARY_ALERT)){
            Notification diaryReminderNotification = NotificationHandler.getDiaryReminderNotification(context);
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(MainActivity.DIARY_REMINDER_NOTIFICATION_ID, diaryReminderNotification);

            // Create the new alarm for tomorrow
            AlarmSetter.setNextAlarm(context);
        }
    }
}
