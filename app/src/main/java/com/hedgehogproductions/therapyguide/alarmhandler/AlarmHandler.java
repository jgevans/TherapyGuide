package com.hedgehogproductions.therapyguide.alarmhandler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity;

public class AlarmHandler extends BroadcastReceiver {

    public static final String DIARY_ALERT = "diaryAlert";

    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DIARY_ALERT)){
            // Create a notification for the diary reminder
            Intent notificationIntent = new Intent(context, EditDiaryEntryActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

            Notification diaryReminderNotification;
            if(android.os.Build.VERSION.SDK_INT < 16) {
                //noinspection deprecation
                diaryReminderNotification = new Notification.Builder(context)
                        .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                        .setContentText(context.getText(R.string.diary_reminder_notification_message))
                        .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                        .setContentIntent(pendingIntent)
                        .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                        .setAutoCancel(true)
                        .getNotification();
            } else {
                diaryReminderNotification = new Notification.Builder(context)
                        .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                        .setContentText(context.getText(R.string.diary_reminder_notification_message))
                        .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                        .setContentIntent(pendingIntent)
                        .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                        .setAutoCancel(true)
                        .build();
            }
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(MainActivity.DIARY_REMINDER_NOTIFICATION_ID, diaryReminderNotification);
        }
    }
}
