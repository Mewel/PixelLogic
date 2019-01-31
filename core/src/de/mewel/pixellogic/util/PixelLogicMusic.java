package de.mewel.pixellogic.util;

import com.badlogic.gdx.audio.Music;

public class PixelLogicMusic {

    private Music music;

    private float fadeTime;

    private float currentTime;

    private float maxVolume;

    // null = nothing, true = fade in, false = fade out
    private Boolean fade;

    private Runnable afterFadeIn, afterFadeOut;

    public PixelLogicMusic(Music music) {
        this.music = music;
        this.fade = null;
        this.maxVolume = this.music.getVolume();
    }

    public void setMaxVolume(float maxVolume) {
        this.maxVolume = maxVolume;
    }

    public void fadeTo(float time, float volume, Runnable after) {

    }

    public void fadeIn(float time) {
        fadeIn(time, null);
    }

    public void fadeOut(float time) {
        fadeOut(time, null);
    }

    public void fadeIn(float time, Runnable after) {
        this.fadeTime = time;
        this.currentTime = time;
        this.fade = true;
        this.music.setVolume(0f);
        if (!this.music.isPlaying()) {
            this.music.play();
        }
        this.afterFadeIn = after;
    }

    public void fadeOut(float time, Runnable after) {
        this.fadeTime = time;
        this.currentTime = time;
        this.fade = false;
        this.afterFadeOut = after;
    }

    public Music get() {
        return music;
    }

    public void act(float delta) {
        if (fade == null) {
            return;
        }
        this.currentTime -= delta;
        if (this.currentTime <= 0) {
            this.currentTime = fadeTime;
            if (this.fade && this.afterFadeIn != null) {
                this.afterFadeIn.run();
                this.afterFadeIn = null;
            } else if (!this.fade) {
                this.music.stop();
                if (this.afterFadeOut != null) {
                    this.afterFadeOut.run();
                    this.afterFadeOut = null;
                }
            }
            this.music.setVolume(fade ? this.maxVolume : 0f);
            this.fade = null;
            return;
        }
        float volume = (this.currentTime / this.fadeTime) * this.maxVolume;
        this.music.setVolume(fade ? this.maxVolume - volume : volume);
    }

}
