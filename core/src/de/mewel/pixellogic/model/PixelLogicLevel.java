package de.mewel.pixellogic.model;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicLevel {

    private int rows, cols;

    private Pixel[][] pixels;

    private boolean[][] levelData;

    public PixelLogicLevel(boolean[][] levelData) {
        this.rows = levelData.length;
        this.cols = levelData[0].length;
        this.levelData = levelData;
        pixels = new Pixel[rows][cols];
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                pixels[row][col] = Pixel.EMPTY;
            }
        }
    }

    public int getColumns() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Pixel get(int row, int col) {
        return pixels[row][col];
    }

    public void set(int row, int col, Pixel pixel) {
        if (row < 0 || col < 0 || row >= getRows() || col >= getColumns()) {
            return;
        }
        pixels[row][col] = pixel;
    }

    /**
     * Checks if the whole level is solved.
     *
     * @return true if its solved
     */
    public boolean isSolved() {
        if (this.levelData == null) {
            return false;
        }
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                boolean required = levelData[row][col];
                Pixel pixel = pixels[row][col];
                if ((required && !pixel.equals(Pixel.FULL)) || (!required && pixel.equals(Pixel.FULL))) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if a row is complete. This does not say if its correctly solved, just that all
     * numbers are entered.
     *
     * @param row the row to check
     * @return true if the row is complete
     */
    public boolean isRowComplete(int row) {
        List<Integer> levelRow = PixelLogicUtil.getNumbersOfRow(levelData, row);
        List<Integer> pixelRow = this.getRowPixelData(row);
        return PixelLogicUtil.compareNumberLists(levelRow, pixelRow);
    }

    /**
     * Checks if a column is complete. This does not say if its correctly solved, just that all
     * numbers are entered.
     *
     * @param column the column to check
     * @return true if the row is complete
     */
    public boolean isColumnComplete(int column) {
        List<Integer> levelColumn = PixelLogicUtil.getNumbersOfCol(levelData, column);
        List<Integer> pixelColumn = this.getColumnPixelData(column);
        return PixelLogicUtil.compareNumberLists(levelColumn, pixelColumn);
    }

    public List<Integer> getRowPixelData(int row) {
        List<Integer> rowData = new ArrayList<Integer>();
        int consecutive = 0;
        for (int col = 0; col < this.cols; col++) {
            if (Pixel.FULL.equals(pixels[row][col])) {
                consecutive++;
                continue;
            }
            if (consecutive == 0) {
                continue;
            }
            rowData.add(consecutive);
            consecutive = 0;
        }
        if (consecutive != 0) {
            rowData.add(consecutive);
        }
        if (rowData.isEmpty()) {
            rowData.add(0);
        }
        return rowData;
    }

    public List<Integer> getColumnPixelData(int col) {
        List<Integer> colData = new ArrayList<Integer>();
        int consecutive = 0;
        for (int row = 0; row < this.rows; row++) {
            if (Pixel.FULL.equals(pixels[row][col])) {
                consecutive++;
                continue;
            }
            if (consecutive == 0) {
                continue;
            }
            colData.add(consecutive);
            consecutive = 0;
        }
        if (consecutive != 0) {
            colData.add(consecutive);
        }
        if (colData.isEmpty()) {
            colData.add(0);
        }
        return colData;
    }

    public boolean[][] getLevelData() {
        return levelData;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                builder.append(this.levelData[row][col] ? "x" : "o");
            }
            builder.append("\n");
        }
        return builder.toString();
    }

}
