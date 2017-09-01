package com.hedgehogproductions.therapyguide.alarmhandler;

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
            // Check the Diary reminder setting and set the alarm if needed
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if(sharedPreferences.getBoolean(SettingsFragment.KEY_PREF_DIARY_ALERT, false)) {
                AlarmSetter.setNextAlarm(context);
            }
        }
    }
}
