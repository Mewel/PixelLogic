package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicLoadNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUISecretLevelStartAnimation;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicUserChangedBoardEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicStopWatch;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicTimeTrialMode extends PixelLogicLevelMode {

    private PixelLogicTimeTrialModeOptions options;

    private AtomicInteger round;

    private PixelLogicStopWatch stopWatch;

    private boolean playSecretLevel;

    public PixelLogicTimeTrialMode(PixelLogicTimeTrialModeOptions options) {
        this.options = options;
        this.stopWatch = new PixelLogicStopWatch();
        this.playSecretLevel = false;
    }

    @Override
    public void setup(PixelLogicGlobal global) {
        super.setup(global);
    }

    @Override
    public void run() {
        this.round = new AtomicInteger(0);
        final PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", PixelLogicUIPageId.level);
        data.put("options", options);
        data.put("menuBackId", PixelLogicUIPageId.timeTrial);
        getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
        runNext();
    }

    private void runNext() {
        final int roundNumber = round.getAndIncrement();
        if (roundNumber >= options.levelSize.length) {
            onFinished();
            return;
        }
        getEventManager().fire(new PixelLogicLoadNextLevelEvent(this));
        new Thread() {
            @Override
            public void run() {
                Random random = new Random();
                int offset = random.nextInt(options.levelSizeOffset[roundNumber] + 1);
                boolean side = random.nextBoolean();

                int rows = options.levelSizeXY == null ? options.levelSize[roundNumber] :
                        options.levelSizeXY[roundNumber * 2 + 1];
                int cols = options.levelSizeXY == null ? options.levelSize[roundNumber] :
                        options.levelSizeXY[roundNumber * 2];
                float minDifficulty = options.levelMinDifficulty[roundNumber];
                float maxDifficulty = options.levelMaxDifficulty[roundNumber];
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

    private void runSecretLevel() {

    }

    private void onFinished() {
        this.stopWatch.stop();
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", PixelLogicUIPageId.timeTrialFinished);
        data.put("mode", this.options.id);
        data.put("time", this.stopWatch.elapsed());
        int rank = PixelLogicTimeTrialHighscoreStore.add(this.options.id.name(), this.stopWatch.elapsed());
        data.put("rank", rank);
        this.getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
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

    private void startSecretLevel() {
        final PixelLogicUILevelPage page = (PixelLogicUILevelPage) getAppScreen().getPage(PixelLogicUIPageId.level);
        float executionTime = new PixelLogicUISecretLevelStartAnimation(page).execute();
        page.getToolbar().addAction(Actions.fadeOut(.2f));
        SequenceAction awaitAction = Actions.sequence(Actions.delay(executionTime), Actions.run(new Runnable() {
            @Override
            public void run() {
                playSecretLevel = true;
                page.destroyLevel();
            }
        }));
        page.getStage().addAction(awaitAction);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                if(!playSecretLevel) {
                    runNext();
                } else {
                    runSecretLevel();
                }
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
        if (event instanceof PixelLogicUIPageChangeEvent) {
            PixelLogicUIPageChangeEvent screenChangeEvent = (PixelLogicUIPageChangeEvent) event;
            if (!screenChangeEvent.getPageId().equals(PixelLogicUIPageId.level)) {
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
        if (event instanceof PixelLogicUserChangedBoardEvent) {
            PixelLogicUserChangedBoardEvent changedBoardEvent = (PixelLogicUserChangedBoardEvent) event;
            if (!PixelLogicTimeTrialModeOptions.Mode.time_trial_easy.equals(options.id) &&
                    changedBoardEvent.getLevel().isFilled()) {
                startSecretLevel();
            }
        }
    }

}
