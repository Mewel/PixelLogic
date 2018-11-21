package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;
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
        float time = super.execute();

        PixelLogicUILevelPage page = getPage();
        PixelLogicUILevel level = getLevelUI();
        PixelLogicUIBoard board = level.getBoard();
        SequenceAction boardAnimation = new SequenceAction();
        boardAnimation.addAction(Actions.delay(time + .2f));
        boardAnimation.addAction(Actions.scaleTo(.25f, .25f, 1.5f));
        board.addAction(boardAnimation);

        PixelLogicLevelCollection collection = page.getProperties().get("pictureCollection");
        /*PixelLogicUISprite picture = new PixelLogicUISprite(page.getAssets(), page.getEventManager());
        picture.setSprite(new Sprite(new Texture(collection.getPixmap())));
       */

        picture = new PixelLogicUIPicture(page.getAssets(), page.getEventManager(), collection);
        float size = page.getWidth();
        picture.setSize(size, size);
        picture.setPosition(0, page.getHeight() / 2 - size / 2);
        picture.getColor().a = 0f;
        page.getRoot().addActorBefore(level, picture);

        picture.addAction(new SequenceAction(Actions.delay(time), Actions.fadeIn(.2f)));

        return time;
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
