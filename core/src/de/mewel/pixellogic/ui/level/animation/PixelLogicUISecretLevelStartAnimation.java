package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import java.util.Random;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoard;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public class PixelLogicUISecretLevelStartAnimation extends PixelLogicUIBaseLevelAnimation {

    private static final float DELAY = .3f;

    private Random random;

    public PixelLogicUISecretLevelStartAnimation() {
        this.random = new Random();
    }

    @Override
    public float execute() {
        float time = super.execute();
        PixelLogicUIBoard board = getLevelUI().getBoard();
        board.setOrigin(Align.center);
        SequenceAction sequence = Actions.sequence(Actions.delay(DELAY), Actions.parallel(
                Actions.rotateBy(360, time),
                Actions.scaleBy(2, 2, time)
        ));
        board.addAction(sequence);
        return time;
    }

    @Override
    protected float animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        int row = pixel.getRow();
        int col = pixel.getCol();
        int pos = find(row, col, level.getRows(), level.getColumns());
        // fade out
        float delayPerPixel = 1.5f / (level.getRows() * level.getColumns());
        float delayDuration = DELAY + (delayPerPixel * pos);
        SequenceAction fadeOutAction = new SequenceAction();
        fadeOutAction.addAction(Actions.delay(delayDuration));
        fadeOutAction.addAction(Actions.fadeOut(.3f));
        // colorize
        SequenceAction colorizeAction = new SequenceAction();
        colorizeAction.addAction(Actions.delay(delayDuration - .1f));
        float offset = random.nextFloat() * .4f * (random.nextBoolean() ? 1f : -1f);
        Color newColor = new Color(PixelLogicConstants.PIXEL_BLOCKED_COLOR);
        newColor.r = newColor.r + offset;
        newColor.g = newColor.g + offset;
        newColor.b = newColor.b + offset;
        colorizeAction.addAction(Actions.color(newColor, .1f));

        // parallel fade out and colorize
        ParallelAction parallelAction = new ParallelAction(fadeOutAction, colorizeAction);
        pixel.addAction(parallelAction);
        return getDuration(fadeOutAction);
    }

    @Override
    protected float animateRowGroup(PixelLogicUIRowGroup group) {
        group.addAction(Actions.fadeOut(FADE_OUT_TIME));
        return FADE_OUT_TIME;
    }

    @Override
    protected float animateColumnGroup(PixelLogicUIColumnGroup group) {
        group.addAction(Actions.fadeOut(FADE_OUT_TIME));
        return FADE_OUT_TIME;
    }

    protected int find(int row, int col, int rows, int cols) {
        int dir = 1;
        int x = 0;
        int y = 0;
        int xMax = cols;
        int yMax = rows;
        int xMin = 0;
        int yMin = 0;

        int num = 0;
        while (y != row || x != col) {
            if (dir == 1) {
                x++;
                if (x + 1 >= xMax) {
                    dir = 2;
                    yMin++;
                }
            } else if (dir == 2) {
                y++;
                if (y + 1 >= yMax) {
                    dir = 3;
                    xMax--;
                }
            } else if (dir == 3) {
                x--;
                if (x <= xMin) {
                    dir = 4;
                    yMax--;
                }
            } else {
                y--;
                if (y <= yMin) {
                    dir = 1;
                    xMin++;
                }
            }
            num++;
        }
        return num;
    }

}
