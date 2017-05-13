package com.hedgehogproductions.therapyguide.adddiaryentry;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hedgehogproductions.therapyguide.Injection;
import com.hedgehogproductions.therapyguide.R;


public class AddDiaryEntryFragment extends Fragment implements AddDiaryEntryContract.View {

    private AddDiaryEntryContract.UserActionsListener mActionListener;

    private TextView mDiaryText;

    public static AddDiaryEntryFragment newInstance() {
        return new AddDiaryEntryFragment();
    }

    public AddDiaryEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public void showDiaryView() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActionListener = new AddDiaryEntryPresenter(Injection.provideDiaryRepository(getContext()), this);

        Button saveButton =
                (Button) getActivity().findViewById(R.id.adddiaryentry_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionListener.saveNewDiaryEntry(System.currentTimeMillis(),
                        mDiaryText.getText().toString());
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_adddiaryentry, container, false);
        mDiaryText = (TextView) root.findViewById(R.id.adddiaryentry_entry_text);

        return root;
    }

}
