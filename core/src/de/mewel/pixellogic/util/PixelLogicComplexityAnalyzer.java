package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public abstract class PixelLogicComplexityAnalyzer {

    public static List<PixelLogicComplexityAnalyzerResult> analyze(PixelLogicLevelCollection collection) {
        List<PixelLogicComplexityAnalyzerResult> resultList = new ArrayList<PixelLogicComplexityAnalyzerResult>();
        List<PixelLogicLevel> levels = PixelLogicLevelLoader.load(collection);
        for (PixelLogicLevel level : levels) {
            resultList.add(analyze(level));
        }
        return resultList;
    }

    public static PixelLogicComplexityAnalyzerResult analyze(PixelLogicLevel level) {
        PixelLogicSolverResult solverResult = analyze(level.getLevelData());
        return new PixelLogicComplexityAnalyzerResult(level.getName(), solverResult);
    }

    public static PixelLogicSolverResult analyze(Boolean[][] levelData) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(levelData);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(levelData);
        return PixelLogicUtil.solve(rowData, colData);
    }

}
