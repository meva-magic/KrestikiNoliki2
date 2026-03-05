package com.example.krestikinoliki;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class SoundManager {
    private static SoundManager instance;
    private MediaPlayer mediaPlayer;
    private SoundPool soundPool;
    private int clickSoundId;
    private Context context;
    private boolean isMusicEnabled = true;
    private boolean isSoundEnabled = true;
    private float musicVolume = 0.5f;
    private float soundVolume = 0.5f;

    private SoundManager(Context context) {
        this.context = context.getApplicationContext();
        loadSettings();

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(audioAttributes)
                .build();

        clickSoundId = soundPool.load(context, R.raw.click_sound, 1);
    }

    public static synchronized SoundManager getInstance(Context context) {
        if (instance == null) {
            instance = new SoundManager(context);
        }
        return instance;
    }

    public static SoundManager getInstance() {
        return instance;
    }

    private void loadSettings() {
        SharedPreferences prefs = context.getSharedPreferences("game_settings", Context.MODE_PRIVATE);
        isMusicEnabled = prefs.getBoolean("sound_enabled", true);
        isSoundEnabled = prefs.getBoolean("sound_enabled", true);
        musicVolume = prefs.getFloat("music_volume", 0.5f);
        soundVolume = prefs.getFloat("music_volume", 0.5f);
    }

    public void updateSettings() {
        loadSettings();
        if (mediaPlayer != null) {
            if (isMusicEnabled) {
                mediaPlayer.setVolume(musicVolume, musicVolume);
                if (!mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            } else {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }
    }

    public void startBackgroundMusic() {
        if (mediaPlayer == null) {
            try {
                mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
                if (mediaPlayer != null) {
                    mediaPlayer.setLooping(true);
                    mediaPlayer.setVolume(isMusicEnabled ? musicVolume : 0, isMusicEnabled ? musicVolume : 0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (mediaPlayer != null && isMusicEnabled && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public void pauseBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    public void stopBackgroundMusic() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public void playClickSound() {
        if (isSoundEnabled && soundPool != null) {
            soundPool.play(clickSoundId, soundVolume, soundVolume, 1, 0, 1);
        }
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (mediaPlayer != null) {
            mediaPlayer.setVolume(isMusicEnabled ? volume : 0, isMusicEnabled ? volume : 0);
        }
    }

    public boolean isMusicEnabled() {
        return isMusicEnabled;
    }

    public boolean isSoundEnabled() {
        return isSoundEnabled;
    }

    public float getMusicVolume() {
        return musicVolume;
    }
}