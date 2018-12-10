package de.mewel.pixellogic.ui.level.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;

public class PixelLogicBoardChangedEvent extends PixelLogicEvent {

    private PixelLogicLevel level;

    private int row, col;

    private Boolean value;

    /**
     * A particular pixel changed
     *
     * @param source
     * @param level
     * @param row
     * @param col
     * @param value
     */
    public PixelLogicBoardChangedEvent(PixelLogicUILevel source, PixelLogicLevel level, int row, int col, Boolean value) {
        super(source);
        this.level = level;
        this.row = row;
        this.col = col;
        this.value = value;
    }

    /**
     * The whole level changed
     *
     * @param source
     * @param level
     */
    public PixelLogicBoardChangedEvent(PixelLogicUILevel source, PixelLogicLevel level) {
        super(source);
        this.level = level;
        this.row = -1;
        this.col = -1;
        this.value = null;
    }

    public PixelLogicLevel getLevel() {
        return level;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Boolean getValue() {
        return value;
    }

    /**
     * Checks if the whole level changed or just one pixel.
     *
     * @return true if a particular pixel has changed
     */
    public boolean isPixelChanged() {
        return this.col != -1 && this.row != -1;
    }

}
