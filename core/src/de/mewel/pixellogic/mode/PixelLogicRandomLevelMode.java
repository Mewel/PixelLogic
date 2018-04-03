package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicRandomLevelMode extends PixelLogicLevelMode {

    private PixelLogicLevelCollection collection;

    private List<Integer> played;

    public PixelLogicRandomLevelMode(PixelLogicLevelCollection collection) {
        this.collection = collection;
    }

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super.setup(assets, eventManager);
        this.played = new ArrayList<Integer>();
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
        runLevel(level);
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
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                loadNextLevel();
            }
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus())) {
                // solveLevel(level);
            }
        }
    }

}
