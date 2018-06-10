package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessActions;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessSelf;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessThoughts;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessWords;

import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.FROM_MAIN_ACTIVITY;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.SELECTED_ENTRY_TIMESTAMP;
import static com.hedgehogproductions.therapyguide.kindness.KindnessFragment.ENTRY_DELETION_REQ_CODE;
import static com.hedgehogproductions.therapyguide.kindness.KindnessFragment.ENTRY_DELETION_RES_CODE_CONFIRM;

public class EditKindnessEntryFragment extends Fragment implements EditKindnessEntryContract.View {

    private EditKindnessEntryContract.UserActionsListener mActionsListener;

    private Spinner mKindnessWords, mKindnessThoughts, mKindnessActions, mKindnessSelf;

    private boolean mEditMode;


    public static EditKindnessEntryFragment newInstance(long entryTimestamp) {
        Bundle arguments = new Bundle();
        arguments.putLong(SELECTED_ENTRY_TIMESTAMP, entryTimestamp);
        EditKindnessEntryFragment fragment = new EditKindnessEntryFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public EditKindnessEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void showKindnessView() {
        getActivity().setResult(Activity.RESULT_OK);
        if(getActivity().getIntent().getBooleanExtra(FROM_MAIN_ACTIVITY, false)) {
            getActivity().finish();
        }
        else {
            // Navigate up to MainActivity with a new intent to view the diary tab
            Intent intent = NavUtils.getParentActivityIntent(getActivity());
            intent.putExtra(MainActivity.REQUESTED_TAB_NAME, getResources().getString(R.string.kindness_tab_name));
            NavUtils.navigateUpTo(getActivity(), intent);
        }
    }

    @Override
    public void showEmptyEntryError() {
        // Show error toast
        Toast errorToast = Toast.makeText(
                this.getContext(), getString(R.string.empty_kindness_error_toast_text), Toast.LENGTH_SHORT);
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
    public void showKindnessDetail(KindnessWords words, KindnessThoughts thoughts,
                                   KindnessActions actions, KindnessSelf self, boolean complete) {
        mKindnessWords.setSelection(0);
        mKindnessThoughts.setSelection(0);
        mKindnessActions.setSelection(0);
        mKindnessSelf.setSelection(0);
    }

    @Override
    public void showEntryDeletionMessage() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionsListener = new EditKindnessEntryPresenter(Injection.provideKindnessRepository(getContext()), this);

        Button saveButton =
                getActivity().findViewById(R.id.editkindnessentry_save_button);
        if( mEditMode ) {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.updateKindnessEntry(
                            KindnessWords.APPEARANCE,
                            KindnessThoughts.DOUBT,
                            KindnessActions.BUY,
                            KindnessSelf.KIND
                    );
                }
            });
        }
        else {
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.saveNewKindnessEntry(
                            System.currentTimeMillis(),
                            KindnessWords.APPEARANCE,
                            KindnessThoughts.DOUBT,
                            KindnessActions.BUY,
                            KindnessSelf.KIND
                    );
                }
            });
        }
        Button cancelButton =
                getActivity().findViewById(R.id.editkindnessentry_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKindnessView();
            }
        });

        // If in edit mode, set up delete button, otherwise, hide it
        Button deleteButton =
                getActivity().findViewById(R.id.editkindnessentry_delete_button);
        if( mEditMode ) {
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActionsListener.instigateKindnessEntryDeletion();
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
        View root = inflater.inflate(R.layout.fragment_editkindnessentry, container, false);
        mKindnessWords = root.findViewById(R.id.editkindnessentry_words_spinner);
        mKindnessThoughts = root.findViewById(R.id.editkindnessentry_thoughts_spinner);
        mKindnessActions = root.findViewById(R.id.editkindnessentry_actions_spinner);
        mKindnessSelf = root.findViewById(R.id.editkindnessentry_self_spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> wordsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.kindness_words_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> thoughtsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.kindness_thoughts_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> actionsAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.kindness_actions_array, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> selfAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.kindness_self_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        wordsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        thoughtsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        actionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selfAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mKindnessWords.setAdapter(wordsAdapter);
        mKindnessThoughts.setAdapter(thoughtsAdapter);
        mKindnessActions.setAdapter(actionsAdapter);
        mKindnessSelf.setAdapter(selfAdapter);

        mEditMode = getActivity().getIntent().getBooleanExtra(EDIT_MODE, false);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mEditMode) {
            long timestamp = getArguments().getLong(SELECTED_ENTRY_TIMESTAMP);
            mActionsListener.openKindnessEntry(timestamp);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( ENTRY_DELETION_REQ_CODE==requestCode ) {
            if( ENTRY_DELETION_RES_CODE_CONFIRM==resultCode ) {
                // Delete Entry
                mActionsListener.deleteKindnessEntry();
                // Close view
                showKindnessView();
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
