package de.mewel.pixellogic.ui.component;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public class PixelLogicUIAudioButton extends Actor {

    private Sprite audioUnmutedSprite;

    private Sprite audioMuteSprite;

    private PixelLogicGlobal global;

    public PixelLogicUIAudioButton(PixelLogicGlobal global) {
        this.audioUnmutedSprite = global.getAssets().getIcon(8);
        this.audioMuteSprite = global.getAssets().getIcon(9);
        this.global = global;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);
        float alpha = parentAlpha * color.a;
        batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
        batch.draw(global.getAudio().isMuted() ? audioMuteSprite : audioUnmutedSprite, getX(), getY(), getWidth(), getHeight());
        batch.setColor(color);
    }

    public void switchAudio() {
        PixelLogicAudio audio = global.getAudio();
        if (audio.isMuted()) {
            audio.unmute();
            audio.playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
            if (PixelLogicUIPageId.mainMenu.equals(global.getAppScreen().getCurrentPage().getPageId())) {
                audio.playMenuMusic();
            } else {
                audio.playLevelMusic();
            }
        } else {
            audio.mute();
        }
    }

}
