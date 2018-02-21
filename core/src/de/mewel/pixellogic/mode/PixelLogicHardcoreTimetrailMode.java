package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;

import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicHardcoreTimetrailMode implements PixelLogicLevelMode, PixelLogicListener {

    private PixelLogicLevel level;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.eventManager.listen(this);
    }

    @Override
    public void run() {
        Boolean[][] randomLevelData = PixelLogicUtil.createRandomLevel(6, 6, 7);
        PixelLogicLevel randomLevel = createLevel(randomLevelData);
        runLevel(randomLevel);
    }

    @Override
    public PixelLogicLevel next() {
        return null;
    }

    private PixelLogicLevel createLevel(Boolean[][] levelData) {
        Integer[][] imageData = getImageData(levelData);
        return new PixelLogicLevel("hardcore", levelData, imageData);
    }

    private Integer[][] getImageData(Boolean[][] levelData) {
        int rows = levelData.length;
        int cols = levelData[0].length;
        Integer[][] imageData = new Integer[rows][cols];
        Random random = new Random();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                imageData[row][col] = levelData[row][col] ? random.nextInt() : 0;
            }
        }
        return imageData;
    }

    private void runLevel(PixelLogicLevel level) {
        this.level = level;
        this.eventManager.fire(new PixelLogicNextLevelEvent(this, level));
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                run();
            }
        }
    }

}
