package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicSolver2 {

    public Boolean[][] solve(List<List<Integer>> rowNumbers, List<List<Integer>> colNumbers) {
        int rows = rowNumbers.size();
        int cols = colNumbers.size();

        Boolean[][] level = new Boolean[rows][cols];

        boolean changed;
        do {
            changed = false;
            for (int col = 0; col < cols; col++) {
                Boolean[] line = columnToLine(level, col);
                if (checkLine(line, colNumbers.get(col))) {
                    for (int row = 0; row < level.length; row++) {
                        level[row][col] = line[row];
                    }
                    changed = true;
                }
            }
            for (int row = 0; row < rows; row++) {
                if (checkLine(level[row], rowNumbers.get(row))) {
                    changed = true;
                }
            }
        } while (changed);
        return level;
    }

    private boolean checkLine(Boolean[] line, List<Integer> numbers) {

        List<PixelLogicLinePart> parts = new ArrayList<PixelLogicLinePart>();

        // fill pixel
        for (int numberIndex = 0; numberIndex < numbers.size(); numberIndex++) {
            List<Integer> left = new ArrayList<Integer>(numbers.subList(0, numberIndex));
            List<Integer> right = new ArrayList<Integer>(numbers.subList(numberIndex + 1, numbers.size()));
            int start = getNumberLength(left) + left.size();
            int end = line.length - (getNumberLength(right) + right.size());
            int pixelsToAdd = numbers.get(numberIndex);
            parts.add(new PixelLogicLinePart(line, numberIndex, start, end, pixelsToAdd));
        }

        // update boundaries (start end values)
        boolean changed;
        do {
            changed = false;
            for (PixelLogicLinePart part : parts) {
                if (part.updateBoundaries(parts)) {
                    changed = true;
                }
            }
        } while (changed);

        // fill pixels in line

        return false;
    }

    private Boolean[] columnToLine(Boolean[][] level, int col) {
        Boolean[] column = new Boolean[level.length];
        for (int row = 0; row < level.length; row++) {
            column[row] = level[row][col];
        }
        return column;
    }

    private int getNumberLength(List<Integer> numbers) {
        int amount = 0;
        for (Integer number : numbers) {
            amount += number;
        }
        return amount;
    }

}
