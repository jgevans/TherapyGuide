package com.hedgehogproductions.therapyguide.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TimePicker;

import com.hedgehogproductions.therapyguide.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerPreference extends DialogPreference {

    static final GregorianCalendar DEFAULT_TIME = new GregorianCalendar(0, 0, 0, 16, 0);

    private TimePicker mPicker = null;
    private GregorianCalendar mCalendar;

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPositiveButtonText(R.string.pref_diary_alert_time_save_button);
        setNegativeButtonText(R.string.pref_diary_alert_time_cancel_button);

        setDialogIcon(null);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        long diaryReminderTime = sharedPref.getLong(SettingsFragment.KEY_PREF_DIARY_ALERT_TIME, ~0);

        if ( diaryReminderTime == ~0 ) {
            mCalendar = TimePickerPreference.DEFAULT_TIME;
        }
        else {
            mCalendar = new GregorianCalendar();
            mCalendar.setTimeInMillis(diaryReminderTime);
        }
    }

    @Override
    protected View onCreateDialogView() {
        mPicker = new TimePicker(getContext());
        return mPicker;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        if(android.os.Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            mPicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            //noinspection deprecation
            mPicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        }
        else {
            mPicker.setHour(mCalendar.get(Calendar.HOUR_OF_DAY));
            mPicker.setMinute(mCalendar.get(Calendar.MINUTE));
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if(positiveResult) {
            // Get new time value
            if(android.os.Build.VERSION.SDK_INT < 23) {
                //noinspection deprecation
                mCalendar.set(Calendar.HOUR_OF_DAY, mPicker.getCurrentHour());
                //noinspection deprecation
                mCalendar.set(Calendar.MINUTE, mPicker.getCurrentMinute());
            }
            else {
                mCalendar.set(Calendar.HOUR_OF_DAY, mPicker.getHour());
                mCalendar.set(Calendar.MINUTE, mPicker.getMinute());
            }
            // If different from stored value, persist it
            if(callChangeListener(mCalendar.getTimeInMillis())) {
                persistLong(mCalendar.getTimeInMillis());
                notifyChanged();
            }
        }
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        if(restorePersistedValue) {
            // Restore existing time value
            mCalendar.setTimeInMillis(this.getPersistedLong(DEFAULT_TIME.getTimeInMillis()));
        }
        else {
            // Set default state (not from the XML attribute as this doesn't make sense for a calendar)
            mCalendar = (GregorianCalendar) defaultValue;
            persistLong(mCalendar.getTimeInMillis());
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray attributes, int index) {
        return DEFAULT_TIME;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();
        // Check whether this Preference is persistent (continually saved)
        if (isPersistent()) {
            // No need to save instance state since it's persistent,
            // use superclass state
            return superState;
        }

        // Create instance of custom BaseSavedState
        final SavedState myState = new SavedState(superState);
        // Set the state's value with the class member that holds current
        // setting value
        GregorianCalendar currentTime = new GregorianCalendar();
        // Get current time value
        if(android.os.Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            currentTime.set(Calendar.HOUR_OF_DAY, mPicker.getCurrentHour());
            //noinspection deprecation
            currentTime.set(Calendar.MINUTE, mPicker.getCurrentMinute());
        }
        else {
            currentTime.set(Calendar.HOUR_OF_DAY, mPicker.getHour());
            currentTime.set(Calendar.MINUTE, mPicker.getMinute());
        }

        myState.value = currentTime;
        return myState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        // Check whether we saved the state in onSaveInstanceState
        if (state == null || !state.getClass().equals(SavedState.class)) {
            // Didn't save the state, so call superclass
            super.onRestoreInstanceState(state);
            return;
        }

        // Cast state to custom BaseSavedState and pass to superclass
        SavedState myState = (SavedState) state;
        super.onRestoreInstanceState(myState.getSuperState());

        // Set this Preference's widget to reflect the restored state
        GregorianCalendar currentTime = myState.value;
        if(android.os.Build.VERSION.SDK_INT < 23) {
            //noinspection deprecation
            mPicker.setCurrentHour(currentTime.get(Calendar.HOUR_OF_DAY));
            //noinspection deprecation
            mPicker.setCurrentMinute(currentTime.get(Calendar.MINUTE));
        }
        else {
            mPicker.setHour(currentTime.get(Calendar.HOUR_OF_DAY));
            mPicker.setMinute(currentTime.get(Calendar.MINUTE));
        }
    }

    private static class SavedState extends BaseSavedState {
        // Member that holds the setting's value
        GregorianCalendar value;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            // Get the current preference's value
            value = (GregorianCalendar) source.readSerializable();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Write the preference's value
            dest.writeSerializable(value);
        }

        // Standard creator object using an instance of this class
        public static final Parcelable.Creator<SavedState> CREATOR =
                new Parcelable.Creator<SavedState>() {

                    public SavedState createFromParcel(Parcel in) {
                        return new SavedState(in);
                    }

                    public SavedState[] newArray(int size) {
                        return new SavedState[size];
                    }
                };
    }
}
