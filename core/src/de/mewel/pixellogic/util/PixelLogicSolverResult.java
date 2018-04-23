package de.mewel.pixellogic.util;

public class PixelLogicSolverResult {

    private Boolean[][] level;

    private float complexity;

    public PixelLogicSolverResult(Boolean[][] level, float complexity) {
        this.level = level;
        this.complexity = complexity;
    }

    public float getComplexity() {
        return complexity;
    }

    public Boolean[][] getLevel() {
        return level;
    }

}
