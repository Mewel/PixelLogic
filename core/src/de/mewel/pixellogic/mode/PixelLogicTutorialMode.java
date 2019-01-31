package de.mewel.pixellogic.mode;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.SnapshotArray;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUITutorialLevelPage;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND_VOLUME;

public class PixelLogicTutorialMode extends PixelLogicLevelMode {

    private int status;

    private List<PixelLogicLevel> levels;

    @Override
    public void setup(PixelLogicGlobal global) {
        super.setup(global);
        this.levels = PixelLogicLevelLoader.load(getAssets().getLevelCollection("tutorial"));
    }

    @Override
    public void run() {
        this.levels.get(0).reset();
        this.levels.get(1).reset();
        this.levels.get(2).reset();
        this.levels.get(3).reset();

        this.status = 0;
        runLevel(this.levels.get(0));

        /*this.runLevel(levels.get(1));
        fadeOutInfoBoxes(getPage().getLevelUI());
        handleStatus16();*/
    }

    private PixelLogicUITutorialLevelPage getPage() {
        return (PixelLogicUITutorialLevelPage) getAppScreen().getPage(PixelLogicUIPageId.tutorialLevel);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (this.status < 15) {
            getPage().hideSwitcher();
        }
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 0) {
                handleStatus1();
            } else if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 3) {
                handleStatus4();
            } else if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 4) {
                handleStatus5();
            } else if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 9) {
                handleStatus10();
            } else if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 10) {
                handleStatus11();
            } else if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 11) {
                handleStatus12();
            } else if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 14) {
                handleStatus15();
            } else if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 15) {
                handleStatus16();
            } else if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 16) {
                handleStatus17();
            } else if (PixelLogicLevelStatus.solved.equals(changeEvent.getStatus()) && status == 21) {
                handleStatus22();
            }
        }
        if (event instanceof PixelLogicBoardChangedEvent) {
            PixelLogicBoardChangedEvent boardChangedEvent = (PixelLogicBoardChangedEvent) event;
            PixelLogicLevel level = boardChangedEvent.getLevel();
            if (this.status == 6 && level.isRowComplete(0)) {
                handleStatus7();
            } else if (this.status == 7 && level.isRowComplete(1)) {
                handleStatus8();
            } else if (this.status == 8 && level.isSolved()) {
                handleStatus9();
            } else if (this.status == 12 && level.isColumnComplete(1)) {
                handleStatus13();
            } else if (this.status == 13 && level.isColumnComplete(2)) {
                handleStatus14();
            } else if (this.status == 17 && level.isRowComplete(4)) {
                handleStatus18();
            } else if (this.status == 18 && level.isBlocked(4, 1) && level.isBlocked(4, 3)) {
                handleStatus19();
            } else if (this.status == 19 && level.isEmpty()) {
                handleStatus20();
            } else if (this.status == 20 && level.isRowComplete(4) && level.isBlocked(4, 1) && level.isBlocked(4, 3)) {
                handleStatus21();
            }
            /*if (this.status == 10 && isSet(level, 2, 2, true)) {
                handleStatus11();
            }
            if (this.status == 13 && isSet(level, 0, 2, true)) {
                handleStatus14();
            }
            if (this.status == 14 && isSet(level, 0, 0, false)) {
                handleStatus15();
            }
            if (this.status == 15 &&
                    isSet(level, 0, 4, false) &&
                    isSet(level, 1, 4, false) &&
                    isSet(level, 2, 4, false)) {
                handleStatus16();
            }
            if (this.status == 16 && isSet(level, 1, 2, true)) {
                handleStatus17();
            }
            if (this.status == 17 && isSet(level, 0, 1, true)) {
                handleStatus18();
            }
            if (status == 18 && level.isSolved()) {
                handleStatus19();
            }*/
        }
    }

    private void handleStatus1() {
        this.status = 1;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);

        levelPage.setMessage(getAssets().translate("tutorial.status1"),
                new Runnable() {
                    @Override
                    public void run() {
                        handleStatus2();
                    }
                });
    }

    private void handleStatus2() {
        this.status = 2;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        Map<Vector2, Boolean> pixelMap = toPixelVectorMap(levelUI.getLevel().getLevelData());

        SequenceAction solveSequence = createSolveSequence(levelUI, pixelMap, .05f);
        solveSequence.addAction(Actions.delay(1f));
        solveSequence.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                levelPage.showNextMessageButton(new Runnable() {
                    @Override
                    public void run() {
                        handleStatus3();
                    }
                });
            }
        }));
        levelUI.addAction(solveSequence);
    }

    private void handleStatus3() {
        this.status = 3;
        getPage().destroyLevel();
    }

    private void handleStatus4() {
        this.status = 4;
        this.runLevel(levels.get(1));
    }

    private void handleStatus5() {
        this.status = 5;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        fadeOutColumns(levelUI, -1);
        fadeOutRows(levelUI, 0);
        // Actor topInfo = rowInfo.get(rowInfo.size - 1);
        // topInfo.addAction(PixelLogicUIUtil.blinkAction(Color.RED, 1f));

        levelPage.setMessage(getAssets().translate("tutorial.status5"),
                new Runnable() {
                    @Override
                    public void run() {
                        handleStatus6();
                    }
                });
    }

    private void handleStatus6() {
        this.status = 6;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        PixelLogicLevel level = levelPage.getLevel();
        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
        for (int x = 0; x < level.getColumns(); x++) {
            pixels.put(new Vector2(x, 0), true);
        }
        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.2f);
        levelUI.addAction(sequence);
    }

    private void handleStatus7() {
        this.status = 7;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelPage.setMessage(getAssets().translate("tutorial.status7"));
        levelUI.setEnabled(false).setEnabledRow(1, true);
        showRow(levelUI, 1);
        hideRow(levelUI, 0);
    }

    private void handleStatus8() {
        this.status = 8;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage(getAssets().translate("tutorial.status8"), new Runnable() {
            @Override
            public void run() {
                levelUI.setEnabled(true);
                fadeInInfoBoxes(levelUI);
                levelPage.hideMessage();
            }
        });
    }

    private void handleStatus9() {
        this.status = 9;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        levelPage.getScreenListener().setDestroyLevelOnClick(true);
    }

    private void handleStatus10() {
        this.status = 10;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        levelPage.getScreenListener().setDestroyLevelOnClick(true);
        this.runLevel(levels.get(2));
    }

    private void handleStatus11() {
        this.status = 11;
        runLevel(levels.get(3));
    }

    private void handleStatus12() {
        this.status = 12;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelUI.setEnabled(false);

        levelPage.setMessage(getAssets().translate("tutorial.status12a"),
                new Runnable() {
                    @Override
                    public void run() {
                        fadeOutRows(levelUI, -1);
                        fadeOutColumns(levelUI, 1);
                        levelPage.setMessage(getAssets().translate("tutorial.status12b"), new Runnable() {
                            @Override
                            public void run() {
                                Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                                pixels.put(new Vector2(1, 3), true);
                                pixels.put(new Vector2(1, 2), true);
                                pixels.put(new Vector2(1, 0), true);
                                SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.2f);
                                levelUI.addAction(sequence);
                            }
                        });
                    }
                });
    }

    private void handleStatus13() {
        this.status = 13;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelPage.setMessage(getAssets().translate("tutorial.status13"));
        levelUI.setEnabled(false).setEnabledColumn(2, true);
        showColumn(levelUI, 2);
    }

    private void handleStatus14() {
        this.status = 14;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelPage.setMessage(getAssets().translate("tutorial.status14"), new Runnable() {
            @Override
            public void run() {
                levelUI.setEnabled(true);
                fadeInInfoBoxes(levelUI);
                levelPage.hideMessage();
                levelPage.getScreenListener().setDestroyLevelOnClick(true);
            }
        });
    }

    private void handleStatus15() {
        this.status = 15;
        runLevel(levels.get(4));
    }

    private void handleStatus16() {
        this.status = 16;
        runLevel(levels.get(5));
    }

    private void handleStatus17() {
        this.status = 17;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelUI.setEnabled(false);
        fadeOutRows(levelUI, 4);
        fadeOutColumns(levelUI, -1);

        levelPage.setMessage(getAssets().translate("tutorial.status17a"), new Runnable() {
            @Override
            public void run() {
                levelPage.setMessage(getAssets().translate("tutorial.status17b"), new Runnable() {
                    @Override
                    public void run() {
                        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                        pixels.put(new Vector2(0, 4), true);
                        pixels.put(new Vector2(2, 4), true);
                        pixels.put(new Vector2(4, 4), true);
                        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.5f);
                        levelUI.addAction(sequence);
                    }
                });
            }
        });
    }

    private void handleStatus18() {
        this.status = 18;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.showSwitcher(new Runnable() {
            @Override
            public void run() {
                Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                pixels.put(new Vector2(1, 4), false);
                pixels.put(new Vector2(3, 4), false);
                SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.5f);
                levelUI.addAction(sequence);
            }
        });
    }

    private void handleStatus19() {
        this.status = 19;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage(getAssets().translate("tutorial.status19"), new Runnable() {
            @Override
            public void run() {
                Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                for (int col = levelUI.getLevel().getColumns() - 1; col >= 0; col--) {
                    pixels.put(new Vector2(col, 4), null);
                }
                SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.1f);
                levelUI.addAction(new SequenceAction(Actions.delay(.5f, sequence)));
            }
        });
    }

    private void handleStatus20() {
        this.status = 20;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabledRow(4, true);
    }

    private void handleStatus21() {
        this.status = 21;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage(getAssets().translate("tutorial.status21"), new PixelLogicUITutorialLevelPage.TutorialTypingAdapter(getAssets()) {
            @Override
            public void end() {
                fadeInInfoBoxes(levelUI);
                levelPage.getScreenListener().setDestroyLevelOnClick(false);
                levelUI.setEnabled(true);
            }
        });
    }

    private void handleStatus22() {
        this.status = 22;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage(getAssets().translate("tutorial.status22a"), new Runnable() {
            @Override
            public void run() {
                levelPage.setMessage(getAssets().translate("tutorial.status22b"), new Runnable() {
                    @Override
                    public void run() {
                        levelPage.setMessage(getAssets().translate("tutorial.status22c"), new Runnable() {
                            @Override
                            public void run() {
                                levelPage.destroyLevel();
                                getAppScreen().setPage(PixelLogicUIPageId.play);
                            }
                        });
                    }
                });
            }
        });
    }

    private boolean isSet(PixelLogicLevel level, int row, int col, boolean targetValue) {
        Boolean value = level.get(row, col);
        return value != null && value == targetValue;
    }

    private void showRow(PixelLogicUILevel levelUI, int row) {
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        rowInfo.get(row).addAction(Actions.fadeIn(.2f));
    }

    private void showColumn(PixelLogicUILevel levelUI, int col) {
        SnapshotArray<Actor> colInfo = levelUI.getColumnGroup().getChildren();
        colInfo.get(col).addAction(Actions.fadeIn(.2f));
    }

    private void hideRow(PixelLogicUILevel levelUI, int row) {
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        rowInfo.get(row).addAction(Actions.fadeOut(.2f));
    }

    private void hideColumn(PixelLogicUILevel levelUI, int col) {
        SnapshotArray<Actor> colInfo = levelUI.getColumnGroup().getChildren();
        colInfo.get(col).addAction(Actions.fadeOut(.2f));
    }

    private SequenceAction createSolveSequence(final PixelLogicUILevel levelUI, Map<Vector2, Boolean> pixels,
                                               float delay) {
        SequenceAction solveSequence = Actions.sequence();
        final Sound drawSound = getAssets().get().get(DRAW_SOUND);
        final Sound blockSound = getAssets().get().get(BLOCK_SOUND);
        for (final Map.Entry<Vector2, Boolean> entry : pixels.entrySet()) {
            final Vector2 pixel = entry.getKey();
            solveSequence.addAction(Actions.delay(delay));
            solveSequence.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    levelUI.setPixel((int) pixel.y, (int) pixel.x, entry.getValue());
                    if (entry.getValue() != null) {
                        if (entry.getValue()) {
                            drawSound.play(DRAW_SOUND_VOLUME);
                        } else {
                            blockSound.play(BLOCK_SOUND_VOLUME);
                        }
                    }
                }
            }));
        }
        return solveSequence;
    }

    private Map<Vector2, Boolean> toPixelVectorMap(Boolean[][] levelData) {
        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
        for (int row = 0; row < levelData.length; row++) {
            for (int col = 0; col < levelData[0].length; col++) {
                if (levelData[row][col]) {
                    pixels.put(new Vector2(col, row), true);
                }
            }
        }
        return pixels;
    }

    private void fadeOutRows(PixelLogicUILevel levelUI, int ignore) {
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        for (int i = 0; i < rowInfo.size; i++) {
            if (i == ignore) {
                continue;
            }
            rowInfo.get(i).addAction(Actions.fadeOut(.5f));
        }
    }

    private void fadeOutColumns(PixelLogicUILevel levelUI, int ignore) {
        SnapshotArray<Actor> colInfo = levelUI.getColumnGroup().getChildren();
        for (int i = 0; i < colInfo.size; i++) {
            if (i == ignore) {
                continue;
            }
            colInfo.get(i).addAction(Actions.fadeOut(.5f));
        }
    }

    private void fadeInInfoBoxes(PixelLogicUILevel levelUI) {
        SnapshotArray<Actor> colInfo = levelUI.getColumnGroup().getChildren();
        for (int i = 0; i < colInfo.size; i++) {
            colInfo.get(i).addAction(Actions.fadeIn(.5f));
        }
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        for (int i = 0; i < rowInfo.size; i++) {
            rowInfo.get(i).addAction(Actions.fadeIn(.5f));
        }
    }


}
