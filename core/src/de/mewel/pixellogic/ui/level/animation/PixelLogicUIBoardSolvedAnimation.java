package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public class PixelLogicUIBoardSolvedAnimation extends PixelLogicUIBaseLevelAnimation {

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

        Color pixelColor = level.getColor(row, col);
        if (pixelColor == null || pixelColor.a == .0f) {
            pixel.addAction(Actions.fadeOut(FADE_OUT_TIME));
            return FADE_OUT_TIME;
        }
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.color(PixelLogicUIConstants.PIXEL_FILLED_COLOR, 0.1f));
        sequenceAction.addAction(Actions.delay(0.2f));
        float mult = (float) (col + row) / (float) (level.getColumns() + level.getRows());
        sequenceAction.addAction(Actions.delay(0.2f * mult));
        sequenceAction.addAction(Actions.color(Color.WHITE, 0.2f + (0.4f * mult)));
        sequenceAction.addAction(Actions.delay(0.2f * mult));
        sequenceAction.addAction(Actions.color(pixelColor, 0.2f + (0.4f * mult)));
        pixel.addAction(sequenceAction);
        return getDuration(sequenceAction);
    }

}
