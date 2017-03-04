package com.hedgehogproductions.therapyguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class DiaryFragment extends Fragment implements View.OnClickListener {

    private DiaryEntryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.diary_tab, container, false);

        // Set up create button
        FloatingActionButton createButton = (FloatingActionButton) view.findViewById(R.id.create_button);
        createButton.setOnClickListener(this);

        // Create the RecyclerView for Diary cards
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.diary_view);

        adapter = new DiaryEntryAdapter(this.getContext());
        // Register the observer which will be told about data changes
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                adapter.reloadDiary();
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Update diary entries view
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        // Switch to diary entry activity when floating action button is pressed
        Intent intent = new Intent(getContext(), DiaryEntryActivity.class);
        startActivity(intent);
    }

}
