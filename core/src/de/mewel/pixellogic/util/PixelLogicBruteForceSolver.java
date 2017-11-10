package de.mewel.pixellogic.util;

import java.util.List;

public class PixelLogicBruteForceSolver {

    public Boolean[] solveLine(Boolean[] line, List<Integer> numbers) {
        return bruteForceLine(line, numbers, 0);
    }

    public Boolean[] bruteForceLine(Boolean[] line, List<Integer> numbers, int startIndex) {
        if (PixelLogicUtil.countPixel(line) >= PixelLogicUtil.countPixel(numbers)) {
            return line;
        }
        Boolean[] mergedLine = line;
        for (int amount = 1; amount < line.length; amount++) {
            for (int index = startIndex; index < line.length - amount; index++) {
                Boolean[] newLine = fillPixel(line, index, amount);
                if (PixelLogicUtil.isSolved(newLine, numbers)) {
                    mergedLine = mergeLines(mergedLine, newLine);
                } else if (index + 1 < line.length) {
                    newLine = bruteForceLine(newLine, numbers, index + 1);
                    if (PixelLogicUtil.isSolved(newLine, numbers)) {
                        mergedLine = mergedLine == null ? newLine : mergeLines(mergedLine, newLine);
                    }
                }
            }
        }
        return mergedLine;
    }

    private Boolean[] mergeLines(Boolean[] line1, Boolean[] line2) {
        Boolean[] mergedLine = new Boolean[line1.length];
        for (int lineIndex = 0; lineIndex < line1.length; lineIndex++) {
            if (line1[lineIndex] == null || line2[lineIndex] == null) {
                continue;
            }
            if (line1[lineIndex] == line2[lineIndex]) {
                mergedLine[lineIndex] = line1[lineIndex];
            }
        }
        return mergedLine;
    }

    private Boolean[] fillPixel(Boolean[] line, int index, int amount) {
        return addPixel(line, index, amount, true);
    }

    private Boolean[] blockPixel(Boolean[] line, int index, int amount) {
        return addPixel(line, index, amount, false);
    }

    private Boolean[] addPixel(Boolean[] line, int index, int amount, boolean value) {
        Boolean[] copy = copyLine(line);
        for (int i = index; i < index + amount; i++) {
            if (line[i] == null) {
                copy[i] = value;
            }
        }
        return copy;
    }

    private static Boolean[] copyLine(Boolean[] line) {
        Boolean[] copy = new Boolean[line.length];
        System.arraycopy(line, 0, copy, 0, line.length);
        return copy;
    }

}
