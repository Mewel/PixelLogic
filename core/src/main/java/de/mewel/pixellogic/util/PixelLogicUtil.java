package de.mewel.pixellogic.util;

import com.badlogic.gdx.graphics.Color;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicUtil {

    public static List<List<Integer>> getRowData(Boolean[][] level) {
        List<List<Integer>> rowData = new ArrayList<>();
        int rows = level.length;
        for (int row = 0; row < rows; row++) {
            rowData.add(getNumbersOfRow(level, row));
        }
        return rowData;
    }

    public static List<List<Integer>> getColumnData(Boolean[][] level) {
        List<List<Integer>> colData = new ArrayList<>();
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
        List<Integer> numbers = new ArrayList<>();
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

    /**
     * Sorts a map by the value and return the keys as list in ascending order.
     *
     * @param map the map to sort
     * @param <K> the keys which will be returned as list
     * @param <V> the values which will be used to determine the order
     * @return a sorted list of keys
     */
    public static <K, V extends Comparable<? super V>> List<K> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());
        List<K> result = new ArrayList<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            result.add(entry.getKey());
        }
        return result;
    }

    public static boolean isSolved(Boolean[] line, List<Integer> numbers) {
        if (line == null) {
            return false;
        }
        int numberIndex = 0;
        Integer number = numberIndex < numbers.size() ? numbers.get(numberIndex) : null;
        int connected = -1;
        for (Boolean pixel : line) {
            if (pixel == null || !pixel) {
                if (number == null || connected == -1) {
                    continue;
                }
                if (connected != number) {
                    return false;
                }
                connected = -1;
                number = ++numberIndex < numbers.size() ? numbers.get(numberIndex) : null;
            } else {
                if (number == null) {
                    return false;
                }
                if (connected == -1) {
                    connected = 0;
                }
                if (++connected > number) {
                    return false;
                }
            }
        }
        if (number != null && number == connected) {
            numberIndex++;
        }
        return numberIndex == numbers.size();
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

    /**
     * Returns empty starting/ending rows/columns offset values.
     * <p>
     * top, bottom, left, right
     *
     * @param level the level offsets
     */
    public static Integer[] offsetLevelData(Boolean[][] level) {
        int top;
        int bottom;
        int left;
        int right;
        // top
        for (top = 0; top < level.length; top++) {
            Boolean[] row = PixelLogicUtil.getRow(level, top);
            if (PixelLogicUtil.countPixel(row) > 0) {
                break;
            }
        }
        // bottom
        for (bottom = 0; bottom < level.length; bottom++) {
            Boolean[] row = PixelLogicUtil.getRow(level, level.length - 1 - bottom);
            if (PixelLogicUtil.countPixel(row) > 0) {
                break;
            }
        }
        // left
        for (left = 0; left < level[0].length; left++) {
            Boolean[] column = PixelLogicUtil.getColumn(level, left);
            if (PixelLogicUtil.countPixel(column) > 0) {
                break;
            }
        }
        // right
        for (right = 0; right < level[0].length; right++) {
            Boolean[] column = PixelLogicUtil.getColumn(level, level[0].length - 1 - right);
            if (PixelLogicUtil.countPixel(column) > 0) {
                break;
            }
        }
        return new Integer[]{top, bottom, left, right};
    }

    public static Boolean[][] cutLevelData(Boolean[][] level, Integer[] offset) {
        int top = offset[0];
        int bottom = offset[1];
        int left = offset[2];
        int right = offset[3];
        Boolean[][] newLevel = new Boolean[level.length - (top + bottom)][level[0].length - (left + right)];
        for (int row = 0; row < newLevel.length; row++) {
            System.arraycopy(level[top + row], left, newLevel[row], 0, newLevel[0].length);
        }
        return newLevel;
    }

    public static Boolean[][] createRandomLevel(int rows, int cols) {
        boolean valid;
        Boolean[][] level;
        Random random = new Random();
        do {
            level = new Boolean[rows][cols];
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    level[row][col] = random.nextBoolean();
                }
            }
            valid = isSolvable(level);
        } while (!valid);
        return level;
    }

    public static Boolean[][] createRandomLevel(int rows, int cols, float minComplexity) {
        return createRandomLevel(rows, cols, minComplexity, -1);
    }

    public static Boolean[][] createRandomLevel(int rows, int cols, float minComplexity, float maxComplexity) {
        Boolean[][] randomLevel = createRandomLevel(rows, cols);
        PixelLogicSolverResult result = PixelLogicComplexityAnalyzer.analyze(randomLevel);
        float complexity = result.getComplexity();
        boolean found = complexity >= minComplexity && (maxComplexity == -1 || complexity <= maxComplexity);
        int variations = rows * cols / 8;
        while (!found) {
            Boolean[][] newLevel = alterLevel(randomLevel, variations);
            result = PixelLogicComplexityAnalyzer.analyze(newLevel);
            float newComplexity = result.getComplexity();
            found = newComplexity >= minComplexity && (maxComplexity == -1 || newComplexity <= maxComplexity);
            if (found || newComplexity == complexity ||
                    (newComplexity > complexity && newComplexity < minComplexity) ||
                    (newComplexity < complexity && maxComplexity != -1 && newComplexity > maxComplexity)) {
                randomLevel = newLevel;
                complexity = newComplexity;
            }
        }
        //Gdx.app.log("crl", "" + complexity);
        return randomLevel;
    }

    private static Boolean[][] alterLevel(Boolean[][] level, int variations) {
        Random random = new Random();
        Boolean[][] newLevel = cloneLevel(level);
        boolean valid;
        do {
            for (int i = 0; i < variations; i++) {
                int row = random.nextInt(newLevel.length);
                int col = random.nextInt(newLevel[0].length);
                newLevel[row][col] = random.nextBoolean();
            }
            valid = isSolvable(newLevel);
        } while (!valid);
        return newLevel;
    }

    public static Boolean[][] cloneLevel(Boolean[][] level) {
        int rows = level.length;
        int cols = level[0].length;
        Boolean[][] newLevel = new Boolean[rows][cols];
        for (int row = 0; row < rows; row++) {
            System.arraycopy(level[row], 0, newLevel[row], 0, cols);
        }
        return newLevel;
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
        }, true);
    }

    /**
     * Checks if the given puzzle is solvable or not
     *
     * @param levelData the level data to check
     * @return true if its solvable, otherwise false
     */
    public static boolean isSolvable(Boolean[][] levelData) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(levelData);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(levelData);
        Boolean[][] solvedLevel = solve(rowData, colData).getLevel();
        return isValid(solvedLevel);
    }

    public static void solveLevel(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        Boolean[][] solvedLevel = solve(rowData, colData).getLevel();
        level.setPixels(solvedLevel);
    }

    public static PixelLogicSolverResult solve(List<List<Integer>> rowData, List<List<Integer>> colData) {
        return new PixelLogicSolver().solve(rowData, colData);
    }

    public static String toString(Boolean[][] levelData) {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < levelData.length; row++) {
            for (int col = 0; col < levelData[0].length; col++) {
                builder.append(levelData[row][col] == null ? "-" : (levelData[row][col] ? "x" : "o"));
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
