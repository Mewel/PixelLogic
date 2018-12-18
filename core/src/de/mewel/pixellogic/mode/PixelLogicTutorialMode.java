package de.mewel.pixellogic.mode;

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

        this.status = 0;
        runLevel(this.levels.get(0));

        //this.runLevel(levels.get(1));
        //fadeOutInfoBoxes(getPage().getLevelUI());
        //handleStatus16();
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
            }
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus()) && status == 3) {
                handleStatus4();
            }
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 4) {
                handleStatus5();
            }
        }
        if (event instanceof PixelLogicBoardChangedEvent) {
            PixelLogicBoardChangedEvent boardChangedEvent = (PixelLogicBoardChangedEvent) event;
            PixelLogicLevel level = boardChangedEvent.getLevel();
            if (this.status == 6 && level.isRowComplete(4)) {
                handleStatus7();
            }
            if (this.status == 7 && level.isRowComplete(3)) {
                handleStatus8();
            }
            if (this.status == 10 && isSet(level, 2, 2, true)) {
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
            }
        }
    }

    private void handleStatus1() {
        this.status = 1;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);

        levelPage.setMessage("{COLOR=TEXT_COLOR}Welcome to {COLOR=MAIN_COLOR}PIXELLOGIC" +
                        "{COLOR=TEXT_COLOR}! {WAIT=.5}" +
                        "This game is all about solving pixel art puzzles. {WAIT=.5}Lets have some fun :)",
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
        fadeOutInfoBoxes(levelUI);
        // Actor topInfo = rowInfo.get(rowInfo.size - 1);
        // topInfo.addAction(PixelLogicUIUtil.blinkAction(Color.RED, 1f));

        levelPage.setMessage("{COLOR=TEXT_COLOR}The {COLOR=MAIN_COLOR}5 " +
                        "{COLOR=TEXT_COLOR}in this row means that" +
                        " {COLOR=MAIN_COLOR}all {COLOR=TEXT_COLOR}pixels have to be filled.",
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
        int rows = levelPage.getLevel().getRows();
        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
        for (int x = 0; x < 5; x++) {
            pixels.put(new Vector2(x, rows - 1), true);
        }
        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.2f);
        levelUI.addAction(sequence);
    }

    private void handleStatus7() {
        this.status = 7;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage("{COLOR=TEXT_COLOR}When a row/column is solved, the" +
                        " corresponding info box changes its {COLOR=LINE_COMPLETE_COLOR}color.",
                new Runnable() {
                    @Override
                    public void run() {
                        levelPage.setMessage("{COLOR=TEXT_COLOR}Now its your turn :). Fill this row!");
                        levelUI.setEnabled(false).setEnabledRow(3, true);
                        showRow(levelUI, 3);
                    }
                });
    }

    private void handleStatus8() {
        this.status = 8;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{COLOR=TEXT_COLOR}Perfect! Lets see the next line.", new Runnable() {
            @Override
            public void run() {
                showRow(levelUI, 2);
                levelPage.setMessage("{COLOR=TEXT_COLOR}Now {COLOR=MAIN_COLOR}3 connected " +
                        "{COLOR=TEXT_COLOR}pixels needs to be set. There are three possibilities." +
                        " Lets see them.", new Runnable() {
                    @Override
                    public void run() {
                        handleStatus9(0);
                    }
                });
            }
        });
    }

    private void handleStatus9(final int offset) {
        this.status = 9;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        for (int col = 0; col < levelUI.getLevel().getColumns(); col++) {
            levelUI.setPixel(2, col, null);
        }
        levelPage.setMessage("{COLOR=TEXT_COLOR}" + String.valueOf(offset + 1) + "...");
        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
        pixels.put(new Vector2(offset, 2), true);
        pixels.put(new Vector2(1 + offset, 2), true);
        pixels.put(new Vector2(2 + offset, 2), true);
        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.5f);
        sequence.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                levelPage.showNextMessageButton(new Runnable() {
                    @Override
                    public void run() {
                        levelPage.hideNextButton();
                        if (offset == 2) {
                            handleStatus10();
                        } else {
                            handleStatus9(offset + 1);
                        }
                    }
                });
            }
        }));
        levelUI.addAction(sequence);
    }

    private void handleStatus10() {
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        for (int col = 0; col < levelUI.getLevel().getColumns(); col++) {
            levelUI.setPixel(2, col, null);
        }
        this.status = 10;
        levelPage.setMessage("{COLOR=TEXT_COLOR}All three combinations have " +
                        "{COLOR=MAIN_COLOR} one pixel " +
                        "{COLOR=TEXT_COLOR}in common. This one must be set. Can you find it?",
                new PixelLogicUITutorialLevelPage.TutorialTypingAdapter(getAssets()) {
                    @Override
                    public void end() {
                        levelUI.setEnabled(2, 2, true);
                    }
                });
    }

    private void handleStatus11() {
        this.status = 11;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{COLOR=TEXT_COLOR}Exactly! The center pixel appeared in all three" +
                " combinations.", new Runnable() {
            @Override
            public void run() {
                levelPage.setMessage("{COLOR=TEXT_COLOR}We can't say what other pixels have to be" +
                        " set, so we leave them for now. Let's see the next row.", new Runnable() {
                    @Override
                    public void run() {
                        handleStatus12();
                    }
                });
            }
        });
    }

    private void handleStatus12() {
        this.status = 12;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelPage.setMessage("{COLOR=TEXT_COLOR}There is only {COLOR=MAIN_COLOR}one pixel " +
                "{COLOR=TEXT_COLOR}in this line. But without further information we can't " +
                "determine the exact position.", new Runnable() {
            @Override
            public void run() {
                levelPage.setMessage("{COLOR=MAIN_COLOR}PixelLogic {COLOR=TEXT_COLOR}is always about" +
                                " logic, you never have to guess! If you can't solve a line, try the next one.",
                        new Runnable() {
                            @Override
                            public void run() {
                                handleStatus13();
                            }
                        });
            }
        });
        showRow(levelUI, 1);

    }

    private void handleStatus13() {
        this.status = 13;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage("{COLOR=TEXT_COLOR}So, what to do here? :)",
                new PixelLogicUITutorialLevelPage.TutorialTypingAdapter(getAssets()) {
                    @Override
                    public void end() {
                        levelUI.setEnabled(0, 2, true);
                    }
                });
        showRow(levelUI, 0);
    }

    private void handleStatus14() {
        this.status = 14;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{COLOR=MAIN_COLOR}Awesome{COLOR=TEXT_COLOR}! Lets see the columns." +
                " They work just like the rows.", new Runnable() {
            @Override
            public void run() {
                showColumn(levelUI, 0);
                showColumn(levelUI, 4);
                levelPage.setMessage("{COLOR=TEXT_COLOR}Looks like those two columns are already solved." +
                        " It's important to keep track of that and block the invalid pixel.", new Runnable() {
                    @Override
                    public void run() {
                        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                        for (int row = 2; row >= 0; row--) {
                            pixels.put(new Vector2(0, row), false);
                        }
                        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.3f);
                        levelUI.addAction(sequence);
                    }
                });
            }
        });
    }

    private void handleStatus15() {
        this.status = 15;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();

        levelPage.setMessage("{COLOR=TEXT_COLOR}Block the other column!",
                new PixelLogicUITutorialLevelPage.TutorialTypingAdapter(getAssets()) {
                    @Override
                    public void end() {
                        levelPage.showSwitcher(new Runnable() {
                            @Override
                            public void run() {
                                levelUI.setEnabled(false);
                                levelUI.setEnabled(0, 4, true);
                                levelUI.setEnabled(1, 4, true);
                                levelUI.setEnabled(2, 4, true);
                            }
                        });
                    }
                });
    }

    private void handleStatus16() {
        this.status = 16;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage("{COLOR=MAIN_COLOR}Perfect{COLOR=TEXT_COLOR}! When you block " +
                "pixels you can't accidentally draw on them. Lets move on to the next column.", new Runnable() {
            @Override
            public void run() {
                showColumn(levelUI, 2);
                levelUI.setEnabled(1, 2, true);
                levelPage.setMessage("{COLOR=TEXT_COLOR}That looks easy. I think you can do it :).");
            }
        });
    }

    private void handleStatus17() {
        this.status = 17;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{COLOR=MAIN_COLOR}Great! {COLOR=TEXT_COLOR}Lets see the last two columns.", new Runnable() {
            @Override
            public void run() {
                showColumn(levelUI, 1);
                showColumn(levelUI, 3);
                levelPage.setMessage("{COLOR=TEXT_COLOR}Here's something new." +
                        " {COLOR=MAIN_COLOR}Separate numbers {COLOR=TEXT_COLOR}show different" +
                        " segments. Lets see how it works.", new Runnable() {
                    @Override
                    public void run() {
                        Map<Vector2, Boolean> pixels = new LinkedHashMap<Vector2, Boolean>();
                        pixels.put(new Vector2(1, 2), true);
                        pixels.put(new Vector2(1, 1), false);
                        pixels.put(new Vector2(1, 0), true);
                        levelUI.addAction(createSolveSequence(levelUI, pixels, 0.5f));
                    }
                });
            }
        });
    }

    private void handleStatus18() {
        this.status = 18;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(true);
        levelPage.setMessage("{COLOR=TEXT_COLOR}Separate numbers needs some {COLOR=MAIN_COLOR}space" +
                "{COLOR=TEXT_COLOR} between them. It's not always one pixel, it can vary. Now finish the level :)");
    }

    private void handleStatus19() {
        this.status = 19;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{COLOR=MAIN_COLOR}Awesome{COLOR=TEXT_COLOR}, you did it!!!", new Runnable() {
            @Override
            public void run() {
                levelPage.setMessage("{COLOR=TEXT_COLOR}You learned everything you need to know. " +
                        " To rerun the tutorial click on the question mark at the top" +
                        " right corner of the next page.", new Runnable() {
                    @Override
                    public void run() {
                        levelPage.setMessage("{COLOR=TEXT_COLOR}I recommend starting with the campaign. Have" +
                                " fun with {COLOR=MAIN_COLOR}PixelLogic{COLOR=TEXT_COLOR}!", new Runnable() {
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

    private SequenceAction createSolveSequence(final PixelLogicUILevel levelUI, Map<Vector2, Boolean> pixels,
                                               float delay) {
        SequenceAction solveSequence = Actions.sequence();
        for (final Map.Entry<Vector2, Boolean> entry : pixels.entrySet()) {
            final Vector2 pixel = entry.getKey();
            solveSequence.addAction(Actions.delay(delay));
            solveSequence.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    levelUI.setPixel((int) pixel.y, (int) pixel.x, entry.getValue());
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

    private void fadeOutInfoBoxes(PixelLogicUILevel levelUI) {
        SnapshotArray<Actor> colInfo = levelUI.getColumnGroup().getChildren();
        for (int i = 0; i < colInfo.size; i++) {
            colInfo.get(i).addAction(Actions.fadeOut(.5f));
        }
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        for (int i = 0; i < rowInfo.size - 1; i++) {
            rowInfo.get(i).addAction(Actions.fadeOut(.5f));
        }
    }


}
