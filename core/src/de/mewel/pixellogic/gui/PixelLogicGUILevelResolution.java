package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

/**
 * Created by mewel on 25.11.2017.
 */

public class PixelLogicGUILevelResolution {

    private int resolution;

    private int gamePixelSize;

    private int gameSpaceSize;

    private int gameFontSize;

    private BitmapFont gameFont;

    public PixelLogicGUILevelResolution(int resolution, int gamePixelSize, int gameSpaceSize, int gameFontSize) {
        this.resolution = resolution;
        this.gamePixelSize = gamePixelSize;
        this.gameSpaceSize = gameSpaceSize;
        this.gameFontSize = gameFontSize;
        this.gameFont = null;
    }

    public int getResolution() {
        return resolution;
    }

    public int getGamePixelSize() {
        return gamePixelSize;
    }

    public int getGameSpaceSize() {
        return gameSpaceSize;
    }

    public int getGameFontSize() {
        return gameFontSize;
    }

    public int getGamePixelSizeCombined() {
        return gamePixelSize + gameSpaceSize;
    }

    public BitmapFont getGameFont() {
        if (gameFont != null) {
            return gameFont;
        }
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ObelusCompact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = gameFontSize;
        params.color = TEXT_COLOR;
        params.flip = true;
        try {
            return gameFont = generator.generateFont(params);
        } finally {
            generator.dispose();
        }
    }

    public void dispose() {
        if (gameFont != null) {
            gameFont.dispose();
        }
    }

}
