package de.mewel.pixellogic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public class PixelLogicUtil {

    public static List<List<Integer>> getRowData(Boolean[][] level) {
        List<List<Integer>> rowData = new ArrayList<List<Integer>>();
        int rows = level.length;
        for (int row = 0; row < rows; row++) {
            rowData.add(getNumbersOfRow(level, row));
        }
        return rowData;
    }

    public static List<List<Integer>> getColumnData(Boolean[][] level) {
        List<List<Integer>> colData = new ArrayList<List<Integer>>();
        int cols = level[0].length;
        for (int col = 0; col < cols; col++) {
            colData.add(getNumbersOfCol(level, col));
        }
        return colData;
    }

    public static List<Integer> getNumbersOfRow(Boolean[][] level, int indexOfRow) {
        Boolean[] row = getRow(level, indexOfRow);
        return getConnectedPixel(row);
    }

    public static List<Integer> getNumbersOfCol(Boolean[][] level, int indexOfColumn) {
        Boolean[] column = getColumn(level, indexOfColumn);
        return getConnectedPixel(column);
    }

    public static Boolean[] getRow(Boolean[][] level, int row) {
        int cols = level[0].length;
        Boolean[] line = new Boolean[cols];
        System.arraycopy(level[row], 0, line, 0, cols);
        return line;
    }

    public static Boolean[] getColumn(Boolean[][] level, int column) {
        int rows = level.length;
        Boolean[] line = new Boolean[rows];
        for (int row = 0; row < rows; row++) {
            line[row] = level[row][column];
        }
        return line;
    }

    public static List<Integer> getConnectedPixel(Boolean[] line) {
        List<Integer> numbers = new ArrayList<Integer>();
        int consecutive = 0;
        for (Boolean pixel : line) {
            if (pixel != null && pixel) {
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

    public static boolean isSolved(Boolean[] line, List<Integer> numbers) {
        if (line == null) {
            return false;
        }
        List<Integer> connectedPixels = getConnectedPixel(line);
        return compareNumberLists(connectedPixels, numbers);
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

    public static Boolean[][] toLevelData(Integer[][] imageData) {
        int rows = imageData.length;
        int cols = imageData[0].length;
        Boolean[][] level = new Boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int value = imageData[row][col];
                level[row][col] = new Color(value).a == 1.0f;
            }
        }
        return level;
    }

    public static Boolean[][] sampleLevel() {
        return new Boolean[][]{
                {true, true, true, false, true},
                {false, false, false, false, false},
                {false, true, true, false, false},
                {true, false, false, false, true},
                {true, true, true, false, true}
        };
    }

    public static Boolean[][] invalidSampleLevel() {
        return new Boolean[][]{
                {true, true, true, false, true},
                {false, false, false, false, false},
                {false, true, true, false, false},
                {true, false, false, false, true},
                {true, true, false, false, true}
        };
    }

    public static PixelLogicLevel sampleImageLevel() {
        int red = Color.RED.toIntBits();
        return new PixelLogicLevel(null, new Integer[][]{
                {red, red, red, 0, red},
                {0, 0, 0, 0, 0},
                {0, red, red, 0, 0},
                {red, 0, 0, 0, red},
                {red, red, 0, 0, red}
        });
    }

    public static void validateLevel(PixelLogicLevel level) {
        PixelLogicLevelValidator validator = new PixelLogicLevelValidator();
        Gdx.app.log("validate level", level.getName() + " is " + (validator.validate(level.getLevelData()) ? "valid" : "invalid"));
    }

    public static void solveLevel(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        Boolean[][] solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();
        level.setPixels(solvedLevel);
    }

}
