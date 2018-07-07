package com.hedgehogproductions.therapyguide.listen;


import android.content.Context;
import android.media.MediaPlayer;

import org.junit.Before;
//import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

//import static org.junit.Assert.assertFalse;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Mockito.reset;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link ListenPresenter}
 */
@SuppressWarnings("CanBeFinal")
class ListenPresenterTest {

/*    @Mock
    private MediaPlayer mMediaPlayer;

    @Mock
    private ListenContract.View mListenView;

    @Mock
    private Context mContext;


    private ListenPresenter mListenPresenter;

    @Before
    public void setupListenPresenter() {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        // Get a reference to the class under test
        mListenPresenter = new ListenPresenter(mListenView, mContext);
    }


    // TODO Work out how to test Presenter logic. Currently always bypassed as ListenService is not bound
    @Test
    public void handlePlayWhilstStopped_PlaysTrack() {
        // When clicking on play
        mListenPresenter.handlePlayRequest();

        // Then pause button is displayed
        verify(mListenView).showPause();

        // and playback starts
        verify(mMediaPlayer).start();
    }

    @Test
    public void handlePlayWhilstPlaying_PausesPlay() {
        // Setup playing state for test and disregard related mock interactions
        mListenPresenter.handlePlayRequest();
        reset(mListenView);
        reset(mMediaPlayer);

        when(mMediaPlayer.isPlaying()).thenReturn(true);

        // When clicking on play
        mListenPresenter.handlePlayRequest();

        // Then play button is displayed
        verify(mListenView).showPlay();

        // and playback starts
        verify(mMediaPlayer).pause();
    }

    @Test
    public void clickOnStop_StopsTrack() {
        // When clicking on stop
        mListenPresenter.handleStopRequest();

        // Then play button is displayed
        verify(mListenView).showPlay();

        // and playback stops
        verify(mMediaPlayer).stop();
    }

    @Test
    public void handleLoopRequestWhileLooping_TurnsOffLoop() {
        // Setup looping state for test and disregard related mock interactions
        mListenPresenter.handleLoopRequest();
        reset(mListenView);
        reset(mMediaPlayer);

        // When clicking on loop
        mListenPresenter.handleLoopRequest();

        // Then loop button is displayed
        verify(mListenView).showLoop();

        // and mediaPlayer is not looping
        verify(mMediaPlayer).setLooping(false);
    }

    @Test
    public void handleLoopRequestWhileNotLooping_TurnsOnLoop() {
        // When clicking on loop
        mListenPresenter.handleLoopRequest();

        // Then disable loop button is shown
        verify(mListenView).showStopLoop();

        // and looping message is shown
        verify(mListenView).showLoopMessage();

        // and mediaPlayer is looping
        verify(mMediaPlayer).setLooping(true);
    }

    @Test
    public void clickOnRestart_MovesToStart() {
        // When clicking on restart
        mListenPresenter.handleRestartRequest();

        // Then the media player seeks to start
        verify(mMediaPlayer).seekTo(0);
    }*/

}
