package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public abstract class PixelLogicUILevelAnimation {

    protected static final float FADE_OUT_TIME = .4f;

    private PixelLogicUILevelPage page;

    public PixelLogicUILevelAnimation(PixelLogicUILevelPage levelPage) {
        this.page = levelPage;
    }

    public PixelLogicUILevel getLevelUI() {
        return page.getLevelUI();
    }

    /**
     * Executes the animation.
     *
     * @return the total execution time.
     */
    public float execute() {
        float executionTime = 0f;
        executionTime = Math.max(centerBoard(), executionTime);
        getLevelUI().getBoard().getGrid().addAction(Actions.fadeOut(.2f));

        PixelLogicUIBoardPixel[][] pixels = getLevelUI().getBoard().getPixels();
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                executionTime = Math.max(animatePixel(pixels[row][col]), executionTime);
            }
        }
        executionTime = Math.max(animateRowGroup(getLevelUI().getRowGroup()), executionTime);
        executionTime = Math.max(animateColumnGroup(getLevelUI().getColumnGroup()), executionTime);
        return executionTime;
    }

    protected abstract float animatePixel(PixelLogicUIBoardPixel pixel);

    protected abstract float animateRowGroup(PixelLogicUIRowGroup group);

    protected abstract float animateColumnGroup(PixelLogicUIColumnGroup group);

    protected float centerBoard() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(0.2f));
        PixelLogicUILevel levelUI = getLevelUI();
        float x = levelUI.getWidth() / 2f - levelUI.getBoard().getWidth() / 2f;
        float y = levelUI.getHeight() / 2f - levelUI.getBoard().getHeight() / 2f;
        sequenceAction.addAction(Actions.moveTo(x, y, 0.2f));
        levelUI.getBoard().addAction(sequenceAction);
        return .4f;
    }

}
