package de.mewel.pixellogic.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelResolution;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BASE_SIZE;

public class PixelLogicUIUtil {

    private static final DateFormat SDF = SimpleDateFormat.getDateInstance();

    public static Texture getWhiteTexture() {
        return getTexture(Color.WHITE);
    }

    public static Texture getTexture(Color color) {
        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(color);
        linePixmap.fill();
        return new Texture(linePixmap);
    }

    /**
     * Calculates the maximum numbers of both info sidebars.
     *
     * @param level the level
     * @return [0] = max numbers on the rows; [1] = max numbers on the columns
     */
    public static int[] getMaxInfoNumbers(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        int maxRowNumbers = 1;
        int maxColNumbers = 1;
        for (List<Integer> row : rowData) {
            if (row.size() > maxRowNumbers) {
                maxRowNumbers = row.size();
            }
        }
        for (List<Integer> column : colData) {
            if (column.size() > maxColNumbers) {
                maxColNumbers = column.size();
            }
        }
        return new int[]{maxRowNumbers, maxColNumbers};
    }

    public static int getBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 10)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static int getIconBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 20)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static void fixLabelHeight(Label label, float width) {
        label.pack();
        label.setWidth(width);
        label.pack();
        label.setWidth(width);
    }

    public static BitmapFont getAppFont(PixelLogicAssets assets, int size) {
        float baseSize = Gdx.graphics.getHeight() * 0.05f;
        float fontSize = baseSize + (size * (baseSize / 6));
        return assets.getGameFont((int) fontSize);
    }

    public static PixelLogicUILevelResolution get(PixelLogicLevel level) {
        int maxWidth = Gdx.graphics.getWidth();
        int maxHeight = PixelLogicUIUtil.getLevelMaxHeight();
        return new PixelLogicUILevelResolution(level, maxWidth, maxHeight);
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
        return screenHeight - (getToolbarHeight() + getToolbarPaddingTop());
    }

    public static String formatDate(Long milliseconds) {
        return SDF.format(new Date(milliseconds));
    }

    public static String formatMilliseconds(Long time) {
        long inSeconds = time / 1000;
        long minutes = inSeconds / 60;
        long seconds = inSeconds % 60;
        return String.format(Locale.ROOT, "%02d:%02d", minutes, seconds);
    }

}
