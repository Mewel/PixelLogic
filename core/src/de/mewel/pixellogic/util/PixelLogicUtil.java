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

    public static List<Integer> getNumbersOfRow(boolean[][] level, int row) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        int cols = level[0].length;
        int consecutive = 0;
        for (int col = 0; col < cols; col++) {
            if (level[row][col]) {
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

    public static List<Integer> getNumbersOfCol(boolean[][] level, int col) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();
        int rows = level.length;
        int consecutive = 0;
        for (int row = 0; row < rows; row++) {
            if (level[row][col]) {
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
