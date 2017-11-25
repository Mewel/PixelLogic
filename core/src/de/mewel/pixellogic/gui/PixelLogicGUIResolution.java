package de.mewel.pixellogic.gui;

/**
 * Created by mewel on 25.11.2017.
 */

public class PixelLogicGUIResolution {

    private int resolution;

    private int gamePixelSize;

    private int gameSpaceSize;

    private int gameFontSize;

    public PixelLogicGUIResolution(int resolution, int gamePixelSize, int gameSpaceSize, int gameFontSize) {
        this.resolution = resolution;
        this.gamePixelSize = gamePixelSize;
        this.gameSpaceSize = gameSpaceSize;
        this.gameFontSize = gameFontSize;
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

}
