package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoard;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.misc.PixelLogicUIPicture;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public class PixelLogicUIPictureBoardSolvedAnimation extends PixelLogicUIBaseLevelAnimation {

    private PixelLogicUIPicture picture;

    @Override
    public float execute() {
        final float baseAnimationExecutionTime = super.execute();

        PixelLogicUILevelPage page = getPage();
        PixelLogicUILevel level = getLevelUI();
        final PixelLogicUIBoard board = level.getBoard();

        PixelLogicLevelCollection collection = page.getProperties().get("pictureCollection");

        picture = new PixelLogicUIPicture(page.getAssets(), page.getEventManager(), collection);
        float size = page.getWidth();
        picture.setSize(size, size);
        picture.setPosition(0, page.getHeight() / 2 - size / 2);
        picture.getColor().a = 0f;
        page.getRoot().addActorBefore(level, picture);

        picture.addAction(new SequenceAction(
                Actions.delay(baseAnimationExecutionTime),
                Actions.fadeIn(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        picture.solveNext(board, baseAnimationExecutionTime + .2f);
                    }
                })));

        return baseAnimationExecutionTime;
    }

    @Override
    public void destroy() {
        if (picture != null) {
            picture.addAction(new SequenceAction(Actions.fadeOut(.2f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    getPage().getRoot().removeActor(picture);
                }
            })));
        }
    }

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
