package com.hedgehogproductions.therapyguide.listenservice;

public interface ListenServiceContract {

    void play();

    void pause();

    void restart();

    void stop();

    void switchLooping();

    boolean isPlaying();

    boolean isLooping();

    int getCurrentPosition();
}