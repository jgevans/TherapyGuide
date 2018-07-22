package com.hedgehogproductions.therapyguide.listenservice;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.hedgehogproductions.therapyguide.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the implementation of {@link ListenService}
 */

@RunWith(AndroidJUnit4.class)

public class ListenServiceTest {

    private ListenService mService;

    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Before
    public void setupListenService() throws TimeoutException {
        // Create the service Intent.
        Intent serviceIntent =
                new Intent(InstrumentationRegistry.getTargetContext(),
                        ListenService.class);

        // Data can be passed to the service via the Intent.
        serviceIntent.putExtra(ListenService.TRACK, R.raw.track_1);
        serviceIntent.putExtra(ListenService.LOOPING, true);

        // Start the service
        mServiceRule.startService(serviceIntent);

        // Bind the service and grab a reference to the binder.
        IBinder binder;
        binder = mServiceRule.bindService(serviceIntent);

        // Get the reference to the service, or you can call
        // public methods on the binder directly.
        mService = ((ListenService.ListenServiceBinder) binder).getService();
    }

    @After
    public void destroyListenService() {
        mService.stop();
        mService = null;
    }

    @Test
    public void startedListenService_playerStopped() {
        assertFalse(mService.isPlaying());
    }

    @Test
    public void boundListenServicePlay_playerPlaying() {
        mService.play();

        assertTrue(mService.isPlaying());
    }

    @Test
    public void boundListenServiceLoop_playerLooping() {
        assertFalse(mService.isPlaying());
        assertTrue(mService.isLooping());

        mService.setLooping(false);
        assertFalse(mService.isLooping());

        mService.setLooping(false);
        assertFalse(mService.isLooping());

        mService.setLooping(true);
        assertTrue(mService.isLooping());
    }

    @Test
    public void boundListenServicePause_pausesPlayer() {
        mService.play();
        // Sleep so that pause happens after the track has played a little bit
        try {Thread.sleep(100);} catch(InterruptedException e){assertTrue(false);}
        mService.pause();

        assertFalse(mService.isPlaying());
        assertNotEquals(0, mService.getCurrentPosition());
    }

    @Test
    public void boundListenServicePausedRestart_goesToBeginningPaused() {
        mService.play();
        // Sleep so that pause happens after the track has played a little bit
        try {Thread.sleep(100);} catch(InterruptedException e){assertTrue(false);}
        mService.pause();

        assertFalse(mService.isPlaying());
        assertNotEquals(0, mService.getCurrentPosition());

        mService.restart();

        assertFalse(mService.isPlaying());
        assertEquals(0, mService.getCurrentPosition());

        mService.stop();
    }

    @Test
    public void boundListenServicePlayingRestart_goesToBeginningAndPlays() {
        mService.play();
        // Sleep so that pause happens after the track has played a little bit
        try {Thread.sleep(2000);} catch(InterruptedException e){assertTrue(false);}

        assertTrue(mService.isPlaying());
        int preRestartPosition = mService.getCurrentPosition();
        assertNotEquals(0, preRestartPosition);

        mService.restart();

        assertTrue(mService.isPlaying());
        // Sleep so we can check that track is less progressed than before restart
        try {Thread.sleep(50);} catch(InterruptedException e){assertTrue(false);}
        assertTrue(mService.getCurrentPosition() < preRestartPosition );
    }
}