package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUILevelResolutionManager {

    private static PixelLogicGUILevelResolutionManager INSTANCE;

    private Map<Integer, BitmapFont> fonts;

    private int width, height;

    private PixelLogicGUILevelResolutionManager() {
        this.fonts = new HashMap<Integer, BitmapFont>();
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
        float x = (level.getColumns() + 2) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        float y = (level.getRows() + 2) * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        return new Vector2(x, y);
    }

    public PixelLogicGUILevelResolution get(PixelLogicLevel level) {
        int columns = level.getColumns() + 2;
        int rows = level.getRows() + 2;

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
        BitmapFont font = fonts.get(fontSize);
        if (font == null) {
            font = buildFont(fontSize, color);
            this.fonts.put(fontSize, font);
        }
        return font;
    }

    public int getBaseHeight() {
        return MathUtils.floor(Gdx.graphics.getHeight() / 240f) * 24;
    }

    private BitmapFont buildFont(int fontSize, Color color) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ObelusCompact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = fontSize;
        params.color = color;
        params.flip = true;
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
