package de.mewel.pixellogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.List;

import de.mewel.pixellogic.gui.PixelLogicGUILevelResolutionManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzer;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzerResult;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicLevelValidator;
import de.mewel.pixellogic.util.PixelLogicSolver;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicLevelManager levelManager = new PixelLogicLevelManager();
        levelManager.setup();
        PixelLogicLevelCollection levelCollection = levelManager.getCollection("characters");

        PixelLogicLevel level = PixelLogicLevelLoader.load(levelCollection, "Pikachu");

        validateLevel(level);
        // analyzeCollection(levelCollection);
        // solveLevel(level);

        PixelLogicLevelScreen screen = new PixelLogicLevelScreen();
         //screen.load(PixelLogicUtil.sampleImageLevel());
        screen.load(level);

        this.setScreen(screen);
    }

    @Override
    public void dispose() {
        PixelLogicGUILevelResolutionManager.instance().dispose();
        super.dispose();
    }

    private void validateLevel(PixelLogicLevel level) {
        PixelLogicLevelValidator validator = new PixelLogicLevelValidator();
        Gdx.app.log("validate level", "level is " + (validator.validate(level.getLevelData()) ? "valid" : "invalid"));
    }

    private void analyzeCollection(PixelLogicLevelCollection levelCollection) {
        PixelLogicComplexityAnalyzer analyzer = new PixelLogicComplexityAnalyzer();
        List<PixelLogicComplexityAnalyzerResult> result = analyzer.analyze(levelCollection);
        System.out.println(result);
    }

    private void solveLevel(PixelLogicLevel level) {
        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        Boolean[][] solvedLevel = new PixelLogicSolver().solve(rowData, colData).getLevel();
        level.setPixels(solvedLevel);
    }

}
