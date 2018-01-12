package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.g2d.BitmapFont;

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
