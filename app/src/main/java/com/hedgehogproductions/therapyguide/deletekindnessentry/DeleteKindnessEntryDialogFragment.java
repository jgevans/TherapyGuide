package com.hedgehogproductions.therapyguide.deletekindnessentry;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindness.KindnessFragment;

public class DeleteKindnessEntryDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_delete_entry)
                .setPositiveButton(R.string.ok_delete_entry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Delete Diary Entry
                        getTargetFragment().onActivityResult(
                                getTargetRequestCode(),
                                KindnessFragment.ENTRY_DELETION_RES_CODE_CONFIRM,
                                new Intent(Intent.ACTION_ANSWER));
                    }
                })
                .setNegativeButton(R.string.cancel_delete_entry, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelDeletion();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    // Handle back and other cancel gestures
    @Override
    public void onCancel(DialogInterface dialogInterface) {
        cancelDeletion();
    }

    private void cancelDeletion() {
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                KindnessFragment.ENTRY_DELETION_RES_CODE_CANCEL,
                new Intent(Intent.ACTION_ANSWER));
    }
}
