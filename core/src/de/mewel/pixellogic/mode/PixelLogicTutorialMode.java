package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.SnapshotArray;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;

import java.util.ArrayList;
import java.util.List;

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
        //this.status = 0;
        //runLevel(this.levels.get(0));
        handleStatus4();
    }

    private PixelLogicUITutorialLevelPage getPage() {
        return (PixelLogicUITutorialLevelPage) getAppScreen().getPage(PixelLogicUIPageId.tutorialLevel);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (this.status < 10) {
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
            if (this.status == 6 && boardChangedEvent.getLevel().isRowComplete(4)) {
                handleStatus7();
            }
            if (this.status == 7 && boardChangedEvent.getLevel().isRowComplete(3)) {
                handleStatus8();
            }
            if (this.status == 10 && boardChangedEvent.getLevel().get(2, 2)) {
                handleStatus11();
            }
        }
    }

    private void handleStatus1() {
        this.status = 1;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);

        levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}Welcome to {COLOR=MAIN_COLOR}PIXELLOGIC" +
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

        List<Vector2> pixels = toPixelVector(levelUI.getLevel().getLevelData());

        SequenceAction solveSequence = createSolveSequence(levelUI, pixels, .05f);
        solveSequence.addAction(Actions.delay(1f));
        solveSequence.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                Gdx.app.log("tutorial mode", "show message button!!!");
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
        levelUI.getColumnGroup().addAction(Actions.fadeOut(.5f));
        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
        for (int i = 0; i < rowInfo.size - 1; i++) {
            rowInfo.get(i).addAction(Actions.fadeOut(.5f));
        }
        // Actor topInfo = rowInfo.get(rowInfo.size - 1);
        // topInfo.addAction(PixelLogicUIUtil.blinkAction(Color.RED, 1f));

        levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}The {COLOR=MAIN_COLOR}5 " +
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
        List<Vector2> pixels = new ArrayList<Vector2>();
        for (int x = 0; x < 5; x++) {
            pixels.add(new Vector2(x, rows - 1));
        }
        SequenceAction sequence = createSolveSequence(levelUI, pixels, 0.2f);
        levelUI.addAction(sequence);
    }

    private void handleStatus7() {
        this.status = 7;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}When a row/column is solved, the" +
                        " corresponding info box is changing its {COLOR=LINE_COMPLETE_COLOR}color.",
                new Runnable() {
                    @Override
                    public void run() {
                        levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}Now its your turn :). Fill this row!");
                        levelUI.setEnabled(false).setEnabledRow(3, true);
                        SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
                        rowInfo.get(3).addAction(Actions.fadeIn(.2f));
                    }
                });
    }

    private void handleStatus8() {
        this.status = 8;
        final PixelLogicUITutorialLevelPage levelPage = getPage();
        final PixelLogicUILevel levelUI = levelPage.getLevelUI();
        levelUI.setEnabled(false);
        levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}Perfect! Lets see the next row.", new Runnable() {
            @Override
            public void run() {
                SnapshotArray<Actor> rowInfo = levelUI.getRowGroup().getChildren();
                rowInfo.get(2).addAction(Actions.fadeIn(.2f));
                levelPage.setMessage("{SLOWER}{COLOR=TEXT_COLOR}Now {COLOR=MAIN_COLOR}3 connected " +
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
        List<Vector2> pixels = new ArrayList<Vector2>();
        pixels.add(new Vector2(offset, 2));
        pixels.add(new Vector2(1 + offset, 2));
        pixels.add(new Vector2(2 + offset, 2));
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
        levelPage.setMessage("{COLOR=TEXT_COLOR}All three possibilities have " +
                "{COLOR=MAIN_COLOR} one pixel " +
                "{COLOR=TEXT_COLOR}in common. This one has to be set. Can you find it?", new TypingAdapter() {
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
                levelPage.setMessage("{COLOR=TEXT_COLOR}We cannot tell what other pixels need to be" +
                        " set, so we leave them for now. Lets see the next row.");
            }
        });
    }

    private SequenceAction createSolveSequence(final PixelLogicUILevel levelUI, List<Vector2> pixels, float delay) {
        SequenceAction solveSequence = Actions.sequence();
        for (final Vector2 pixel : pixels) {
            solveSequence.addAction(Actions.delay(delay));
            solveSequence.addAction(Actions.run(new Runnable() {
                @Override
                public void run() {
                    levelUI.setPixel((int) pixel.y, (int) pixel.x, true);
                }
            }));
        }
        return solveSequence;
    }

    private List<Vector2> toPixelVector(Boolean[][] levelData) {
        List<Vector2> pixels = new ArrayList<Vector2>();
        for (int row = 0; row < levelData.length; row++) {
            for (int col = 0; col < levelData[0].length; col++) {
                if (levelData[row][col]) {
                    pixels.add(new Vector2(col, row));
                }
            }
        }
        return pixels;
    }


}
