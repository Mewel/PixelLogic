package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.g2d.Sprite;

import de.mewel.pixellogic.PixelLogicGlobal;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public class PixelLogicUISwitchSpriteButton extends PixelLogicUISprite {

    protected final Sprite sprite2;

    protected boolean active;

    public PixelLogicUISwitchSpriteButton(PixelLogicGlobal global, int spriteIndex1, int spriteIndex2) {
        super(global);
        this.active = true;
        this.sprite = global.getAssets().getIcon(spriteIndex1);
        this.sprite2 = global.getAssets().getIcon(spriteIndex2);
    }

    @Override
    public Sprite getSprite() {
        return active ? this.sprite : this.sprite2;
    }

    public void switchButton() {
        getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
        this.active = !this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
