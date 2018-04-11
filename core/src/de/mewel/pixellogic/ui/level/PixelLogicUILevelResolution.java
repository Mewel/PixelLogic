package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUILevelResolution {

    private PixelLogicLevel level;

    private int gamePixelSize;

    private int gameSpaceSize;

    private int fontSize;

    public PixelLogicUILevelResolution(PixelLogicLevel level) {
        this.level = level;
        build();
    }

    private void build() {
        int[] numbers = PixelLogicUIUtil.getMaxInfoNumbers(level);
        int rows = level.getRows();
        int columns = level.getColumns();

        float maxWidth = Gdx.graphics.getWidth();
        float maxHeight = PixelLogicUIUtil.getLevelMaxHeight();

        float rowInfoWidth = 0;
        float colInfoHeight = 0;

        float pixelPerColumn = MathUtils.floor(maxWidth - rowInfoWidth / (float) columns);
        float pixelPerRow = MathUtils.floor(maxHeight - colInfoHeight / (float) rows);

        float maxPixel = Math.min(pixelPerColumn, pixelPerRow);

        this.gameSpaceSize = MathUtils.floor(maxPixel / 17);
        this.gamePixelSize = (int) maxPixel - this.gameSpaceSize;
        this.fontSize = PixelLogicAssets.getFixedLevelFontSize(this.gamePixelSize / 3);
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
