package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.async.AsyncTask;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicLoadNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenProperties;
import de.mewel.pixellogic.ui.screen.event.PixelLogicScreenChangeEvent;
import de.mewel.pixellogic.util.PixelLogicStopWatch;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicTimeTrialMode extends PixelLogicLevelMode {

    private PixelLogicTimeTrialModeOptions options;

    private AtomicInteger round;

    private PixelLogicStopWatch stopWatch;

    public PixelLogicTimeTrialMode(PixelLogicTimeTrialModeOptions options) {
        this.options = options;
        this.stopWatch = new PixelLogicStopWatch();
    }

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super.setup(assets, eventManager);
    }

    @Override
    public void run() {
        this.round = new AtomicInteger(-1);
        final PixelLogicUIScreenProperties data = new PixelLogicUIScreenProperties();
        data.put("screenId", "level");
        data.put("options", options);
        data.put("menu_back_id", "timeTrial");
        getEventManager().fire(new PixelLogicScreenChangeEvent(this, data));
        runNext();
    }

    private void runNext() {
        final int rountNumber = round.incrementAndGet();
        if (rountNumber >= options.levelSize.length) {
            onFinished();
            return;
        }
        getEventManager().fire(new PixelLogicLoadNextLevelEvent(this));
        new Thread() {
            @Override
            public void run() {
                Random random = new Random();
                int offset = random.nextInt(options.levelSizeOffset[rountNumber] + 1);
                boolean side = random.nextBoolean();

                int rows = options.levelSize[rountNumber];
                int cols = options.levelSize[rountNumber];
                int minDifficulty = options.levelMinDifficulty[rountNumber];
                int maxDifficulty = options.levelMaxDifficulty[rountNumber];
                rows += side ? offset : -offset;
                cols += side ? -offset : offset;

                Boolean[][] randomLevelData = PixelLogicUtil.createRandomLevel(rows, cols, minDifficulty, maxDifficulty);
                final PixelLogicLevel level = createLevel(randomLevelData);
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        runLevel(level);
                    }
                });
            }
        }.start();
    }

    private void onFinished() {
        this.stopWatch.stop();
        PixelLogicUIScreenProperties data = new PixelLogicUIScreenProperties();
        data.put("screenId", "timeTrialFinished");
        String mode = this.options.id;
        data.put("mode", mode);
        data.put("time", this.stopWatch.elapsed());
        int rank = PixelLogicTimeTrialHighscoreStore.add(mode, this.stopWatch.elapsed());
        data.put("rank", rank);
        this.getEventManager().fire(new PixelLogicScreenChangeEvent(this, data));
    }

    private PixelLogicLevel createLevel(Boolean[][] levelData) {
        Integer[][] imageData = getImageData(levelData);
        return new PixelLogicLevel("#" + (this.round.get() + 1), levelData, imageData);
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

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                runNext();
            }
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus())) {
                long elapsed = this.stopWatch.startOrResume();
                this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.start, elapsed));
            }
            if (PixelLogicLevelStatus.finished.equals(changeEvent.getStatus())) {
                long elapsed = this.stopWatch.pause();
                this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.stop, elapsed));
            }
        }
        if (event instanceof PixelLogicScreenChangeEvent) {
            PixelLogicScreenChangeEvent screenChangeEvent = (PixelLogicScreenChangeEvent) event;
            if (!screenChangeEvent.getScreenId().equals("level")) {
                this.dispose();
            }
        }
        if (event instanceof PixelLogicUserEvent) {
            PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
            if (PixelLogicUserEvent.Type.LEVEL_MENU_CLICKED.equals(userEvent.getType())) {
                long elapsed = this.stopWatch.pause();
                this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.pause, elapsed));
            }
            if (PixelLogicUserEvent.Type.LEVEL_MENU_CLOSED.equals(userEvent.getType())) {
                long elapsed = this.stopWatch.resume();
                this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.resume, elapsed));
            }
        }
    }

}
