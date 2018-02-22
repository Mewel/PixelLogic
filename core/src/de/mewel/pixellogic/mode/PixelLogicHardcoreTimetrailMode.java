package de.mewel.pixellogic.mode;

import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicHardcoreTimetrailMode implements PixelLogicLevelMode, PixelLogicListener {

    private static int[] LEVEL_SIZE = new int[]{6, 7, 8};

    private static int[] LEVEL_DIFFICULTY = new int[]{6, 7, 7};

    private PixelLogicLevel level;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private int round;

    private long totalTime, startTime;

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.eventManager.listen(this);
    }

    @Override
    public void run() {
        this.round = -1;
        this.totalTime = 0;
        runNext();
    }

    private void runNext() {
        if (++this.round >= 3) {
            // this.eventManager.fire(new PixelLogicNextLevelEvent(this, level));
            // TODO
            return;
        }
        Random random = new Random();
        int offset = random.nextInt(this.round + 1);
        boolean side = random.nextBoolean();

        int rows = LEVEL_SIZE[this.round];
        int cols = LEVEL_SIZE[this.round];
        int difficulty = LEVEL_DIFFICULTY[this.round];
        rows += side ? offset : -offset;
        cols += side ? -offset : offset;

        Boolean[][] randomLevelData = PixelLogicUtil.createRandomLevel(rows, cols, difficulty);
        PixelLogicLevel randomLevel = createLevel(randomLevelData);
        runLevel(randomLevel);
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
                runNext();
            }
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus())) {
                this.startTime = System.currentTimeMillis();
                this.eventManager.fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.start, this.totalTime));
            }
            if (PixelLogicLevelStatus.finished.equals(changeEvent.getStatus())) {
                this.totalTime += System.currentTimeMillis() - this.startTime;
                this.eventManager.fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.stop, this.totalTime));
            }
        }
    }

}
