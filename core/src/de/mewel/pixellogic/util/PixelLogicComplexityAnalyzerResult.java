package de.mewel.pixellogic.util;

public class PixelLogicComplexityAnalyzerResult {

    private String name;

    private PixelLogicSolverResult result;

    public PixelLogicComplexityAnalyzerResult(PixelLogicSolverResult result) {
        this("unknown", result);
    }

    public PixelLogicComplexityAnalyzerResult(String name, PixelLogicSolverResult result) {
        this.name = name;
        this.result = result;
    }

    public float getComplexity() {
        return result.getComplexity();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + ": " + result.getComplexity();
    }

}
