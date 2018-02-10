package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicGUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.BASE_SIZE;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUILevelResolutionManager {

    private static PixelLogicGUILevelResolutionManager INSTANCE;

    private Map<String, BitmapFont> fonts;

    private int width, height;

    private PixelLogicGUILevelResolutionManager() {
        this.fonts = new HashMap<String, BitmapFont>();
        this.width = Gdx.graphics.getWidth();
        this.height = Gdx.graphics.getHeight();
    }

    public static PixelLogicGUILevelResolutionManager instance() {
        if (INSTANCE == null) {
            INSTANCE = new PixelLogicGUILevelResolutionManager();
        }
        return INSTANCE;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Vector2 getLevelSize(PixelLogicLevel level) {
        PixelLogicGUILevelResolution resolution = get(level);
        int infoSize = PixelLogicGUIUtil.getInfoSizeFactor(level);
        float x = (level.getColumns() + infoSize) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        float y = (level.getRows() + infoSize) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        return new Vector2(x, y);
    }

    public Vector2 getBoardSize(PixelLogicLevel level) {
        PixelLogicGUILevelResolution resolution = get(level);
        float x = level.getColumns() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        float y = level.getRows() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        return new Vector2(x, y);
    }

    public PixelLogicGUILevelResolution get(PixelLogicLevel level) {
        int size = PixelLogicGUIUtil.getInfoSizeFactor(level);
        int columns = level.getColumns() + size;
        int rows = level.getRows() + size;

        float pixelPerColumn = MathUtils.floor((float) width / (float) columns);
        float pixelPerRow = MathUtils.floor((float) height / (float) rows);

        float maxPixel = Math.min(pixelPerColumn, pixelPerRow);
        float spaceSize = MathUtils.floor(maxPixel / 17);
        float pixelSize = maxPixel - spaceSize;

        BitmapFont font = getFont(pixelSize, TEXT_COLOR);

        return new PixelLogicGUILevelResolution((int) pixelSize, (int) spaceSize, font);
    }

    public BitmapFont getFont(float pixelSize, Color color) {
        int fontSize = MathUtils.floor(pixelSize / 8f) * 8;
        String hash = pixelSize + "_" + color.toString();
        BitmapFont font = fonts.get(hash);
        if (font == null) {
            font = buildFont(fontSize, color);
            this.fonts.put(hash, font);
        }
        return font;
    }

    public int getBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 10)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public int getIconBaseHeight() {
        int baseHeight = MathUtils.floor(Gdx.graphics.getHeight() / (BASE_SIZE * 20)) * BASE_SIZE;
        return Math.max(BASE_SIZE, baseHeight);
    }

    public BitmapFont buildFont(int fontSize, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ObelusCompact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = fontSize;
        params.color = color;
        params.flip = true;
        params.mono = true;
        try {
            return generator.generateFont(params);
        } finally {
            generator.dispose();
        }
    }

    public void dispose() {
        for (BitmapFont font : fonts.values()) {
            font.dispose();
        }
    }

}
