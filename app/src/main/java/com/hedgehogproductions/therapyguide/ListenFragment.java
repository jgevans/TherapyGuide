package com.hedgehogproductions.therapyguide;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


public class ListenFragment extends Fragment implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private ImageButton restartButton;
    private ImageButton playButton;
    private ImageButton stopButton;
    private ImageButton loopButton;

    private Toast loopToast;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.listen_tab, container, false);

        // Set up buttons
        restartButton = (ImageButton) view.findViewById(R.id.restart_button);
        playButton = (ImageButton) view.findViewById(R.id.play_button);
        stopButton = (ImageButton) view.findViewById(R.id.stop_button);
        loopButton = (ImageButton) view.findViewById(R.id.loop_button);
        restartButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        stopButton.setOnClickListener(this);
        loopButton.setOnClickListener(this);

        // Set up toast
        loopToast = Toast.makeText(
                this.getContext(),getString(R.string.looping_toast_text), Toast.LENGTH_SHORT);

        return view;
    }

    @Override
    public void onClick(View view) {

        // Create media player if one doesn't currently exist
        if (mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(getContext(), R.raw.track_1);
        }

        switch(view.getId()) {
            case R.id.restart_button:

                mediaPlayer.seekTo(0);
                break;

            case R.id.play_button:

                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);

                } else {
                    mediaPlayer.start();
                    playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                        @Override
                        public void onCompletion(MediaPlayer player) {
                            player.release();
                            playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                            mediaPlayer = null;
                        }

                    });
                }
                break;

            case R.id.stop_button:

                mediaPlayer.stop();
                playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                mediaPlayer.release();
                mediaPlayer = null;
                break;

            case R.id.loop_button:

                mediaPlayer.setLooping(!mediaPlayer.isLooping());
                if (mediaPlayer.isLooping()) {
                    // Turned looping on. Show toast to indicate
                    loopToast.show();
                }
                break;

        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        }
    }
}