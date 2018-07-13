package com.hedgehogproductions.therapyguide.createkindnessreminder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindness.KindnessFragment;

public class CreateKindnessReminderDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_kindness_reminder)
                .setPositiveButton(R.string.ok_kindness_reminder, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(),
                                KindnessFragment.CREATION_REMINDER_RES_CODE_CONFIRM,
                                new Intent(Intent.ACTION_ANSWER));
                    }
                })
                .setNegativeButton(R.string.cancel_kindness_reminder, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismissReminder();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Handle back and other cancel gestures
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        dismissReminder();
    }

    private void dismissReminder() {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                KindnessFragment.CREATION_REMINDER_RES_CODE_CANCEL,
                new Intent(Intent.ACTION_ANSWER));
    }
}
