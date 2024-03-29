package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicLoadNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicSecretLevelEvent;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.component.PixelLogicUIMessageModal;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUISecretLevelStartAnimation;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicStopWatch;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicTimeTrialMode extends PixelLogicLevelMode {

    private final PixelLogicTimeTrialModeOptions options;

    private AtomicInteger round;

    private final PixelLogicStopWatch stopWatch;

    /**
     * 0 = no secret level
     * 1 = play secret level
     * 2 = secret level finished
     */
    private short secretLevelStatus;

    public PixelLogicTimeTrialMode(PixelLogicTimeTrialModeOptions options) {
        super();
        this.options = options;
        this.stopWatch = new PixelLogicStopWatch();
        this.secretLevelStatus = 0;
    }

    public void run() {
        this.round = new AtomicInteger(0);
        runNext();
    }

    private void runNext() {
        final int roundNumber = round.getAndIncrement();
        if (roundNumber >= options.levelSize.length) {
            onFinished();
            return;
        }
        getEventManager().fire(new PixelLogicLoadNextLevelEvent(this));
        new Thread(() -> {
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
            Gdx.app.postRunnable(() -> runLevel(level));
        }).start();
    }

    private void runSecretLevel() {
        PixelLogicLevel secretLevel = PixelLogicLevelLoader.load(getAssets().getLevelCollection("secret"), "SECRET LEVEL");
        runLevel(secretLevel);
    }

    private void onFinished() {
        this.stopWatch.stop();
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("mode", this.options.id);
        data.put("time", this.stopWatch.elapsed());
        int rank = PixelLogicTimeTrialHighscoreStore.add(this.options.id.name(), this.stopWatch.elapsed());
        data.put("rank", rank);
        this.getAppScreen().setPage(PixelLogicUIPageId.timeTrialFinished, data);
    }

    private PixelLogicLevel createLevel(Boolean[][] levelData) {
        Integer[][] imageData = getImageData(levelData);
        return new PixelLogicLevel("#" + this.round.get(), levelData, imageData, false);
    }

    private Integer[][] getImageData(Boolean[][] levelData) {
        int rows = levelData.length;
        int cols = levelData[0].length;
        Integer[][] imageData = new Integer[rows][cols];
        Random random = new Random();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                float r = random.nextFloat() * .5f + .5f;
                float g = random.nextFloat() * .5f + .5f;
                float b = random.nextFloat() * .5f + .5f;
                imageData[row][col] = levelData[row][col] ?
                        new Color(r, g, b, 1f).toIntBits() : 0;
            }
        }
        return imageData;
    }

    private void startSecretLevel() {
        Gdx.input.setInputProcessor(null);
        final PixelLogicUILevelPage page = (PixelLogicUILevelPage) getAppScreen().getCurrentPage();
        final Stage stage = page.getStage();
        PixelLogicUISecretLevelStartAnimation animation = new PixelLogicUISecretLevelStartAnimation(getGlobal());
        animation.setPage(page);
        float executionTime = animation.execute();
        page.getToolbar().addAction(Actions.fadeOut(.2f));
        SequenceAction awaitAction = Actions.sequence(Actions.delay(executionTime), Actions.run(new Runnable() {
            @Override
            public void run() {
                secretLevelStatus = 1;
                final PixelLogicUIMessageModal secretLevelIntroModal = new PixelLogicUIMessageModal("secret level", getGlobal(), stage) {
                    @Override
                    protected void afterClose() {
                        super.afterClose();
                        this.clear();
                        page.destroyLevel();
                    }
                };
                secretLevelIntroModal.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                secretLevelIntroModal.show();
                secretLevelIntroModal.addListener(new InputListener() {
                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        secretLevelIntroModal.close();
                        return true;
                    }
                });
                Gdx.input.setInputProcessor(stage);
                getEventManager().fire(new PixelLogicSecretLevelEvent(PixelLogicTimeTrialMode.this, PixelLogicSecretLevelEvent.Type.find));
            }
        }));
        stage.addAction(awaitAction);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.DESTROYED.equals(changeEvent.getStatus())) {
                if (secretLevelStatus == 0) {
                    runNext();
                } else if (secretLevelStatus == 1) {
                    runSecretLevel();
                } else if (secretLevelStatus == 2) {
                    getAppScreen().setPage(PixelLogicUIPageId.timeTrial);
                    PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
                }
            }
            if (PixelLogicLevelStatus.PLAYABLE.equals(changeEvent.getStatus())) {
                if (secretLevelStatus == 0) {
                    long elapsed = this.stopWatch.startOrResume();
                    this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.start, elapsed));
                }
            }
            if (PixelLogicLevelStatus.FINISHED.equals(changeEvent.getStatus())) {
                if (secretLevelStatus == 0) {
                    long elapsed = this.stopWatch.pause();
                    this.getEventManager().fire(new PixelLogicTimerEvent(this, PixelLogicTimerEvent.Status.stop, elapsed));
                } else if (secretLevelStatus == 1) {
                    secretLevelStatus = 2;
                    getEventManager().fire(new PixelLogicSecretLevelEvent(PixelLogicTimeTrialMode.this, PixelLogicSecretLevelEvent.Type.beat));
                }
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
        if (event instanceof PixelLogicBoardChangedEvent && secretLevelStatus == 0) {
            PixelLogicBoardChangedEvent changedBoardEvent = (PixelLogicBoardChangedEvent) event;
            if (PixelLogicTimeTrialModeOptions.Mode.time_trial_hardcore.equals(options.id) &&
                    changedBoardEvent.getLevel().isBlocked()) {
                startSecretLevel();
            }
        }
    }

}
