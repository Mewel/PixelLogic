package de.mewel.pixellogic.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyleable;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.PixelLogicConstants.BASE_SIZE;

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
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 10f)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static int getIconBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 20f)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public static BitmapFont getAppFont(PixelLogicAssets assets, int size) {
        return assets.getGameFont(size);
    }

    public static BitmapFont getMainFont(PixelLogicAssets assets) {
        return assets.getMainFont();
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

    public static void fixLabelHeight(Label label, float width) {
        label.pack();
        label.setWidth(width);
        label.pack();
        label.setWidth(width);
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

    public static boolean equalLabelStyle(Label.LabelStyle l1, Label.LabelStyle l2) {
        if (l1 == null || l2 == null) {
            return l1 == null && l2 == null;
        }
        if (!l1.fontColor.equals(l2.fontColor)) {
            return false;
        }
        return l1.font == l2.font;
    }

    public static Action blinkAction(Color color, float duration) {
        Color brighter = new Color(color).mul(1.4f);
        return Actions.forever(Actions.sequence(
                Actions.color(brighter, duration),
                Actions.color(color, duration)));
    }

    /**
     * Determine if the device is a desktop (i.e. it has a large screen).
     */
    public static boolean isDesktop() {
        return Application.ApplicationType.Desktop.equals(Gdx.app.getType());
    }

    public static boolean isTablet() {
        return getScreenSizeInches() > 6f;
    }

    public static double getScreenSizeInches() {
        // Use the primary monitor as baseline
        // It would also be possible to get the monitor where the window is displayed
        Graphics.Monitor primary = Gdx.graphics.getPrimaryMonitor();
        Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(primary);

        float dpi = 160 * Gdx.graphics.getDensity();
        float widthInches = displayMode.width / dpi;
        float heightInches = displayMode.height / dpi;

        //Use the pythagorean theorem to get the diagonal screen size
        return Math.sqrt(Math.pow(widthInches, 2) + Math.pow(heightInches, 2));
    }

    public static void changeStyle(Actor actor, PixelLogicUIStyle style) {
        if (actor instanceof PixelLogicUIStyleable) {
            ((PixelLogicUIStyleable) actor).styleChanged(style);
        }
        if (actor instanceof Group) {
            for (Actor child : ((Group) actor).getChildren()) {
                changeStyle(child, style);
            }
        }
    }

}
