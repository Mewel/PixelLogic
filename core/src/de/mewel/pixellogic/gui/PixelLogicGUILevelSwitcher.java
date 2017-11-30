package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PixelLogicGUILevelSwitcher extends Actor {

    private Sprite background, marker;

    public PixelLogicGUILevelSwitcher(Texture texture) {
        this.background = new Sprite(texture, 1, 0, 32, 32);
        this.marker = new Sprite(texture, 33, 0, 12, 12);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.draw(this.marker, getX(), getY(), 12 * getScaleX(), 12 * getScaleY());
        batch.setColor(color);
    }

}
