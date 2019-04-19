package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public class PixelLogicUISwitchSpriteButton extends PixelLogicUIActor {

    protected Sprite sprite1, sprite2;

    protected boolean active;

    public PixelLogicUISwitchSpriteButton(PixelLogicGlobal global, int spriteIndex1, int spriteIndex2) {
        super(global);
        this.active = true;
        this.sprite1 = global.getAssets().getIcon(spriteIndex1);
        this.sprite2 = global.getAssets().getIcon(spriteIndex2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        super.draw(batch, parentAlpha);
        float alpha = parentAlpha * color.a;
        Sprite sprite = active ? sprite1 : sprite2;
        sprite.setColor(color.r, color.g, color.b, color.a * alpha);
        sprite.setPosition(getX(), getY());
        sprite.setSize(getWidth(), getHeight());
        sprite.draw(batch);
    }

    public void switchButton() {
        getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
        this.active = !this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
