package de.mewel.pixellogic.util;

import java.util.List;

public class PixelLogicLinePart extends PixelLogicBaseLinePart {

    private int index;

    private int pixelToFill;

    public PixelLogicLinePart(Boolean[] line, int index, int start, int end, int pixelToFill) {
        super(line, start, end);
        this.index = index;
        this.pixelToFill = pixelToFill;
    }

    public int getPixelToFill() {
        return pixelToFill;
    }

    public int getIndex() {
        return index;
    }

    public void setPixelToFill(int pixelToFill) {
        this.pixelToFill = pixelToFill;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isComplete() {
        return end - start == pixelToFill;
    }

    public boolean updateLine(List<PixelLogicLinePart> parts) {
        return false;
    }

}
