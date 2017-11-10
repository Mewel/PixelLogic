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

    public boolean updateBoundaries(List<PixelLogicLinePart> parts) {
        PixelLogicLinePart leftPart = index > 0 ? parts.get(index - 1) : null;
        PixelLogicLinePart rightPart = index < (parts.size() - 1) ? parts.get(index + 1) : null;
        boolean changed = false;
        if (this.updateBoundariesByPixelFitting()) {
            changed = true;
        }
        if (this.updateBoundariesByLeftPart(leftPart)) {
            changed = true;
        }
        if (this.updateBoundariesByRightPart(rightPart)) {
            changed = true;
        }
        if (this.updateBoundariesByPixelFitting()) {
            changed = true;
        }
        if(this.updateBoundariesByPartIndex(parts)) {
            changed = true;
        }
        // if( this.update)
        return changed;
    }

    protected boolean updateBoundariesByLeftPart(PixelLogicLinePart leftPart) {
        if (leftPart == null) {
            return false;
        }
        return updateStartAndEnd(leftPart.start + leftPart.pixelToFill + 1, this.end);
    }

    protected boolean updateBoundariesByRightPart(PixelLogicLinePart rightPart) {
        if (rightPart == null) {
            return false;
        }
        return updateStartAndEnd(this.start, rightPart.end - (rightPart.pixelToFill + 1));
    }

    protected boolean updateBoundariesByPixelFitting() {
        int newStart = fitFirst(line, this.start, this.end, this.pixelToFill);
        int newEnd = line.length - fitFirst(PixelLogicUtil.invert(line),
                line.length - end, line.length - start, this.pixelToFill);
        return updateStartAndEnd(newStart, newEnd);
    }

    protected int fitFirst(Boolean[] line, int start, int end, int pixel) {
        for (int lineIndex = start; lineIndex < (end - pixel); lineIndex++) {
            boolean fitSubLine = true;
            for (int subLineIndex = lineIndex; subLineIndex < (lineIndex + pixel); subLineIndex++) {
                if (line[subLineIndex] != null && !line[subLineIndex]) {
                    fitSubLine = false;
                    break;
                }
            }
            if (fitSubLine) {
                return lineIndex;
            }
        }
        throw new IllegalArgumentException("Does not fit at all. This should never happen!");
    }

    private boolean updateBoundariesByPartIndex(List<PixelLogicLinePart> parts) {
        return false;
    }

    private Integer getPartOffset(List<Integer> base, List<Integer> variant) {
        int tries = base.size() - variant.size();
        if(tries < 0) {
            return null;
        }
        if(tries == 0) {
            return 0;
        }
        Integer foundOffset = null;
        for(int offset = 0; offset < tries; offset++) {
            boolean valid = true;
            for(int variantIndex = 0; variantIndex < variant.size(); variantIndex++) {
                int diff = base.get(variantIndex + offset) - variant.get(variantIndex);
                if(diff < 0) {
                    valid = false;
                    break;
                }
            }
            if(valid) {
                if(foundOffset == null) {
                    foundOffset = offset;
                } else {
                    // there are multiple possible offsets, we don't know which one is valid
                    foundOffset = null;
                    break;
                }
            }
        }
        return foundOffset;
    }
}
