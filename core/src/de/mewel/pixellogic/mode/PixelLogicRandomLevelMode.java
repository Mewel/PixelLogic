package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.PixelLogicLevelStatus;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.screen.PixelLogicLevelScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenManager;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzer;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzerResult;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicLevelValidator;
import de.mewel.pixellogic.util.PixelLogicSolver;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicRandomLevelMode implements PixelLogicLevelMode, PixelLogicListener {

    private PixelLogicLevelCollection collection;

    private List<Integer> played;

    private PixelLogicLevel level;

    public PixelLogicRandomLevelMode(PixelLogicLevelCollection collection) {
        this.collection = collection;
        this.played = new ArrayList<Integer>();
        PixelLogicEventManager.instance().listen(this);
    }

    @Override
    public void run() {
        loadNextLevel();
    }

    private void loadNextLevel() {
        PixelLogicLevel level = next();
        if (level == null) {
            // TODO handle no more level's
            return;
        }
        this.level = level;

        PixelLogicScreenManager screenManager = PixelLogicScreenManager.instance();
        PixelLogicLevelScreen levelScreen = screenManager.getLevelScreen();
        levelScreen.loadLevel(level);
        screenManager.set(levelScreen);
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
        if (event instanceof PixelLogicLevelChangeEvent) {
            PixelLogicLevelChangeEvent changeEvent = (PixelLogicLevelChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                loadNextLevel();
            }
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus())) {
                // solveLevel(level);
            }
        }
    }

}
