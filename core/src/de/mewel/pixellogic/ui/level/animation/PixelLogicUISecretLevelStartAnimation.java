package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public class PixelLogicUISecretLevelStartAnimation extends PixelLogicUILevelAnimation {

    public PixelLogicUISecretLevelStartAnimation(PixelLogicUILevel levelUI) {
        super(levelUI);
    }

    @Override
    protected void animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        int row = pixel.getRow();
        int col = pixel.getCol();
        int pos = find(row, col, level.getRows(), level.getColumns());
        float delayPerPixel = 2f / (level.getRows() * level.getColumns());
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(delayPerPixel * pos));
        sequenceAction.addAction(Actions.fadeOut(0.4f));
        pixel.addAction(sequenceAction);
    }

    @Override
    protected void animateRowGroup(PixelLogicUIRowGroup group) {
        group.addAction(Actions.fadeOut(0.4f));
    }

    @Override
    protected void animateColumnGroup(PixelLogicUIColumnGroup group) {
        group.addAction(Actions.fadeOut(0.4f));
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
