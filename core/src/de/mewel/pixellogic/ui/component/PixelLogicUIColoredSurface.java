package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIColoredSurface extends Actor {

    private Texture texture;

    public PixelLogicUIColoredSurface(Color surfaceColor) {
        this.setSurface(surfaceColor);
    }

    public void setSurface(Color textureColor) {
        if (this.texture != null) {
            this.texture.dispose();
        }
        this.texture = PixelLogicUIUtil.getTexture(textureColor);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(color);
    }

    @Override
    public void clear() {
        Gdx.app.log("PixelLogicUIColoredSurface", "dispose");
        texture.dispose();
        super.clear();
    }

}
