package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIColumnInfo extends Actor {

    private PixelLogicLevel level;
    private int column;

    private Texture texture;
    private BitmapFont font;

    public PixelLogicGUIColumnInfo(PixelLogicLevel level, int column) {
        this.level = level;
        this.column = column;
        this.font = PixelLogicGUIUtil.getNumberFont();
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        // block
        Color blockColor = level.isColumnComplete(this.column) ? LINE_COMPLETE_COLOR : LINE_COLOR;
        blockColor.a = baseColor.a * parentAlpha;
        batch.setColor(blockColor);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());

        // text
        GlyphLayout layout = new GlyphLayout();
        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), column);
        float x = getX() + 2;
        float y = getY() + (PIXEL_SIZE * 2) - 14;
        Color textColor = TEXT_COLOR;
        textColor.a = baseColor.a * parentAlpha;
        for (int i = 0; i < colLevelData.size(); i++) {
            layout.setText(font, String.valueOf(colLevelData.get(i)),
                    textColor, PIXEL_SIZE, Align.center, false);
            font.draw(batch, layout, x, y - ((colLevelData.size() - 1 - i) * 14));
        }
        batch.setColor(baseColor.r, baseColor.g, baseColor.b, 1f);
    }

    @Override
    public void clear() {
        super.clear();
        this.texture.dispose();
        this.font.dispose();
    }

}
