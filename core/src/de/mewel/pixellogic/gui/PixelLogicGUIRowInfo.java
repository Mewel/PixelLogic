package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIRowInfo extends Actor {

    private PixelLogicLevel level;
    private int row;

    private Texture texture;
    private BitmapFont font;

    public PixelLogicGUIRowInfo(PixelLogicLevel level, int row) {
        this.level = level;
        this.row = row;
        this.font = PixelLogicGUIUtil.getNumberFont();
        this.texture = PixelLogicGUIUtil.getWhiteTexture();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color baseColor = getColor();
        // block
        Color blockColor = level.isRowComplete(this.row) ? LINE_COMPLETE_COLOR : LINE_COLOR;
        blockColor.a = baseColor.a * parentAlpha;
        batch.setColor(blockColor);
        batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());

        // text
        GlyphLayout layout = new GlyphLayout();
        List<Integer> rowLevelData = PixelLogicUtil.getNumbersOfRow(level.getLevelData(), row);
        Collections.reverse(rowLevelData);
        float x = getX() - 4;
        float y = getY() + 12;
        Color textColor = TEXT_COLOR;
        textColor.a = baseColor.a * parentAlpha;
        for (int i = 0; i < rowLevelData.size(); i++) {
            layout.setText(font, String.valueOf(rowLevelData.get(i)),
                    textColor, PIXEL_SIZE * 2, Align.right, false);
            font.draw(batch, layout, x - (i * 10), y);
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
