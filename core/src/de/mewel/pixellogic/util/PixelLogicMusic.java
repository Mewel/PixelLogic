package de.mewel.pixellogic.util;

import com.badlogic.gdx.audio.Music;

public class PixelLogicMusic {

    private final Music music;

    private boolean muted;

    private boolean play;

    private float fadeTime;

    private float currentTime;

    private float maxVolume;

    private Float fromVolume;

    private Float toVolume;

    private Runnable afterFadeAction;

    public PixelLogicMusic(Music music) {
        this.music = music;
        this.maxVolume = this.music.getVolume();
        this.muted = false;
        this.play = false;
    }

    public PixelLogicMusic setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
        if (this.music.getVolume() > this.maxVolume) {
            this.music.setVolume(this.maxVolume);
        }
        return this;
    }

    public void fadeTo(float time, float volume, Runnable after) {
        this.play = true;
        if (after != null && afterFadeAction != null) {
            afterFadeAction.run();
        }
        if (!muted && !this.music.isPlaying()) {
            this.music.play();
        }
        this.fromVolume = music.getVolume();
        this.toVolume = volume;
        this.fadeTime = time;
        this.currentTime = time;
        this.afterFadeAction = after;
    }

    public void fadeIn(float time) {
        fadeIn(time, null);
    }

    public void fadeOut(float time) {
        fadeOut(time, null);
    }

    public void fadeIn(float time, Runnable after) {
        if (!this.music.isPlaying()) {
            this.music.setVolume(0f);
        }
        fadeTo(time, this.maxVolume, after);
    }

    public void fadeOut(float time, Runnable after) {
        fadeTo(time, 0, after);
    }

    public Music music() {
        return this.music;
    }

    public PixelLogicMusic play() {
        this.play = true;
        if (!muted) {
            music.play();
        }
        return this;
    }

    public void stop() {
        this.play = false;
        music.stop();
    }

    public void mute() {
        this.muted = true;
        if (music.isPlaying()) {
            music.stop();
        }
    }

    public void unmute() {
        this.muted = false;
        if (this.play) {
            music.play();
        }
    }

    public PixelLogicMusic setMuted(boolean muted) {
        this.muted = muted;
        return this;
    }

    public void act(float delta) {
        if (toVolume == null || toVolume == this.music.getVolume()) {
            return;
        }
        boolean fadeIn = this.fromVolume < this.toVolume;
        this.currentTime -= delta;
        if (this.currentTime <= 0) {
            this.currentTime = fadeTime;
            this.music.setVolume(this.toVolume);
            if (this.toVolume == 0f) {
                this.music.stop();
            }
            if (this.afterFadeAction != null) {
                this.afterFadeAction.run();
            }
            this.fromVolume = null;
            this.toVolume = null;
            this.afterFadeAction = null;
            return;
        }
        float time = this.currentTime / this.fadeTime;
        float volumeDelta = Math.abs(this.fromVolume - this.toVolume);
        float volumeDiff = volumeDelta * time;
        this.music.setVolume(fadeIn ? this.toVolume - volumeDiff : this.toVolume + volumeDiff);
    }

    public void dispose() {
        this.music.dispose();
    }

}
