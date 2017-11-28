package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;

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

    public PixelLogicGUILevelResolution get(PixelLogicLevel level) {
        int columns = level.getColumns() + 2;
        int rows = level.getRows() + 2;

        float pixelPerColumn = MathUtils.floor((float) width / (float) columns);
        float pixelPerRow = MathUtils.floor((float) height / (float) rows);

        float maxPixel = Math.min(pixelPerColumn, pixelPerRow);
        float spaceSize = MathUtils.floor(maxPixel / 17);
        float pixelSize = maxPixel - spaceSize;

        BitmapFont font = getFont(pixelSize);

        //Gdx.app.log("res", "font " + font.getLineHeight());

        return new PixelLogicGUILevelResolution((int) pixelSize, (int) spaceSize, font);
    }

    private BitmapFont getFont(float pixelSize) {
        int fontSize = MathUtils.floor(pixelSize / 8f) * 8;
        BitmapFont font = fonts.get(fontSize);
        if (font == null) {
            font = buildFont(fontSize);
            this.fonts.put(fontSize, font);
        }
        return font;
    }

    private BitmapFont buildFont(int fontSize) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ObelusCompact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = fontSize;
        params.color = TEXT_COLOR;
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
