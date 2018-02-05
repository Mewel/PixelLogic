package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;

import java.util.List;

import de.mewel.pixellogic.ui.PixelLogicGUIUtil;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIColumnInfo extends Actor {

    private PixelLogicLevel level;
    private int column;

    private Texture texture;

    public PixelLogicGUIColumnInfo(PixelLogicLevel level, int column) {
        this.level = level;
        this.column = column;
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
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        int scale = PixelLogicGUIUtil.getInfoSizeFactor(level);
        GlyphLayout layout = new GlyphLayout();
        List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), column);
        int yOffset = ((int) resolution.getGameFont().getLineHeight() / 2) + 2;
        float x = getX() + 2;
        float y = getY() + (resolution.getGamePixelSize() * scale) - yOffset;
        Color textColor = TEXT_COLOR;
        textColor.a = baseColor.a * parentAlpha;
        for (int i = 0; i < colLevelData.size(); i++) {
            layout.setText(resolution.getGameFont(), String.valueOf(colLevelData.get(i)),
                    textColor, resolution.getGamePixelSize(), Align.center, false);
            resolution.getGameFont().draw(batch, layout, x, y - ((colLevelData.size() - 1 - i) * yOffset));
        }
        batch.setColor(baseColor.r, baseColor.g, baseColor.b, 1f);
    }

    @Override
    public void clear() {
        super.clear();
        this.texture.dispose();
    }

}
