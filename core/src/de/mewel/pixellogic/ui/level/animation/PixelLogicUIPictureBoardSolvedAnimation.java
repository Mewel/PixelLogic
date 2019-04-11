package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;

public class PixelLogicUIPictureBoardSolvedAnimation extends PixelLogicUIBaseLevelAnimation {

    @Override
    protected float animatePixel(PixelLogicUIBoardPixel pixel) {
        PixelLogicLevel level = getLevelUI().getLevel();
        if (!level.isFilled(pixel.getRow(), pixel.getCol())) {
            pixel.addAction(Actions.fadeOut(FADE_OUT_TIME));
            return FADE_OUT_TIME;
        }
        return 0;
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

}
