package com.hedgehogproductions.therapyguide.listen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.hedgehogproductions.therapyguide.R;


public class ListenFragment extends Fragment implements ListenContract.View {

    private ListenContract.UserActionsListener mActionsListener;

    private ImageButton mRestartButton;
    private ImageButton mPlayButton;
    private ImageButton mStopButton;
    private ImageButton mLoopButton;

    private Toast mLoopToast;

    public static ListenFragment newInstance() {
        return new ListenFragment();
    }

    public ListenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionsListener = new ListenPresenter(this, getContext());
    }

    @SuppressLint("ShowToast")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listen_tab, container, false);

        // Set up buttons
        mRestartButton = view.findViewById(R.id.restart_button);
        mPlayButton = view.findViewById(R.id.play_button);
        mStopButton = view.findViewById(R.id.stop_button);
        mLoopButton = view.findViewById(R.id.loop_button);
        mRestartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.handleRestartRequest();
            }
        });
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.handlePlayRequest();
            }
        });
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.handleStopRequest();
            }
        });
        mLoopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.handleLoopRequest();
            }
        });
        if(mActionsListener.isLooping()) {
            showStopLoop();
        }

        // Set up toast
        mLoopToast = Toast.makeText(
                this.getContext(),getString(R.string.looping_toast_text), Toast.LENGTH_SHORT);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActionsListener.tearDown();
    }

    @Override
    public void showPlay() {
        mPlayButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    @Override
    public void showPause() {
        mPlayButton.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    @Override
    public void showLoop() {
        mLoopButton.setImageResource(R.drawable.ic_loop_black_24dp);
    }

    @Override
    public void showStopLoop() {
        mLoopButton.setImageResource(R.drawable.ic_disable_loop_black_24dp);
    }

    @Override
    public void showLoopMessage() {
        mLoopToast.show();
    }
}