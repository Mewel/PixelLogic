package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicCollectionManager;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.screen.PixelLogicLevelScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenManager;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCampaignMode implements PixelLogicLevelMode, PixelLogicListener {

    private List<PixelLogicLevel> levels;

    private PixelLogicLevel level;

    public PixelLogicCampaignMode() {
        loadLevels();
        PixelLogicEventManager.instance().listen(this);
    }

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

    @Override
    public PixelLogicLevel next() {
        int currentLevelIndex = this.levels.indexOf(this.level);
        if (currentLevelIndex == -1) {
            return this.levels.get(0);
        }
        int nextLevelIndex = currentLevelIndex + 1;
        if (nextLevelIndex >= this.levels.size()) {
            for(PixelLogicLevel level : this.levels) {
                level.reset();
            }
            return this.levels.get(0);
        }
        return this.levels.get(nextLevelIndex);
    }

    private void loadLevels() {
        levels = new ArrayList<PixelLogicLevel>();
        PixelLogicCollectionManager collectionManager = PixelLogicCollectionManager.instance();

        // load tutorial level's
        PixelLogicLevelCollection levelCollection = collectionManager.getCollection("tutorial");
        levels.addAll(PixelLogicLevelLoader.load(levelCollection));
    }

}
