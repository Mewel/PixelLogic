package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public class PixelLogicUIBoardSolvedAnimation extends PixelLogicUILevelAnimation {

    public PixelLogicUIBoardSolvedAnimation(PixelLogicUILevelPage levelPage) {
        super(levelPage);
    }

    @Override
    protected float animateColumnGroup(PixelLogicUIColumnGroup group) {
        group.addAction(Actions.fadeOut(FADE_OUT_TIME));
        return FADE_OUT_TIME;
    }

    @Override
    protected float animateRowGroup(PixelLogicUIRowGroup group) {
        group.addAction(Actions.fadeOut(FADE_OUT_TIME));
        return FADE_OUT_TIME;
    }

    @Override
    protected float animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        int row = pixel.getRow();
        int col = pixel.getCol();

        boolean isFilled = level.isFilled(row, col);
        Color pixelColor = level.getColor(row, col);
        if (!isFilled) {
            if (pixelColor == null) {
                pixel.addAction(Actions.fadeOut(FADE_OUT_TIME));
                return FADE_OUT_TIME;
            } else {
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.fadeOut(FADE_OUT_TIME));
                sequenceAction.addAction(Actions.delay(0.2f));
                sequenceAction.addAction(Actions.color(pixelColor, 0.6f));
                pixel.addAction(sequenceAction);
                return FADE_OUT_TIME + .8f;
            }
        } else {
            if (pixelColor != null) {
                SequenceAction sequenceAction = new SequenceAction();
                sequenceAction.addAction(Actions.delay(0.3f));
                float mult = (float) (col + row) / (float) (level.getColumns() + level.getRows());
                sequenceAction.addAction(Actions.delay(0.2f * mult));
                sequenceAction.addAction(Actions.color(Color.WHITE, 0.2f + (0.4f * mult)));
                sequenceAction.addAction(Actions.delay(0.2f * mult));
                sequenceAction.addAction(Actions.color(pixelColor, 0.2f + (0.4f * mult)));
                pixel.addAction(sequenceAction);
                return .3f + 0.2f * mult + 0.2f + (0.4f * mult) + 0.2f * mult + 0.2f + (0.4f * mult);
            }
        }
        return 0f;
    }

}
