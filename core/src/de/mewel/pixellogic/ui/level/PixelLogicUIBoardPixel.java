package de.mewel.pixellogic.ui.level;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUIBoardPixel extends PixelLogicUIColoredSurface {

    private Boolean value;

    private int row, col;

    public PixelLogicUIBoardPixel(PixelLogicGlobal global, int row, int col) {
        super(global);
        this.row = row;
        this.col = col;
        setColor(getStyle().getPixelEmptyColor());
    }

    public void set(Boolean value) {
        this.value = value;
        updateColor();
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        this.updateColor();
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    private void updateColor() {
        setColor(this.value == null ? getStyle().getPixelEmptyColor() :
                (this.value ? getStyle().getPixelFilledColor() :
                        getStyle().getPixelBlockedColor()));
    }

}
