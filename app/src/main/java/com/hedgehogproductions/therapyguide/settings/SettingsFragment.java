package com.hedgehogproductions.therapyguide.settings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.alarmhandler.AlarmHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String KEY_PREF_DIARY_ALERT = "pref_diary_alert";
    public static final String KEY_PREF_DIARY_ALERT_TIME = "pref_diary_alert_time";

    private AlarmManager mAlarmManager;
    private PendingIntent mAlarmIntent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        // Set the summary for the diary reminder time
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        setDiaryReminderTimeSummary(sharedPreferences);

        // Prepare alarm
        Intent intent = new Intent(getActivity(), AlarmHandler.class);
        intent.setAction(AlarmHandler.DIARY_ALERT);
        mAlarmIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);
        mAlarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
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
                    long diaryReminderTime = sharedPreferences.getLong(KEY_PREF_DIARY_ALERT_TIME, ~0);
                    if (diaryReminderTime != ~0 ) {
                        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, diaryReminderTime,
                                AlarmManager.INTERVAL_DAY, mAlarmIntent);
                    }
                }
                else {
                    // Turn off alarm
                    mAlarmManager.cancel(mAlarmIntent);
                }
                break;
            case KEY_PREF_DIARY_ALERT_TIME:
                setDiaryReminderTimeSummary(sharedPreferences);
                if(sharedPreferences.getBoolean(KEY_PREF_DIARY_ALERT, false)) {
                    // Replace alarm
                    mAlarmManager.cancel(mAlarmIntent);
                    long diaryReminderTime = sharedPreferences.getLong(KEY_PREF_DIARY_ALERT_TIME, ~0);
                    if (diaryReminderTime != ~0 ) {
                        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, diaryReminderTime,
                                AlarmManager.INTERVAL_DAY, mAlarmIntent);
                    }
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
