package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.gui.screen.PixelLogicLevelStatus;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.gui.screen.PixelLogicLevelScreen;
import de.mewel.pixellogic.gui.screen.PixelLogicScreenManager;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzer;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzerResult;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicLevelValidator;
import de.mewel.pixellogic.util.PixelLogicSolver;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicRandomLevelMode implements PixelLogicLevelMode, PixelLogicListener {

    private PixelLogicLevelCollection collection;

    private List<Integer> played;

    @Override
    public void run(PixelLogicLevelCollection collection) {
        this.collection = collection;
        this.played = new ArrayList<Integer>();

        PixelLogicEventManager.instance().listen(this);
        loadNextLevel();
    }

    private void loadNextLevel() {
        final PixelLogicLevel level = next();
        if(level == null) {
            // TODO handle no more level's
            return;
        }
        PixelLogicScreenManager screenManager = PixelLogicScreenManager.instance();
        PixelLogicLevelScreen levelScreen = screenManager.getLevelScreen();
        levelScreen.loadLevel(level);
        screenManager.set(levelScreen);

        PixelLogicEventManager.instance().listen(new PixelLogicListener() {
            @Override
            public void handle(PixelLogicEvent event) {
                if(event instanceof PixelLogicLevelChangeEvent) {
                    PixelLogicLevelChangeEvent changeEvent = (PixelLogicLevelChangeEvent) event;
                    if(PixelLogicLevelStatus.playable.equals(changeEvent.getStatus())) {
                        // solveLevel(level);
                    }
                }
            }
        });
    }

    public PixelLogicLevel next() {
        int size = this.collection.getLevelList().size();
        if (size == played.size()) {
            return null;
        }
        PixelLogicLevel level = null;
        Random rand = new Random();
        while (level == null) {
            Integer index = rand.nextInt(size);
            if (played.contains(index)) {
                continue;
            }
            played.add(index);
            level = PixelLogicLevelLoader.load(this.collection, index);
        }
        return level;
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if(event instanceof PixelLogicLevelChangeEvent) {
            PixelLogicLevelChangeEvent changeEvent = (PixelLogicLevelChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                loadNextLevel();
            }
        }
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
