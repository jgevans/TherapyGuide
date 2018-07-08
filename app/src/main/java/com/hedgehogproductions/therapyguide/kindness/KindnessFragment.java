package com.hedgehogproductions.therapyguide.kindness;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.MainActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.deletekindnessentry.DeleteKindnessEntryDialogFragment;
import com.hedgehogproductions.therapyguide.editkindnessentry.EditKindnessEntryActivity;
import com.hedgehogproductions.therapyguide.kindnesscreatereminder.CreateKindnessReminderDialogFragment;
import com.hedgehogproductions.therapyguide.kindnessdata.KindnessEntry;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class KindnessFragment extends Fragment implements KindnessContract.View {

    private KindnessContract.UserActionsListener mActionsListener;

    private KindnessEntryAdapter mKindnessEntriesAdapter;

    public static final int ENTRY_DELETION_REQ_CODE = 0;
    public static final int ENTRY_DELETION_RES_CODE_CONFIRM = 0;
    public static final int ENTRY_DELETION_RES_CODE_CANCEL = 1;

    public static final int CREATION_REMINDER_REQ_CODE = 1;
    public static final int CREATION_REMINDER_RES_CODE_CONFIRM = 0;
    public static final int CREATION_REMINDER_RES_CODE_CANCEL = 1;

    private int mDeletionPosition;

    public static KindnessFragment newInstance() {
        return new KindnessFragment();
    }

    public KindnessFragment() {
        // Required empty public constructor
    }

    @Override
    public void showKindnessDiary(List<KindnessEntry> entries) {
        mKindnessEntriesAdapter.replaceData(entries);

        // If not yet created an entry today, show the Kindness Selector
        Calendar today = new GregorianCalendar();
        today.setTime(new Date(System.currentTimeMillis()));
        today.set(Calendar.HOUR_OF_DAY, 1);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        if( entries.contains(new KindnessEntry(today.getTime())) ){
            hideCreateButton();
        }
        else {
            showCreateButton();
            Calendar nowCalendar = Calendar.getInstance();
            nowCalendar.setTimeInMillis(System.currentTimeMillis());
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(
                    MainActivity.PREFERENCES, Context.MODE_PRIVATE);
            long lastKindnessReminderTime = sharedPreferences.getLong(KindnessPresenter.LAST_KINDNESS_NOTIFICATION_PREF, ~0);
            if (lastKindnessReminderTime != ~0 && lastKindnessReminderTime != 0) {
                Calendar lastAlertTime = Calendar.getInstance();
                lastAlertTime.setTimeInMillis(lastKindnessReminderTime);
                // If last alert was NOT in same year and day of year...
                if (lastAlertTime.get(Calendar.DAY_OF_YEAR) != nowCalendar.get(Calendar.DAY_OF_YEAR) ||
                        lastAlertTime.get(Calendar.YEAR) != nowCalendar.get(Calendar.YEAR)) {
                    showKindnessSelectorMessage();
                }
            }
            else {
                showKindnessSelectorMessage();
            }
        }
    }

    @Override
    public void showAddKindnessEntry() {
        Intent intent = new Intent(getContext(), EditKindnessEntryActivity.class);
        intent.putExtra(EditKindnessEntryActivity.EDIT_MODE, false);
        intent.putExtra(EditKindnessEntryActivity.FROM_MAIN_ACTIVITY, true);
        startActivity(intent);
    }

    @Override
    public void showUpdateKindnessEntry(Date selectedEntryDate) {
        Intent intent = new Intent(getContext(), EditKindnessEntryActivity.class);
        intent.putExtra(EditKindnessEntryActivity.SELECTED_ENTRY_TIMESTAMP, selectedEntryDate);
        intent.putExtra(EditKindnessEntryActivity.EDIT_MODE, true);
        startActivity(intent);
    }

    @Override
    public void showKindnessEntryDeletionMessage(final int position) {
        mDeletionPosition = position;
        DialogFragment newFragment = new DeleteKindnessEntryDialogFragment() ;
        newFragment.setTargetFragment(this, ENTRY_DELETION_REQ_CODE);
        newFragment.show(getFragmentManager(), "delete entry");
    }

    private void showCreateButton() {
        FloatingActionButton createButton = getView().findViewById(R.id.create_kindness_button);
        createButton.setVisibility(View.VISIBLE);
    }

    private void hideCreateButton() {
        FloatingActionButton createButton = getView().findViewById(R.id.create_kindness_button);
        createButton.setVisibility(View.INVISIBLE);
    }

    private void showKindnessSelectorMessage() {
        DialogFragment newFragment = new CreateKindnessReminderDialogFragment() ;
        newFragment.setTargetFragment(this, CREATION_REMINDER_REQ_CODE);
        newFragment.show(getFragmentManager(), "create entry reminder");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( ENTRY_DELETION_REQ_CODE==requestCode ) {
            if( ENTRY_DELETION_RES_CODE_CONFIRM==resultCode ) {
                // Delete Entry
                mActionsListener.deleteKindnessEntry(mKindnessEntriesAdapter.getEntry(mDeletionPosition));
                mActionsListener.loadKindnessDiary();
                // Refresh view
                mKindnessEntriesAdapter.notifyDataSetChanged();
            }
            else if( ENTRY_DELETION_RES_CODE_CANCEL==resultCode ) {
                // Refresh view
                mKindnessEntriesAdapter.notifyItemChanged(mDeletionPosition);
            }
            mDeletionPosition = ~0;
        }
        else if( CREATION_REMINDER_REQ_CODE==requestCode ) {
            mActionsListener.setKindnessNotificationShown(getContext());
            if( CREATION_REMINDER_RES_CODE_CONFIRM==resultCode ) {
                // Open Edit Kindness view
                mActionsListener.addNewKindnessEntry();
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new KindnessPresenter(Injection.provideKindnessRepository(getContext()), this);
        mKindnessEntriesAdapter = new KindnessEntryAdapter(new ArrayList<KindnessEntry>(), mEntryListener, mActionsListener, getContext());
        mDeletionPosition = ~0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadKindnessDiary();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.kindness_tab, container, false);

        // Set up create button
        FloatingActionButton createButton = view.findViewById(R.id.create_kindness_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.addNewKindnessEntry();
            }
        });

        // Create the RecyclerView for Kindness cards
        RecyclerView recyclerView = view.findViewById(R.id.kindness_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mKindnessEntriesAdapter);

        // Setup the ItemTouchHelper to deal with swiping Kindness Cards
        ItemTouchHelper.Callback callback =
                new KindnessTouchHelperCallback(mActionsListener);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * Listener for clicks on entries in the RecyclerView.
     */
    private final KindnessEntryListener mEntryListener = new KindnessEntryListener() {
        @Override
        public void onEntryClick(KindnessEntry selectedEntry) {
            mActionsListener.updateKindnessEntry(selectedEntry);
        }
    };


    private static class KindnessEntryAdapter extends RecyclerView.Adapter<KindnessEntryAdapter.ViewHolder> {

        private List<KindnessEntry> mKindnessEntries;
        private final KindnessEntryListener mKindnessEntryListener;
        private final KindnessContract.UserActionsListener mActionsListener;
        private final Context mContext;

        KindnessEntryAdapter(@NonNull List<KindnessEntry> kindnessEntries,
                             KindnessEntryListener kindnessEntryListener,
                             @NonNull KindnessContract.UserActionsListener kindnessActionsListener,
                             Context context) {
            checkNotNull(kindnessActionsListener);
            setEntries(kindnessEntries);
            mKindnessEntryListener = kindnessEntryListener;
            mActionsListener = kindnessActionsListener;
            mContext = context;
        }

        @NonNull
        @Override
        public KindnessEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.kindness_card, parent, false);

            return new ViewHolder(itemView, mKindnessEntryListener);
        }

        @Override
        public void onBindViewHolder(@NonNull final KindnessEntryAdapter.ViewHolder viewHolder, int position) {
            final KindnessEntry entry = mKindnessEntries.get(position);

            viewHolder.words.setText(entry.getWords().toString(mContext));
            viewHolder.thoughts.setText(entry.getThoughts().toString(mContext));
            viewHolder.actions.setText(entry.getActions().toString(mContext));
            viewHolder.self.setText(entry.getSelf().toString(mContext));
            viewHolder.complete.setChecked(entry.isComplete());

            if(entry.isComplete()) {
                viewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.completeKindness));
            }

            // Convert timestamp to useful string description based on age
            CharSequence dateString;
            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 1);
            today.set(Calendar.MINUTE, 0);
            today.set(Calendar.SECOND, 0);
            today.set(Calendar.MILLISECOND, 0);
            if (entry.getCreationDate().equals(today.getTime())) {
                dateString = "Today";
            }
            else {
                dateString = DateFormat.getDateInstance().format( entry.getCreationDate() );
            }

            viewHolder.time.setText(dateString);

            // Set up the checkbox
            viewHolder.complete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                    if(entry.isComplete() != isChecked) {
                        entry.setComplete(isChecked);
                        // Persist to database
                        mActionsListener.updateKindnessEntryCompleteness(entry);

                        if(entry.isComplete()) {
                            viewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.completeKindness));
                        }
                        else{
                            viewHolder.card.setCardBackgroundColor(mContext.getResources().getColor(R.color.incompleteKindness));
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mKindnessEntries.size();
        }


        void replaceData(List<KindnessEntry> kindnessEntries) {
            setEntries(kindnessEntries);
            notifyDataSetChanged();
        }

        private void setEntries(List<KindnessEntry> kindnessEntries) {
            mKindnessEntries = checkNotNull(kindnessEntries);
        }

        KindnessEntry getEntry(int position) {
            return mKindnessEntries.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            final TextView words, thoughts, actions, self;
            final TextView time;
            final CheckBox complete;
            final CardView card;

            private final KindnessEntryListener mKindnessEntryListener;

            ViewHolder(View view, KindnessEntryListener KindnessEntryListener) {
                super(view);
                mKindnessEntryListener = KindnessEntryListener;
                words = view.findViewById(R.id.kindness_words);
                thoughts = view.findViewById(R.id.kindness_thoughts);
                actions = view.findViewById(R.id.kindness_actions);
                self = view.findViewById(R.id.kindness_self);
                time = view.findViewById(R.id.kindness_time);
                complete = view.findViewById(R.id.kindness_card_checkbox);
                card = view.findViewById(R.id.kindness_card_view);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                KindnessEntry entry = getEntry(position);
                mKindnessEntryListener.onEntryClick(entry);
            }
        }
    }

    interface KindnessEntryListener {
        void onEntryClick(KindnessEntry selectedEntry);
    }
}
