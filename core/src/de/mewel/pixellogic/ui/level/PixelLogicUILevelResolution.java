package de.mewel.pixellogic.ui.level;

public class PixelLogicUILevelResolution {

    private int gamePixelSize;

    private int gameSpaceSize;

    public PixelLogicUILevelResolution(int gamePixelSize, int gameSpaceSize) {
        this.gamePixelSize = gamePixelSize;
        this.gameSpaceSize = gameSpaceSize;
    }

    public int getGamePixelSize() {
        return gamePixelSize;
    }

    public int getGameSpaceSize() {
        return gameSpaceSize;
    }

    public int getGamePixelSizeCombined() {
        return gamePixelSize + gameSpaceSize;
    }

}
