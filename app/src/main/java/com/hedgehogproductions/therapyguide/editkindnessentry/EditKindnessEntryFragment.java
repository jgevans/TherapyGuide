package com.hedgehogproductions.therapyguide.editkindnessentry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.ProgressiveViewPager;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;

import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.EDIT_MODE;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.FROM_MAIN_ACTIVITY;
import static com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity.SELECTED_ENTRY_TIMESTAMP;
import static com.hedgehogproductions.therapyguide.kindness.KindnessFragment.ENTRY_DELETION_REQ_CODE;
import static com.hedgehogproductions.therapyguide.kindness.KindnessFragment.ENTRY_DELETION_RES_CODE_CONFIRM;

public class EditKindnessEntryFragment extends DialogFragment implements EditKindnessEntryContract.View {

    private EditKindnessEntryContract.UserActionsListener mActionsListener;

    private ProgressiveViewPager mViewPager;
    private EditKindnessEntryViewPagerAdapter mViewPagerAdapter;
    private Button mNextButton, mBackButton, mCancelButton;

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActionsListener = new EditKindnessEntryPresenter(Injection.provideKindnessRepository(getContext()), this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Set up buttons
        mNextButton = getActivity().findViewById(R.id.editkindnessentry_next_button);
        mBackButton = getActivity().findViewById(R.id.editkindnessentry_back_button);
        mCancelButton = getActivity().findViewById(R.id.editkindnessentry_cancel_button);

        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextItem = mViewPager.getCurrentItem() + 1;
                if( nextItem < mViewPagerAdapter.getCount() ) {
                    mViewPager.setCurrentItem(nextItem);
                }
                else {
                    // Save entry and close edit activity
                    KindnessEntry kindnessEntry = mActionsListener.getKindnessEntry();
                    if(kindnessEntry == null) {
                        throw new NullPointerException("Null KindessEntry");
                    }
                    if( mEditMode ) {
                        mActionsListener.updateKindnessEntry(
                                kindnessEntry.getWords(),
                                kindnessEntry.getThoughts(),
                                kindnessEntry.getActions(),
                                kindnessEntry.getSelf()
                        );
                    }
                    else {
                        mActionsListener.saveNewKindnessEntry(
                                System.currentTimeMillis(),
                                kindnessEntry.getWords(),
                                kindnessEntry.getThoughts(),
                                kindnessEntry.getActions(),
                                kindnessEntry.getSelf()
                        );
                    }
                }
            }
        });
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        });
        mBackButton.setVisibility(View.INVISIBLE);

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close edit activity
                showKindnessView();
            }
        });

        // TODO ?? If in edit mode, set up delete button, otherwise, hide it
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_editkindnessentry, container, false);
        mEditMode = getActivity().getIntent().getBooleanExtra(EDIT_MODE, false);

        if (mEditMode) {
            long timestamp = getArguments().getLong(SELECTED_ENTRY_TIMESTAMP);
            mActionsListener.openKindnessEntry(timestamp);
        }

        mViewPager = root.findViewById(R.id.editkindnessentry_pager);
        mViewPagerAdapter = new EditKindnessEntryViewPagerAdapter(getContext(), mActionsListener);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        // Add views in order of appearance
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);
        mViewPagerAdapter.addView(R.layout.editkindnessentry_section);

        return root;
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

    private final ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            // Set button text and visibility
            if(position == mViewPagerAdapter.getCount() - 1) {
                mNextButton.setText(R.string.kindness_done_button_text);
            }
            else
            {
                mNextButton.setText(R.string.kindness_next_button_text);
            }
            if(position == 0) {
                mBackButton.setVisibility(View.INVISIBLE);
            }
            else {
                mBackButton.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

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
