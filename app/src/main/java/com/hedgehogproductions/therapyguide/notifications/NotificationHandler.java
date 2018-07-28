package com.hedgehogproductions.therapyguide.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.TaskStackBuilder;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity;

public class NotificationHandler {

    private static final String NOTIFICATION_CHANNEL_ID_DIARY = "diary_notification_channel";
    private static final String NOTIFICATION_CHANNEL_ID_PLAYER = "player_notification_channel";

    public static void createNotificationChannels(Context context) {

        if(android.os.Build.VERSION.SDK_INT >= 26) {

            NotificationChannel diaryChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_DIARY,
                    context.getString(R.string.diary_reminder_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            diaryChannel.setDescription(context.getString(R.string.diary_reminder_notification_channel_description));

            NotificationChannel playerChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID_PLAYER,
                    context.getString(R.string.player_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            playerChannel.setDescription(context.getString(R.string.player_notification_channel_description));

            // Create channels
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(diaryChannel);
            notificationManager.createNotificationChannel(playerChannel);
        }

    }

    public static Notification getDiaryReminderNotification(Context context) {
        Notification diaryReminderNotification;

        // Create a notification for the diary reminder
        Intent notificationIntent = new Intent(context, EditDiaryEntryActivity.class);
        // Add a back stack so that pressing back on the add screen takes us to the diary
        PendingIntent pendingIntent = TaskStackBuilder.create(context)
                .addNextIntentWithParentStack(notificationIntent)
                .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= 26) {
            diaryReminderNotification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID_DIARY)
                    .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                    .setContentText(context.getText(R.string.diary_reminder_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                    .setAutoCancel(true)
                    .build();
        } else if(Build.VERSION.SDK_INT >= 21) {
            //noinspection deprecation
            diaryReminderNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                    .setContentText(context.getText(R.string.diary_reminder_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                    .setAutoCancel(true)
                    .build();
        } else if(Build.VERSION.SDK_INT >= 16) {
            //noinspection deprecation
            diaryReminderNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                    .setContentText(context.getText(R.string.diary_reminder_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                    .setAutoCancel(true)
                    .build();
        } else {
            //noinspection deprecation
            diaryReminderNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.diary_reminder_notification_title))
                    .setContentText(context.getText(R.string.diary_reminder_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.diary_reminder_notification_ticker_text))
                    .setAutoCancel(true)
                    .getNotification();
        }
        return diaryReminderNotification;
    }

    public static Notification getListenPlayerNotification(Context context) {
        Notification playerNotification;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra(MainActivity.REQUESTED_TAB_NAME, context.getString(R.string.listen_tab_name));
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(Build.VERSION.SDK_INT >= 26) {
            playerNotification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID_PLAYER)
                    .setContentTitle(context.getText(R.string.player_notification_title))
                    .setContentText(context.getText(R.string.player_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.player_notification_ticker_text))
                    .build();
        } else if(Build.VERSION.SDK_INT >= 21) {
            //noinspection deprecation
            playerNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.player_notification_title))
                    .setContentText(context.getText(R.string.player_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.player_notification_ticker_text))
                    .build();
        } else if(Build.VERSION.SDK_INT >= 16) {
            //noinspection deprecation
            playerNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.player_notification_title))
                    .setContentText(context.getText(R.string.player_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.player_notification_ticker_text))
                    .build();
        } else {
            //noinspection deprecation
            playerNotification = new Notification.Builder(context)
                    .setContentTitle(context.getText(R.string.player_notification_title))
                    .setContentText(context.getText(R.string.player_notification_message))
                    .setSmallIcon(R.drawable.ic_lighthouse_icon)
                    .setContentIntent(pendingIntent)
                    .setTicker(context.getText(R.string.player_notification_ticker_text))
                    .getNotification();
        }
        return playerNotification;
    }
}
