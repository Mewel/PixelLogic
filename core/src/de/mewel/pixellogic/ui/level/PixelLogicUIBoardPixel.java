package de.mewel.pixellogic.ui.level;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_BLOCKED_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_EMPTY_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_FILLED_COLOR;

public class PixelLogicUIBoardPixel extends PixelLogicUIColoredSurface {

    private int row, col;

    public PixelLogicUIBoardPixel(PixelLogicAssets assets, PixelLogicEventManager eventManager, int row, int col) {
        super(assets, eventManager);
        this.row = row;
        this.col = col;
        setColor(PIXEL_EMPTY_COLOR);
    }

    public void set(Boolean value) {
        setColor(value == null ? PIXEL_EMPTY_COLOR : (value ? PIXEL_FILLED_COLOR : PIXEL_BLOCKED_COLOR));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
