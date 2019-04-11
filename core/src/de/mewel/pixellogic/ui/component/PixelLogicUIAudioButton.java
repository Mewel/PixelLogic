package de.mewel.pixellogic.ui.component;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public class PixelLogicUIAudioButton extends Actor {

    private Sprite audioUnmutedSprite;

    private Sprite audioMuteSprite;

    private PixelLogicAudio audio;

    public PixelLogicUIAudioButton(PixelLogicAssets assets, PixelLogicAudio audio) {
        this.audioUnmutedSprite = assets.getIcon(8);
        this.audioMuteSprite = assets.getIcon(9);
        this.audio = audio;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);
        float alpha = parentAlpha * color.a;
        batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
        batch.draw(audio.isMuted() ? audioMuteSprite : audioUnmutedSprite, getX(), getY(), getWidth(), getHeight());
        batch.setColor(color);
    }

    public void switchAudio() {
        if (audio.isMuted()) {
            audio.unmute();
            audio.playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
        } else {
            audio.mute();
        }
    }

}
