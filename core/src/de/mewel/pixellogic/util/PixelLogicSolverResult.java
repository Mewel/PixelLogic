package de.mewel.pixellogic.util;

public class PixelLogicSolverResult {

    private Boolean[][] level;

    private int complexity;

    public PixelLogicSolverResult(Boolean[][] level, int complexity) {
        this.level = level;
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }

    public Boolean[][] getLevel() {
        return level;
    }

}
