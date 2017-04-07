package com.hedgehogproductions.therapyguide.diary;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hedgehogproductions.therapyguide.DiaryEntryActivity;
import com.hedgehogproductions.therapyguide.R;
import com.hedgehogproductions.therapyguide.diarydata.DiaryEntry;
import com.hedgehogproductions.therapyguide.diarydata.DiaryServiceApiImpl;
import com.hedgehogproductions.therapyguide.diarydata.InMemoryDiaryRepository;

import java.util.List;

public class DiaryFragment extends Fragment implements DiaryContract.View {


    private DiaryContract.UserActionsListener mActionsListener;


    public void showDiary(List<DiaryEntry> entries) {

    }

    @Override
    public void showAddDiaryEntry() {
        Intent intent = new Intent(getContext(), DiaryEntryActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO use Injection to provideDiaryRepository (to allow injection of mock in testing)
        mActionsListener = new DiaryPresenter(new InMemoryDiaryRepository(new DiaryServiceApiImpl()), this);
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



        return view;
    }

}
