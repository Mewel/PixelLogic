package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicUtil {

    public static List<List<Integer>> getRowData(boolean[][] level) {
        List<List<Integer>> rowData = new ArrayList<List<Integer>>();
        int rows = level.length;
        for (int row = 0; row < rows; row++) {
            rowData.add(getNumbersOfRow(level, row));
        }
        return rowData;
    }

    public static List<List<Integer>> getColumnData(boolean[][] level) {
        List<List<Integer>> colData = new ArrayList<List<Integer>>();
        int cols = level[0].length;
        for (int col = 0; col < cols; col++) {
            colData.add(getNumbersOfCol(level, col));
        }
        return colData;
    }

    public static List<Integer> getNumbersOfRow(boolean[][] level, int indexOfRow) {
        boolean[] row = getRow(level, indexOfRow);
        return getConnectedPixel(row);
    }

    public static List<Integer> getNumbersOfCol(boolean[][] level, int indexOfColumn) {
        boolean[] column = getColumn(level, indexOfColumn);
        return getConnectedPixel(column);
    }

    public static boolean[] getRow(boolean[][] level, int row) {
        int cols = level[0].length;
        boolean[] line = new boolean[cols];
        System.arraycopy(level[row], 0, line, 0, cols);
        return line;
    }

    public static boolean[] getColumn(boolean[][] level, int column) {
        int rows = level.length;
        boolean[] line = new boolean[rows];
        for (int row = 0; row < rows; row++) {
            line[row] = level[row][column];
        }
        return line;
    }

    public static boolean[] toLevelLine(Boolean[] line) {
        boolean[] levelLine = new boolean[line.length];
        for (int i = 0; i < line.length; i++) {
            levelLine[i] = line[i] != null && line[i];
        }
        return levelLine;
    }

    public static boolean[][] toLevel(Boolean[][] testLevel) {
        boolean[][] level = new boolean[testLevel.length][testLevel[0].length];
        for (int row = 0; row < testLevel.length; row++) {
            for (int col = 0; col < testLevel[0].length; col++) {
                level[row][col] = testLevel[row][col];
            }
        }
        return level;
    }

    public static List<Integer> getConnectedPixel(boolean[] line) {
        List<Integer> numbers = new ArrayList<Integer>();
        int consecutive = 0;
        for (boolean pixel : line) {
            if (pixel) {
                consecutive++;
                continue;
            }
            if (consecutive == 0) {
                continue;
            }
            numbers.add(consecutive);
            consecutive = 0;
        }
        if (consecutive != 0) {
            numbers.add(consecutive);
        }
        if (numbers.isEmpty()) {
            numbers.add(0);
        }
        return numbers;
    }

    public static boolean compareNumberLists(List<Integer> list1, List<Integer> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static boolean isValid(Boolean[][] level) {
        for (int col = 0; col < level[0].length; col++) {
            Boolean[] line = columnToLine(level, col);
            for (Boolean pixel : line) {
                if (pixel == null) {
                    return false;
                }
            }
        }
        for (Boolean[] line : level) {
            for (Boolean pixel : line) {
                if (pixel == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isSolved(boolean[] line, List<Integer> numbers) {
        List<Integer> connectedPixels = getConnectedPixel(line);
        return compareNumberLists(connectedPixels, numbers);
    }

    public static boolean isSolved(Boolean[] line, List<Integer> numbers) {
        if (line == null) {
            return false;
        }
        boolean levelLine[] = PixelLogicUtil.toLevelLine(line);
        return isSolved(levelLine, numbers);
    }

    public static int countPixel(List<Integer> numbers) {
        int amount = 0;
        for (Integer number : numbers) {
            amount += number;
        }
        return amount;
    }

    public static int countPixel(Boolean[] line) {
        int amount = 0;
        for (Boolean pixel : line) {
            if (pixel != null && pixel) {
                amount++;
            }
        }
        return amount;
    }

    public static Boolean[] columnToLine(Boolean[][] level, int columnIndex) {
        Boolean[] column = new Boolean[level.length];
        for (int row = 0; row < level.length; row++) {
            column[row] = level[row][columnIndex];
        }
        return column;
    }

    public static boolean differs(Boolean[] line, Boolean[] newLine) {
        for (int i = 0; i < line.length; i++) {
            if (line[i] != newLine[i]) {
                return true;
            }
        }
        return false;
    }

    public static boolean[][] sampleLevel() {
        return new boolean[][]{
                {true, true, true, false, true},
                {false, false, false, false, false},
                {false, true, true, false, false},
                {true, false, false, false, true},
                {true, true, true, false, true}
        };
    }

    public static boolean[][] invalidSampleLevel() {
        return new boolean[][]{
                {true, true, true, false, true},
                {false, false, false, false, false},
                {false, true, true, false, false},
                {true, false, false, false, true},
                {true, true, false, false, true}
        };
    }

}
