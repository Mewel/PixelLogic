package de.mewel.pixellogic.model;

import java.util.List;

import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicLevel {

    private int rows, cols;

    private Boolean[][] pixels;

    private boolean[][] levelData;

    public PixelLogicLevel(boolean[][] levelData) {
        this.rows = levelData.length;
        this.cols = levelData[0].length;
        this.levelData = levelData;
        pixels = new Boolean[rows][cols];
    }

    public int getColumns() {
        return cols;
    }

    public int getRows() {
        return rows;
    }

    public Boolean get(int row, int col) {
        return pixels[row][col];
    }

    public void set(int row, int col, Boolean pixel) {
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
                Boolean pixel = pixels[row][col];
                if ((required && (pixel == null || !pixel)) || (!required && (pixel != null && pixel))) {
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
     * @param rowIndex the row to check
     * @return true if the row is complete
     */
    public boolean isRowComplete(int rowIndex) {
        List<Integer> levelRow = PixelLogicUtil.getNumbersOfRow(levelData, rowIndex);
        boolean[] row = PixelLogicUtil.toLevelLine(this.pixels[rowIndex]);
        List<Integer> connectedPixel = PixelLogicUtil.getConnectedPixel(row);
        return PixelLogicUtil.compareNumberLists(levelRow, connectedPixel);
    }

    /**
     * Checks if a column is complete. This does not say if its correctly solved, just that all
     * numbers are entered.
     *
     * @param columnIndex the column to check
     * @return true if the row is complete
     */
    public boolean isColumnComplete(int columnIndex) {
        List<Integer> levelColumn = PixelLogicUtil.getNumbersOfCol(levelData, columnIndex);
        boolean[] column = PixelLogicUtil.toLevelLine(PixelLogicUtil.columnToLine(this.pixels, columnIndex));
        List<Integer> connectedPixel = PixelLogicUtil.getConnectedPixel(column);
        return PixelLogicUtil.compareNumberLists(levelColumn, connectedPixel);
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
