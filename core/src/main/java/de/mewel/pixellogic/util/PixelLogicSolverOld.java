package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicSolverOld {

    public PixelLogicSolverResult solve(List<List<Integer>> rowNumbers, List<List<Integer>> colNumbers) {
        int rows = rowNumbers.size();
        int cols = colNumbers.size();

        Boolean[][] level = new Boolean[rows][cols];

        int complexity = 0;
        boolean changed;
        do {
            changed = false;
            for (int col = 0; col < cols; col++) {
                Boolean[] line = PixelLogicUtil.columnToLine(level, col);
                if (solveLine(line, colNumbers.get(col))) {
                    for (int row = 0; row < level.length; row++) {
                        level[row][col] = line[row];
                    }
                    changed = true;
                }
            }
            for (int row = 0; row < rows; row++) {
                if (solveLine(level[row], rowNumbers.get(row))) {
                    changed = true;
                }
            }
            complexity++;
        } while (changed);
        return new PixelLogicSolverResult(level, complexity);
    }

    boolean solveLine(Boolean[] line, List<Integer> numbers) {
        // if line is solved -> fill null pixel's
        if (PixelLogicUtil.isSolved(line, numbers)) {
            return blockRestOfLine(line);
        }
        // brute force
        List<Boolean[]> lines = bruteForceLine(line, numbers, 0);
        Boolean[] newLine = mergeLines(lines);
        if (newLine != null && PixelLogicUtil.differs(line, newLine)) {
            System.arraycopy(newLine, 0, line, 0, newLine.length);
            return true;
        }
        return false;
    }

    private List<Boolean[]> bruteForceLine(Boolean[] line, List<Integer> numbers, int startIndex) {
        List<Boolean[]> lines = new ArrayList<>();
        if (PixelLogicUtil.countPixel(line) >= PixelLogicUtil.countPixel(numbers)) {
            return lines;
        }
        for (int index = startIndex; index < line.length; index++) {
            Boolean[] newLine = fillPixel(line, index);
            if (PixelLogicUtil.isSolved(newLine, numbers)) {
                lines.add(newLine);
            } else if (index + 1 < line.length) {
                lines.addAll(bruteForceLine(newLine, numbers, index + 1));
            }
        }
        return lines;
    }

    private Boolean[] mergeLines(List<Boolean[]> lines) {
        if (lines.isEmpty()) {
            return null;
        }
        if (lines.size() == 1) {
            return lines.get(0);
        }
        Boolean[] firstLine = lines.get(0);
        Boolean[] mergedLine = new Boolean[firstLine.length];
        boolean alwaysTrue;
        boolean alwaysNullOrFalse;
        for (int lineIndex = 0; lineIndex < firstLine.length; lineIndex++) {
            alwaysTrue = true;
            alwaysNullOrFalse = true;
            for (Boolean[] line : lines) {
                if (line[lineIndex] != null && line[lineIndex]) {
                    alwaysNullOrFalse = false;
                }
                if (line[lineIndex] == null || !line[lineIndex]) {
                    alwaysTrue = false;
                }
                if (!alwaysTrue && !alwaysNullOrFalse) {
                    break;
                }
            }
            if (alwaysTrue) {
                mergedLine[lineIndex] = true;
            } else if (alwaysNullOrFalse) {
                mergedLine[lineIndex] = false;
            }

            //mergedLine[lineIndex] = (alwaysTrue ? true : (alwaysNullOrFalse ? false : null));
        }
        return mergedLine;
    }

    private Boolean[] fillPixel(Boolean[] line, int index) {
        Boolean[] copy = copyLine(line);
        if (line[index] == null) {
            copy[index] = true;
        }
        return copy;
    }

    private boolean blockRestOfLine(Boolean[] line) {
        boolean changed = false;
        for (int i = 0; i < line.length; i++) {
            if (line[i] == null) {
                line[i] = false;
                changed = true;
            }
        }
        return changed;
    }

    private static Boolean[] copyLine(Boolean[] line) {
        Boolean[] copy = new Boolean[line.length];
        System.arraycopy(line, 0, copy, 0, line.length);
        return copy;
    }

}
