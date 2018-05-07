package de.mewel.pixellogic.ui.level.animation;

import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public abstract class PixelLogicUILevelAnimation {

    private PixelLogicUILevel levelUI;

    public PixelLogicUILevelAnimation(PixelLogicUILevel levelUI) {
        this.levelUI = levelUI;
    }

    public void execute() {
        PixelLogicUIBoardPixel[][] pixels = this.levelUI.getBoard().getPixels();
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                animatePixel(pixels[row][col]);
            }
        }
        animateRowGroup(levelUI.getRowGroup());
        animateColumnGroup(levelUI.getColumnGroup());
    }

    protected abstract void animatePixel(PixelLogicUIBoardPixel pixel);

    protected abstract void animateRowGroup(PixelLogicUIRowGroup group);

    protected abstract void animateColumnGroup(PixelLogicUIColumnGroup group);

    public PixelLogicUILevel getLevelUI() {
        return levelUI;
    }

}
