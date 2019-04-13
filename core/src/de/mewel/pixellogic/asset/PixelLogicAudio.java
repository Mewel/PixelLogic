package de.mewel.pixellogic.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicMusic;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.KEY_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_MUSIC_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.MENU_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.MENU_MUSIC_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.PUZZLE_SOLVED_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND;

public class PixelLogicAudio implements PixelLogicListener {

    private PixelLogicEventManager eventManager;

    private PixelLogicMusic menuMusic, levelMusic;

    private Map<String, Sound> soundMap;

    private boolean muted;

    public PixelLogicAudio(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.eventManager = eventManager;
        AssetManager assetManager = assets.get();
        this.soundMap = new HashMap<String, Sound>();
        this.soundMap.put(BUTTON_SOUND, (Sound) assetManager.get(BUTTON_SOUND));
        this.soundMap.put(DRAW_SOUND, (Sound) assetManager.get(DRAW_SOUND));
        this.soundMap.put(BLOCK_SOUND, (Sound) assetManager.get(BLOCK_SOUND));
        this.soundMap.put(SWITCHER_SOUND, (Sound) assetManager.get(SWITCHER_SOUND));
        this.soundMap.put(PUZZLE_SOLVED_SOUND, (Sound) assetManager.get(PUZZLE_SOLVED_SOUND));
        this.soundMap.put(KEY_SOUND, (Sound) assetManager.get(KEY_SOUND));

        this.muted = isMuted();

        Music baseMenuMusic = assetManager.get(MENU_MUSIC);
        baseMenuMusic.setLooping(true);
        baseMenuMusic.setVolume(MENU_MUSIC_VOLUME);
        Music baseLevelMusic = assetManager.get(LEVEL_MUSIC);
        baseLevelMusic.setLooping(true);
        baseLevelMusic.setVolume(LEVEL_MUSIC_VOLUME);
        this.menuMusic = new PixelLogicMusic(baseMenuMusic).setMaxVolume(MENU_MUSIC_VOLUME).setMuted(this.muted);
        this.levelMusic = new PixelLogicMusic(baseLevelMusic).setMaxVolume(LEVEL_MUSIC_VOLUME).setMuted(this.muted);

        if (!this.muted) {
            playMenuMusic();
        }

        eventManager.listen(this);
    }

    public void playSound(String sound, float volume) {
        if (!isMuted()) {
            this.soundMap.get(sound).play(volume);
        }
    }

    public void mute() {
        this.muted = true;
        getPreferences().putBoolean("mute", true);
        getPreferences().flush();
        this.menuMusic.mute();
        this.levelMusic.mute();
    }

    public void unmute() {
        this.muted = false;
        getPreferences().putBoolean("mute", false);
        getPreferences().flush();
        this.menuMusic.unmute();
        this.levelMusic.unmute();
    }

    public boolean isMuted() {
        return this.muted;
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences("pixellogic_settings");
    }

    public PixelLogicMusic getMenuMusic() {
        return menuMusic;
    }

    public PixelLogicMusic getLevelMusic() {
        return levelMusic;
    }

    public void playMenuMusic() {
        this.menuMusic.play();
    }

    public void playLevelMusic() {
        this.levelMusic.play();
    }

    public void act(float delta) {
        this.menuMusic.act(delta);
        this.levelMusic.act(delta);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicUIPageChangeEvent) {
            PixelLogicUIPageChangeEvent pageChangeEvent = (PixelLogicUIPageChangeEvent) event;
            if (pageChangeEvent.getPageId().equals(PixelLogicUIPageId.level)) {
                menuMusic.fadeOut(1f, new Runnable() {
                    @Override
                    public void run() {
                        levelMusic.fadeIn(1f);
                    }
                });
            } else if (pageChangeEvent.oldPageId() != null && pageChangeEvent.oldPageId().equals(PixelLogicUIPageId.level)) {
                levelMusic.fadeOut(1f, new Runnable() {
                    @Override
                    public void run() {
                        menuMusic.fadeIn(1f);
                    }
                });
            }
        }
    }

    public void dispose() {
        this.eventManager.remove(this);
        this.menuMusic.dispose();
        this.levelMusic.dispose();
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
    }

}
