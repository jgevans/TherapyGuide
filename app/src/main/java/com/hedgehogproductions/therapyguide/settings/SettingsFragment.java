package com.hedgehogproductions.therapyguide.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.alarmhandler.AlarmHandler;
import com.hedgehogproductions.therapyguide.alarmhandler.AlarmSetter;
import com.hedgehogproductions.therapyguide.alarmhandler.BootHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_PREF_DIARY_ALERT = "pref_diary_alert";
    public static final String KEY_PREF_DIARY_ALERT_TIME = "pref_diary_alert_time";

    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;
    private ComponentName mReceiver;
    private PackageManager mPackageManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Define the settings file
        getPreferenceManager().setSharedPreferencesName(MainActivity.PREFERENCES);
        getPreferenceManager().setSharedPreferencesMode(Context.MODE_PRIVATE);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Set the summary for the diary reminder time
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(MainActivity.PREFERENCES, Context.MODE_PRIVATE);
        setDiaryReminderTimeSummary(sharedPreferences);

        // Prepare alarm
        Intent intent = new Intent(getActivity(), AlarmHandler.class);
        intent.setAction(AlarmHandler.DIARY_ALERT);
        mAlarmIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

        mReceiver = new ComponentName(getActivity(), BootHandler.class);
        mPackageManager = getActivity().getPackageManager();
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        switch (key) {
            case KEY_PREF_DIARY_ALERT:
                if(sharedPreferences.getBoolean(KEY_PREF_DIARY_ALERT, false)) {
                    // Turn on alarm
                    AlarmSetter.setNextAlarm(getActivity());

                    mPackageManager.setComponentEnabledSetting(mReceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }
                else {
                    // Turn off alarm
                    mAlarmManager.cancel(mAlarmIntent);

                    mPackageManager.setComponentEnabledSetting(mReceiver,
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                            PackageManager.DONT_KILL_APP);
                }
                break;
            case KEY_PREF_DIARY_ALERT_TIME:
                setDiaryReminderTimeSummary(sharedPreferences);
                if(sharedPreferences.getBoolean(KEY_PREF_DIARY_ALERT, false)) {
                    // Replace alarm
                    mAlarmManager.cancel(mAlarmIntent);
                    AlarmSetter.setNextAlarm(getActivity());
                }
                break;
            default:
                break;
        }
    }

    private void setDiaryReminderTimeSummary(SharedPreferences sharedPreferences)
    {
        long diaryReminderTime = sharedPreferences.getLong(KEY_PREF_DIARY_ALERT_TIME, ~0);

        Calendar calendar;
        if ( diaryReminderTime == ~0 ) {
            calendar = TimePickerPreference.DEFAULT_TIME;
        }
        else {
            calendar = new GregorianCalendar();
            calendar.setTimeInMillis(diaryReminderTime);
        }

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm", Locale.UK);
        String timeFormatted = timeFormatter.format(calendar.getTime());

        Preference diaryReminderTimePref = findPreference(KEY_PREF_DIARY_ALERT_TIME);
        diaryReminderTimePref.setSummary(timeFormatted);
    }
}
