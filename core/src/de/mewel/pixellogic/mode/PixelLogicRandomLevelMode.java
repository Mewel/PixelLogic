package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicRandomLevelMode implements PixelLogicLevelMode, PixelLogicListener {

    private PixelLogicLevelCollection collection;

    private List<Integer> played;

    private PixelLogicLevel level;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicRandomLevelMode(PixelLogicLevelCollection collection) {
        this.collection = collection;
    }

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.played = new ArrayList<Integer>();
        this.eventManager.listen(this);
    }

    @Override
    public void dispose() {
        this.eventManager.remove(this);
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
        this.eventManager.fire(new PixelLogicNextLevelEvent(this, level));
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
