package com.example.miniproject_carracing;

import android.content.Context;
import android.media.MediaPlayer;

public class MusicManager {
    private static MusicManager instance;
    private MediaPlayer mediaPlayer;
    private int currentResId = -1;

    private MusicManager() {}

    public static MusicManager getInstance() {
        if (instance == null) {
            instance = new MusicManager();
        }
        return instance;
    }

    public void playMusic(Context context, int resId) {
        if (mediaPlayer != null) {
            if (currentResId == resId && mediaPlayer.isPlaying()) return;
            stop();
        }

        mediaPlayer = MediaPlayer.create(context, resId);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        currentResId = resId;
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            currentResId = -1;
        }
    }

    public void pause() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void resume() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public boolean isPlaying() {
        return mediaPlayer != null && mediaPlayer.isPlaying();
    }
}
