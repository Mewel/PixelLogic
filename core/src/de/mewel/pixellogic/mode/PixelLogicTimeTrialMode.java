package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.time.Instant;
import java.util.Date;
import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicTimeTrialFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicTimeTrialMode implements PixelLogicLevelMode, PixelLogicListener {

    private PixelLogicTimeTrialModeOptions options;

    private PixelLogicEventManager eventManager;

    private int round;

    private long totalTime, startTime;

    public PixelLogicTimeTrialMode(PixelLogicTimeTrialModeOptions options) {
        this.options = options;
    }

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
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
        if (++this.round >= options.levelSize.length) {
            onFinished();
            return;
        }
        Random random = new Random();
        int offset = random.nextInt(options.levelSizeOffset[this.round] + 1);
        boolean side = random.nextBoolean();

        int rows = options.levelSize[this.round];
        int cols = options.levelSize[this.round];
        int minDifficulty = options.levelMinDifficulty[this.round];
        int maxDifficulty = options.levelMaxDifficulty[this.round];
        rows += side ? offset : -offset;
        cols += side ? -offset : offset;

        Boolean[][] randomLevelData = PixelLogicUtil.createRandomLevel(rows, cols, minDifficulty, maxDifficulty);
        PixelLogicLevel randomLevel = createLevel(randomLevelData);
        runLevel(randomLevel);
    }

    private void onFinished() {
        String id = this.options.id;
        Preferences preferences = Gdx.app.getPreferences(id);
        preferences.getString("highscore_1");

        this.eventManager.fire(new PixelLogicTimeTrialFinishedEvent(this, this.totalTime));
    }

    private PixelLogicLevel createLevel(Boolean[][] levelData) {
        Integer[][] imageData = getImageData(levelData);
        return new PixelLogicLevel("#" + (this.round + 1), levelData, imageData);
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

    private static class Highscore {

        public long time;

        public long date;

        public Highscore() {
        }

        public Highscore(long time) {
            this.time = time;
            this.date = new Date().getTime();
        }

        @Override
        public String toString() {
            return String.valueOf(time) + "|" + String.valueOf(date);
        }

        public static Highscore of(String string) {
            Highscore highscore = new Highscore();
            String[] parts = string.split("|");
            highscore.time = Long.valueOf(parts[0]);
            highscore.date = Long.valueOf(parts[2]);
            return highscore;
        }

    }

}
