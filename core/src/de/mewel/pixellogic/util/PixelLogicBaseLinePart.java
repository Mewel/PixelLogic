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

    /**
     * Updates the start and the end value if the new values are greater or lower than the
     * current ones.
     *
     * @param newStart the new start, has to be greated than the current start to be accepted
     * @param newEnd   the new end, has to be lower than the current end to be accepted
     * @return true if one of start or end has changed
     */
    protected boolean updateStartAndEnd(int newStart, int newEnd) {
        boolean changed = false;
        if (this.start < newStart) {
            this.start = newStart;
            changed = true;
        }
        if (this.end > newEnd) {
            this.end = newEnd;
            changed = true;
        }
        return changed;
    }

    protected boolean fit(int pixels) {
        return pixels <= (this.end - this.start);
    }

}
