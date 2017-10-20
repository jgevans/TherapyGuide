package com.hedgehogproductions.therapyguide.editdiaryentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.deletediaryentry.DeleteDiaryEntryDialogFragment;

import static com.hedgehogproductions.therapyguide.diary.DiaryFragment.ENTRY_DELETION_REQ_CODE;
import static com.hedgehogproductions.therapyguide.diary.DiaryFragment.ENTRY_DELETION_RES_CODE_CONFIRM;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.FROM_MAIN_ACTIVITY;
import static com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity.SELECTED_ENTRY_TIMESTAMP;

public class EditDiaryEntryFragment extends Fragment implements EditDiaryEntryContract.View {

    private EditDiaryEntryContract.UserActionsListener mActionsListener;

    private TextView mDiaryText1, mDiaryText2, mDiaryText3, mDiaryText4, mDiaryText5;

    private boolean mEditMode;

    public static EditDiaryEntryFragment newInstance(long entryTimestamp) {
        Bundle arguments = new Bundle();
        arguments.putLong(SELECTED_ENTRY_TIMESTAMP, entryTimestamp);
        EditDiaryEntryFragment fragment = new EditDiaryEntryFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public EditDiaryEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void showDiaryView() {
        getActivity().setResult(Activity.RESULT_OK);
        if(getActivity().getIntent().getBooleanExtra(FROM_MAIN_ACTIVITY, false)) {
            getActivity().finish();
        }
        else {
            // Navigate up to MainActivity with a new intent to view the diary tab
            Intent intent = NavUtils.getParentActivityIntent(getActivity());
            intent.putExtra(MainActivity.REQUESTED_TAB_NAME, getResources().getString(R.string.diary_tab_name));
            NavUtils.navigateUpTo(getActivity(), intent);
        }
    }

    @Override
    public void showEmptyEntryError() {
        // Show error toast
        Toast errorToast = Toast.makeText(
                this.getContext(),getString(R.string.empty_entry_error_toast_text), Toast.LENGTH_SHORT);
        errorToast.show();
    }

    @Override
    public void showMissingEntryError() {
        // Show error toast
        Toast errorToast = Toast.makeText(
                this.getContext(),getString(R.string.missing_entry_error_toast_text), Toast.LENGTH_SHORT);
        errorToast.getView().addOnAttachStateChangeListener(listener);
        errorToast.show();

        // TODO - file bug report??
    }

    @Override
    public void showDiaryText(String text1, String text2, String text3, String text4, String text5) {
        mDiaryText1.setText(text1);
        mDiaryText2.setText(text2);
        mDiaryText3.setText(text3);
        mDiaryText4.setText(text4);
        mDiaryText5.setText(text5);
    }

    @Override
    public void showDiaryEntryDeletionMessage() {
        DialogFragment newFragment = new DeleteDiaryEntryDialogFragment();
        newFragment.setTargetFragment(this, ENTRY_DELETION_REQ_CODE);
        newFragment.show(getFragmentManager(), "delete entry");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new EditDiaryEntryPresenter(Injection.provideDiaryRepository(getContext()), this);

        Button saveButton =
                getActivity().findViewById(R.id.editdiaryentry_save_button);
        if( mEditMode ) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.updateDiaryEntry(
                            mDiaryText1.getText().toString(),
                            mDiaryText2.getText().toString(),
                            mDiaryText3.getText().toString(),
                            mDiaryText4.getText().toString(),
                            mDiaryText5.getText().toString()
                            );
                }
            });
        }
        else {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.saveNewDiaryEntry(
                            System.currentTimeMillis(),
                            mDiaryText1.getText().toString(),
                            mDiaryText2.getText().toString(),
                            mDiaryText3.getText().toString(),
                            mDiaryText4.getText().toString(),
                            mDiaryText5.getText().toString()
                            );
                }
            });
        }
        Button cancelButton =
                getActivity().findViewById(R.id.editdiaryentry_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDiaryView();
            }
        });

        // If in edit mode, set up delete button, otherwise, hide it
        Button deleteButton =
                getActivity().findViewById(R.id.editdiaryentry_delete_button);
        if( mEditMode ) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.instigateDiaryEntryDeletion();
                }
            });
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editdiaryentry, container, false);
        mDiaryText1 = root.findViewById(R.id.editdiaryentry_entry_text1);
        mDiaryText2 = root.findViewById(R.id.editdiaryentry_entry_text2);
        mDiaryText3 = root.findViewById(R.id.editdiaryentry_entry_text3);
        mDiaryText4 = root.findViewById(R.id.editdiaryentry_entry_text4);
        mDiaryText5 = root.findViewById(R.id.editdiaryentry_entry_text5);
        mEditMode = getActivity().getIntent().getBooleanExtra(EDIT_MODE, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mEditMode) {
            long timestamp = getArguments().getLong(SELECTED_ENTRY_TIMESTAMP);
            mActionsListener.openDiaryEntry(timestamp);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( ENTRY_DELETION_REQ_CODE==requestCode ) {
            if( ENTRY_DELETION_RES_CODE_CONFIRM==resultCode ) {
                // Delete Entry
                mActionsListener.deleteDiaryEntry();
                // Close view
                showDiaryView();
            }
            // Else remain in current view
        }
    }

    private static final CountingIdlingResource idlingResource = new CountingIdlingResource("toast");
    private static final View.OnAttachStateChangeListener listener = new View.OnAttachStateChangeListener() {
        @Override
        public void onViewAttachedToWindow(final View v) {
            idlingResource.increment();
        }

        @Override
        public void onViewDetachedFromWindow(final View v) {
            idlingResource.decrement();
        }
    };

    // For testing
    public static IdlingResource getIdlingResource() {
        return idlingResource;
    }
}
