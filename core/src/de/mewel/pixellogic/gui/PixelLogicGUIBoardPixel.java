package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_BLOCKED_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_EMPTY_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_FILLED_COLOR;

class PixelLogicGUIBoardPixel extends Actor {

    private PixelLogicLevel level;
    private int row, col;

    private Texture texture;

    public PixelLogicGUIBoardPixel(PixelLogicLevel level, int row, int col) {
        this.level = level;
        this.row = row;
        this.col = col;
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        Color color = level.isEmpty(row, col) ? PIXEL_EMPTY_COLOR :
                (level.isFilled(row, col) ? PIXEL_FILLED_COLOR : PIXEL_BLOCKED_COLOR);
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(baseColor);
    }

    @Override
    public void clear() {
        this.texture.dispose();
    }

}
