package de.mewel.pixellogic.util;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public class PixelLogicComplexityAnalyzer {

    public List<PixelLogicComplexityAnalyzerResult> analyze(PixelLogicLevelCollection collection) {
        List<PixelLogicComplexityAnalyzerResult> resultList = new ArrayList<PixelLogicComplexityAnalyzerResult>();
        List<PixelLogicLevel> levels = PixelLogicLevelLoader.load(collection);
        for (PixelLogicLevel level : levels) {
            resultList.add(analyze(level));
        }
        return resultList;
    }

    public PixelLogicComplexityAnalyzerResult analyze(PixelLogicLevel level) {
        Boolean[][] levelData = level.getLevelData();
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(levelData);
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(levelData);
        PixelLogicSolverResult solverResult = new PixelLogicSolver().solve(rowData, colData);
        return new PixelLogicComplexityAnalyzerResult(level.getName(), solverResult);
    }

}
