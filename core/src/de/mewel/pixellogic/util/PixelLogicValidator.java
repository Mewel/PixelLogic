package de.mewel.pixellogic.util;


import java.util.List;

/**
 * Checks if a pixel game has just one or multiple solutions.
 */
public class PixelLogicValidator {

    /**
     * Checks if the given puzzle is valid or not
     *
     * @param levelData the level to check
     * @return true if its valid, otherwise false
     */
    public boolean validate(boolean[][] levelData) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(levelData);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(levelData);
        Boolean[][] solvedLevel = new PixelLogicBruteForceSolver().solve(rowData, colData);
        return PixelLogicUtil.isValid(solvedLevel);
    }

}
