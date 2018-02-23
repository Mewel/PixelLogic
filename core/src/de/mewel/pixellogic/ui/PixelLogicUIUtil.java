package de.mewel.pixellogic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.List;
import java.util.Locale;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelResolution;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BASE_SIZE;

public class PixelLogicUIUtil {

    public static Texture getWhiteTexture() {
        return getTexture(Color.WHITE);
    }

    public static Texture getTexture(Color color) {
        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(color);
        linePixmap.fill();
        return new Texture(linePixmap);
    }

    public static int getInfoSizeFactor(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        int maxNumbers = 1;
        for (List<Integer> row : rowData) {
            if (row.size() > maxNumbers) {
                maxNumbers = row.size();
            }
        }
        for (List<Integer> column : colData) {
            if (column.size() > maxNumbers) {
                maxNumbers = column.size();
            }
        }
        return (int) Math.ceil(maxNumbers / 2d);
    }

    public static int getBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 10)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static int getIconBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 20)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static Vector2 getLevelSize(PixelLogicLevel level) {
        PixelLogicUILevelResolution resolution = get(level);
        int infoSize = PixelLogicUIUtil.getInfoSizeFactor(level);
        float x = (level.getColumns() + infoSize) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        float y = (level.getRows() + infoSize) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        return new Vector2(x, y);
    }

    public static Vector2 getBoardSize(PixelLogicLevel level) {
        PixelLogicUILevelResolution resolution = get(level);
        float x = level.getColumns() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        float y = level.getRows() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        return new Vector2(x, y);
    }

    public static PixelLogicUILevelResolution get(PixelLogicLevel level) {
        int size = PixelLogicUIUtil.getInfoSizeFactor(level);
        int columns = level.getColumns() + size;
        int rows = level.getRows() + size;

        float levelWidth = Gdx.graphics.getWidth();
        float levelHeight = getLevelMaxHeight();

        float pixelPerColumn = MathUtils.floor(levelWidth / (float) columns);
        float pixelPerRow = MathUtils.floor(levelHeight / (float) rows);

        float maxPixel = Math.min(pixelPerColumn, pixelPerRow);
        float spaceSize = MathUtils.floor(maxPixel / 17);
        float pixelSize = maxPixel - spaceSize;

        return new PixelLogicUILevelResolution((int) pixelSize, (int) spaceSize);
    }

    public static int getToolbarHeight() {
        int baseHeight = PixelLogicUIUtil.getIconBaseHeight() * 2;
        return Math.max(baseHeight, BASE_SIZE * 2);
    }

    public static int getToolbarPaddingTop() {
        return getToolbarHeight() / 10;
    }

    public static int getLevelMaxHeight() {
        int screenHeight = Gdx.graphics.getHeight();
        return screenHeight - getToolbarHeight() - getToolbarPaddingTop();
    }

    public static String formatMilliseconds(Long time) {
        long inSeconds = time / 1000;
        long minutes = inSeconds / 60;
        long seconds = inSeconds % 60;
        return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds);
    }

}
