package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.PixelLogicConstants.PIXEL_BLOCKED_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.PIXEL_EMPTY_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.PIXEL_FILLED_COLOR;

public class PixelLogicUIBoardPixel extends PixelLogicUIColoredSurface {

    private int row, col;

    public PixelLogicUIBoardPixel(PixelLogicAssets assets, int row, int col) {
        super(assets);
        this.row = row;
        this.col = col;
        setColor(new Color(PIXEL_EMPTY_COLOR));
    }

    public void set(Boolean value) {
        setColor(value == null ? new Color(PIXEL_EMPTY_COLOR) :
                (value ? new Color(PIXEL_FILLED_COLOR) :
                        new Color(PIXEL_BLOCKED_COLOR)));
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

}
