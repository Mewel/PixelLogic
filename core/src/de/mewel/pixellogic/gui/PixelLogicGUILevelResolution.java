package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUILevelResolution {

    private int gamePixelSize;

    private int gameSpaceSize;

    private BitmapFont gameFont;

    public PixelLogicGUILevelResolution(int gamePixelSize, int gameSpaceSize, BitmapFont font) {
        this.gamePixelSize = gamePixelSize;
        this.gameSpaceSize = gameSpaceSize;
        this.gameFont = font;
    }

    public int getGamePixelSize() {
        return gamePixelSize;
    }

    public int getGameSpaceSize() {
        return gameSpaceSize;
    }

    public BitmapFont getGameFont() {
        return gameFont;
    }

    public int getGamePixelSizeCombined() {
        return gamePixelSize + gameSpaceSize;
    }

}
