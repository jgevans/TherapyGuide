package com.hedgehogproductions.therapyguide.listen;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hedgehogproductions.therapyguide.R;


public class ListenFragment extends Fragment implements ListenContract.View {

    private ListenContract.UserActionsListener mActionsListener;

    private ImageView mPurchaseTrackSleepImage;
    private TextView mPurchaseTrackInfoText;
    private TextView mPurchaseTrackPrice;
    private Button mPurchaseTrackButton;
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
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listen_tab, container, false);

        // Set up buttons
        mPurchaseTrackSleepImage = view.findViewById(R.id.purchase_track_sleep_image);
        mPurchaseTrackInfoText = view.findViewById(R.id.purchase_track_info_text);
        mPurchaseTrackPrice = view.findViewById(R.id.purchase_track_price);
        mPurchaseTrackButton = view.findViewById(R.id.purchase_track_button);
        mRestartButton = view.findViewById(R.id.restart_button);
        mPlayButton = view.findViewById(R.id.play_button);
        mStopButton = view.findViewById(R.id.stop_button);
        mLoopButton = view.findViewById(R.id.loop_button);
        mPurchaseTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionsListener.handlePurchaseTrackRequest();
            }
        });
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

        // Hide buttons depending on whether the track has been purchased
        setTrackPurchaseButtonVisibility();

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

    @Override
    public void setTrackPurchaseButtonVisibility() {
        if(mActionsListener.isTrackPurchased()) {
            mPurchaseTrackSleepImage.setVisibility(View.INVISIBLE);
            mPurchaseTrackInfoText.setVisibility(View.INVISIBLE);
            mPurchaseTrackPrice.setVisibility(View.INVISIBLE);
            mPurchaseTrackButton.setVisibility(View.INVISIBLE);
            mRestartButton.setVisibility(View.VISIBLE);
            mPlayButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.VISIBLE);
            mLoopButton.setVisibility(View.VISIBLE);
        }
        else {
            mPurchaseTrackSleepImage.setVisibility(View.VISIBLE);
            mPurchaseTrackInfoText.setVisibility(View.VISIBLE);
            mPurchaseTrackPrice.setVisibility(View.VISIBLE);
            mPurchaseTrackButton.setVisibility(View.VISIBLE);
            mRestartButton.setVisibility(View.INVISIBLE);
            mPlayButton.setVisibility(View.INVISIBLE);
            mStopButton.setVisibility(View.INVISIBLE);
            mLoopButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setTrackPrice(String price) {
        mPurchaseTrackPrice.setText(price);
    }
}