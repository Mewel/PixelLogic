package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoard;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public class PixelLogicUISecretLevelStartAnimation extends PixelLogicUILevelAnimation {

    public PixelLogicUISecretLevelStartAnimation(PixelLogicUILevelPage levelPage) {
        super(levelPage);
    }

    @Override
    public float execute() {
        float time = super.execute();
        getLevelUI();
        PixelLogicUIBoard board = getLevelUI().getBoard();
        board.addAction(Actions.moveTo(-board.getWidth(), -board.getHeight(), time));
        board.addAction(Actions.scaleBy(2, 2, time));
        return time;
    }

    @Override
    protected float animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        int row = pixel.getRow();
        int col = pixel.getCol();
        int pos = find(row, col, level.getRows(), level.getColumns());
        float delayPerPixel = 1.5f / (level.getRows() * level.getColumns());
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(.3f + (delayPerPixel * pos)));
        sequenceAction.addAction(Actions.fadeOut(.3f));
        pixel.addAction(sequenceAction);
        return .3f + (delayPerPixel * pos) + .3f;
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
