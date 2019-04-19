package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUISprite extends PixelLogicUIActor {

    private Sprite sprite;

    public PixelLogicUISprite(PixelLogicGlobal global) {
        super(global);
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);
        if (sprite != null) {
            float alpha = parentAlpha * color.a;
            batch.setColor(color.r, color.g, color.b, color.a * alpha);
            batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
        }
        batch.setColor(color);
    }

}
