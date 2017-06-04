package com.hedgehogproductions.therapyguide.diary;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.adddiaryentry.AddDiaryEntryActivity;
import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.deletediaryentry.DeleteDiaryEntryDialogFragment;
import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.editdiaryentry.EditDiaryEntryActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DiaryFragment extends Fragment implements DiaryContract.View {

    private DiaryContract.UserActionsListener mActionsListener;

    private DiaryEntryAdapter mDiaryEntriesAdapter;

    public static final int ENTRY_DELETION_REQ_CODE = 0;
    public static final int ENTRY_DELETION_RES_CODE_CONFIRM = 0;
    public static final int ENTRY_DELETION_RES_CODE_CANCEL = 1;

    private int mDeletionPosition;

    public static DiaryFragment newInstance() {
        return new DiaryFragment();
    }

    public DiaryFragment() {
        // Required empty public constructor
    }


    public void showDiary(List<DiaryEntry> entries) {
        mDiaryEntriesAdapter.replaceData(entries);
    }

    @Override
    public void showAddDiaryEntry() {
        Intent intent = new Intent(getContext(), AddDiaryEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void showUpdateDiaryEntry(long selectedEntryTimestamp) {
        Intent intent = new Intent(getContext(), EditDiaryEntryActivity.class);
        intent.putExtra(EditDiaryEntryActivity.SELECTED_ENTRY_TIMESTAMP, selectedEntryTimestamp);
        startActivity(intent);
    }

    @Override
    public void showDiaryEntryDeletionMessage(final int position) {
        mDeletionPosition = position;
        DialogFragment newFragment = new DeleteDiaryEntryDialogFragment();
        newFragment.setTargetFragment(this, ENTRY_DELETION_REQ_CODE);
        newFragment.show(getFragmentManager(), "delete entry");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if( ENTRY_DELETION_REQ_CODE==requestCode ) {
            if( ENTRY_DELETION_RES_CODE_CONFIRM==resultCode ) {
                // Delete Entry
                mActionsListener.deleteDiaryEntry(mDiaryEntriesAdapter.getEntry(mDeletionPosition));
                mActionsListener.loadDiary();
                // Refresh view
                mDiaryEntriesAdapter.notifyDataSetChanged();
            }
            else if( ENTRY_DELETION_RES_CODE_CANCEL==resultCode ) {
                // Refresh view
                mDiaryEntriesAdapter.notifyItemChanged(mDeletionPosition);
            }
            mDeletionPosition = ~0;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDiaryEntriesAdapter = new DiaryEntryAdapter(new ArrayList<DiaryEntry>(), mEntryListener);
        mActionsListener = new DiaryPresenter(Injection.provideDiaryRepository(getContext()), this);
        mDeletionPosition = ~0;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadDiary();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diary_tab, container, false);

        // Set up create button
        FloatingActionButton createButton = (FloatingActionButton) view.findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.addNewDiaryEntry();
            }
        });

        // Create the RecyclerView for Diary cards
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.diary_view);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mDiaryEntriesAdapter);

        // Setup the ItemTouchHelper to deal with swiping Diary Cards
        ItemTouchHelper.Callback callback =
                new DiaryTouchHelperCallback(mActionsListener);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    /**
     * Listener for clicks on entries in the RecyclerView.
     */
    private final DiaryEntryListener mEntryListener = new DiaryEntryListener() {
        @Override
        public void onEntryClick(DiaryEntry selectedEntry) {
            mActionsListener.updateDiaryEntry(selectedEntry);
        }
    };


    private static class DiaryEntryAdapter extends RecyclerView.Adapter<DiaryEntryAdapter.ViewHolder> {

        private List<DiaryEntry> mDiaryEntries;
        private final DiaryEntryListener mDiaryEntryListener;

        public DiaryEntryAdapter(@NonNull List<DiaryEntry> diaryEntries, DiaryEntryListener diaryEntryListener) {
            setEntries(diaryEntries);
            mDiaryEntryListener = diaryEntryListener;
        }

        @Override
        public DiaryEntryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.diary_card, parent, false);

            return new ViewHolder(itemView, mDiaryEntryListener);
        }

        @Override
        public void onBindViewHolder(DiaryEntryAdapter.ViewHolder viewHolder, int position) {
            DiaryEntry entry = mDiaryEntries.get(position);

            viewHolder.text.setText(entry.getText());

            // Convert timestamp to useful string description based on age
            long now = System.currentTimeMillis();
            CharSequence ago = DateUtils.getRelativeTimeSpanString(
                    entry.getCreationTimestamp(),
                    now,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_ALL);

            viewHolder.time.setText(ago);
        }

        @Override
        public int getItemCount() {
            return mDiaryEntries.size();
        }


        public void replaceData(List<DiaryEntry> diaryEntries) {
            setEntries(diaryEntries);
            notifyDataSetChanged();
        }

        private void setEntries(List<DiaryEntry> diaryEntries) {
            mDiaryEntries = checkNotNull(diaryEntries);
        }

        public DiaryEntry getEntry(int position) {
            return mDiaryEntries.get(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final TextView text;
            public final TextView time;

            private final DiaryEntryListener mDiaryEntryListener;

            public ViewHolder(View view, DiaryEntryListener diaryEntryListener) {
                super(view);
                mDiaryEntryListener = diaryEntryListener;
                text = (TextView) view.findViewById(R.id.diary_text);
                time = (TextView) view.findViewById(R.id.diary_time);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                DiaryEntry entry = getEntry(position);
                mDiaryEntryListener.onEntryClick(entry);
            }

        }
    }

    public interface DiaryEntryListener {

        void onEntryClick(DiaryEntry selectedEntry);
    }

}