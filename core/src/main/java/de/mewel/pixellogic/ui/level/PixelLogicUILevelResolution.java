package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUILevelResolution {

    private static final float PIXEL_SPACE_RELATION = 17f;

    private final PixelLogicLevel level;

    private int gamePixelSize;

    private int gameSpaceSize;

    private int fontSize;

    public PixelLogicUILevelResolution(PixelLogicLevel level, int width, int height) {
        this.level = level;
        build(width, height);
    }

    private void build(int width, int height) {
        int rows = level.getRows();
        int columns = level.getColumns();

        int basePixel = Math.min(width, height) / Math.max(rows, columns);
        int basePixelSpace = MathUtils.floor(basePixel / PIXEL_SPACE_RELATION);
        int basePixelCombined = basePixel + basePixelSpace;
        int baseFontSize = PixelLogicAssets.getFixedLevelFontSize((int) (basePixelCombined / 4f));

        int[] numbers = PixelLogicUIUtil.getMaxInfoNumbers(level);
        int rowInfoWidth = Math.max((int)(baseFontSize * numbers[0] * 1.3f), basePixelCombined) + basePixelSpace * 2;
        int colInfoHeight = Math.max((int)(baseFontSize * numbers[1] * 1.4f), basePixelCombined) + basePixelSpace * 2;

        float pixelPerColumn = MathUtils.floor((width - rowInfoWidth) / (float) columns);
        float pixelPerRow = MathUtils.floor((height - colInfoHeight) / (float) rows);
        int maxPixel = MathUtils.floor(Math.min(pixelPerColumn, pixelPerRow));

        while(rowInfoWidth < maxPixel || colInfoHeight < maxPixel) {
            if (rowInfoWidth < maxPixel) {
                rowInfoWidth = maxPixel;
                pixelPerColumn = MathUtils.floor((width - rowInfoWidth) / (float) columns);
            }
            if (colInfoHeight < maxPixel) {
                colInfoHeight = maxPixel;
                pixelPerRow = MathUtils.floor((height - colInfoHeight) / (float) rows);
            }
            maxPixel = MathUtils.floor(Math.min(pixelPerColumn, pixelPerRow));
        }

        this.gameSpaceSize = MathUtils.floor(maxPixel / PIXEL_SPACE_RELATION);
        this.gamePixelSize = maxPixel - this.gameSpaceSize;
        this.fontSize = baseFontSize;
    }

    public int getGamePixelSize() {
        return gamePixelSize;
    }

    public int getGameSpaceSize() {
        return gameSpaceSize;
    }

    public int getGamePixelSizeCombined() {
        return gamePixelSize + gameSpaceSize;
    }

    public int getFontSize() {
        return fontSize;
    }

    public Vector2 getLevelSize() {
        Vector2 boardSize = getBoardSize();
        int rowInfoWidth = getRowInfoWidth();
        int colInfoHeight = getColumnInfoHeight();
        int space = getGameSpaceSize() * 2;
        float x = boardSize.x + rowInfoWidth + space;
        float y = boardSize.y + colInfoHeight + space;
        return new Vector2(x, y);
    }

    public Vector2 getBoardSize() {
        float x = level.getColumns() * getGamePixelSizeCombined() - getGameSpaceSize();
        float y = level.getRows() * getGamePixelSizeCombined() - getGameSpaceSize();
        return new Vector2(x, y);
    }

    public int getRowInfoWidth() {
        int[] numbers = PixelLogicUIUtil.getMaxInfoNumbers(level);
        int fontSize = getFontSize();
        int fittingSize = (int) (fontSize * numbers[0] * 1.3f);
        return Math.max(fittingSize, getGamePixelSize());
    }

    public int getColumnInfoHeight() {
        int[] numbers = PixelLogicUIUtil.getMaxInfoNumbers(level);
        int fontSize = getFontSize();
        int fittingSize = (int) (fontSize * numbers[1] * 1.4f);
        return Math.max(fittingSize, getGamePixelSize());
    }

}
