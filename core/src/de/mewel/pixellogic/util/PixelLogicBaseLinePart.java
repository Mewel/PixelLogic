package de.mewel.pixellogic.util;

public class PixelLogicBaseLinePart {

    protected Boolean[] line;

    protected int index;

    protected int start;

    protected int end;

    public PixelLogicBaseLinePart(Boolean[] line, int start, int end) {
        this.line = line;
        this.start = start;
        this.end = end;
    }

    public Boolean[] getLine() {
        return line;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setLine(Boolean[] line) {
        this.line = line;
    }

}
