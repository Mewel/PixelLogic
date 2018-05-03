package de.mewel.pixellogic.model;

import com.badlogic.gdx.graphics.Color;

import java.util.List;

import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicLevel {

    private String name;

    private int rows, cols;

    private Boolean[][] pixels;

    private Boolean[][] levelData;

    private Integer[][] image;

    public PixelLogicLevel(Boolean[][] levelData) {
        this(null, levelData);
    }

    public PixelLogicLevel(String name, Boolean[][] levelData) {
        this.name = name;
        this.rows = levelData.length;
        this.cols = levelData[0].length;
        this.levelData = levelData;
        this.pixels = new Boolean[rows][cols];
    }

    public PixelLogicLevel(String name, Integer[][] image) {
        this(name, PixelLogicUtil.toLevelData(image));
        this.image = image;
    }

    public PixelLogicLevel(String name, Boolean[][] levelData, Integer[][] image) {
        this(name, levelData);
        this.image = image;
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

    public Color getColor(int row, int col) {
        if (this.image == null) {
            return null;
        }
        return new Color(this.image[row][col]);
    }

    /**
     * Checks if this pixel is neither filled nor empty.
     *
     * @param row the row to check
     * @param col the column to check
     * @return true if the pixel is empty
     */
    public boolean isEmpty(int row, int col) {
        return pixels[row][col] == null;
    }

    public boolean isFilled(int row, int col) {
        return pixels[row][col] != null && pixels[row][col];
    }

    public boolean isBlocked(int row, int col) {
        return pixels[row][col] != null && !pixels[row][col];
    }

    public void set(int row, int col, Boolean pixel) {
        if (row < 0 || col < 0 || row >= getRows() || col >= getColumns()) {
            return;
        }
        pixels[row][col] = pixel;
    }

    public void reset() {
        this.pixels = new Boolean[rows][cols];
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
     * Checks if this level has neither filled nor blocked pixel's.
     *
     * @return true if the level is virgin
     */
    public boolean isEmpty() {
        if (this.levelData == null) {
            return true;
        }
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                if (!isEmpty(row, col)) {
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
        Boolean[] row = this.pixels[rowIndex];
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
        Boolean[] column = PixelLogicUtil.columnToLine(this.pixels, columnIndex);
        List<Integer> connectedPixel = PixelLogicUtil.getConnectedPixel(column);
        return PixelLogicUtil.compareNumberLists(levelColumn, connectedPixel);
    }

    public Boolean[][] getLevelData() {
        return levelData;
    }

    public void setPixels(Boolean[][] levelData) {
        this.pixels = levelData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String toPixelString() {
        StringBuilder builder = new StringBuilder();
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.cols; col++) {
                builder.append(this.pixels[row][col] == null ? " " : this.pixels[row][col] ? "x" : "o");
            }
        }
        return builder.toString();
    }

    public void ofPixelString(String pixelData) {
        for (int i = 0; i < pixelData.length(); i++) {
            char c = pixelData.charAt(i);
            int row = i / getColumns();
            int col = i - (row * getColumns());
            if (c == 'x') {
                set(row, col, true);
            } else if (c == 'o') {
                set(row, col, false);
            }
        }
    }

}
