package de.mewel.pixellogic.ui.level.animation;

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
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(0.1f * pos));
        sequenceAction.addAction(Actions.fadeOut(0.4f));
        pixel.addAction(sequenceAction);
    }

    @Override
    protected void animateRowGroup(PixelLogicUIRowGroup group) {

    }

    @Override
    protected void animateColumnGroup(PixelLogicUIColumnGroup group) {

    }

    protected int find(int row, int col, int rows, int cols) {
        int dir = 1;
        int x = 0;
        int y = 0;
        int num = 0;
        while (y != row && x != col) {
            if (dir == 1) {
                x++;
                if (x > cols) {
                    x--;
                    dir = 2;
                }
            } else if (dir == 2) {
                y++;
                if (y > rows) {
                    y--;
                    dir = 3;
                }
            } else if (dir == 3) {
                x--;
                if (x < 0) {
                    x = 0;
                    dir = 4;
                }
            } else if (dir == 4) {
                y--;
                if (y < 0) {
                    y = 0;
                    dir = 1;
                }
            }
            num++;
        }
        return num;
    }

}
