package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public class PixelLogicUIBoardSolvedAnimation extends PixelLogicUILevelAnimation {

    public PixelLogicUIBoardSolvedAnimation(PixelLogicUILevel levelUI) {
        super(levelUI);
    }

    @Override
    protected void animateColumnGroup(PixelLogicUIColumnGroup group) {
        group.addAction(Actions.fadeOut(0.4f));
    }

    @Override
    protected void animateRowGroup(PixelLogicUIRowGroup group) {
        group.addAction(Actions.fadeOut(0.4f));
    }

    @Override
    protected void animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        int row = pixel.getRow();
        int col = pixel.getCol();

        boolean isFilled = level.isFilled(row, col);
        Color pixelColor = level.getColor(row, col);
        if (!isFilled) {
            if (pixelColor == null) {
                pixel.addAction(Actions.fadeOut(0.4f));
            } else {
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.fadeOut(0.4f));
                sequenceAction.addAction(Actions.delay(0.2f));
                sequenceAction.addAction(Actions.color(pixelColor, 0.6f));
                pixel.addAction(sequenceAction);
            }
        }
        if (isFilled) {
            if (pixelColor != null) {
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                float mult = (float) (col + row) / (float) (level.getColumns() + level.getRows());
                sequenceAction.addAction(Actions.delay(0.2f * mult));
                sequenceAction.addAction(Actions.color(Color.WHITE, 0.2f + (0.4f * mult)));
                sequenceAction.addAction(Actions.delay(0.2f * mult));
                sequenceAction.addAction(Actions.color(pixelColor, 0.2f + (0.4f * mult)));
                pixel.addAction(sequenceAction);
            }
        }
    }

}
