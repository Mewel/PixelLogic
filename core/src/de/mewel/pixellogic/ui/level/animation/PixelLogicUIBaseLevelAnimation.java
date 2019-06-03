package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.TemporalAction;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public abstract class PixelLogicUIBaseLevelAnimation implements PixelLogicUILevelAnimation {

    protected static final float FADE_OUT_TIME = .4f;

    private PixelLogicGlobal global;

    private PixelLogicUILevelPage page;

    public PixelLogicUIBaseLevelAnimation(PixelLogicGlobal global) {
        this.global = global;
    }

    public PixelLogicGlobal getGlobal() {
        return global;
    }

    @Override
    public void setPage(PixelLogicUILevelPage page) {
        this.page = page;
    }

    @Override
    public PixelLogicUILevelPage getPage() {
        return page;
    }

    @Override
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

    @Override
    public void destroy() {
    }

    protected float centerBoard() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(0.2f));
        PixelLogicUILevel levelUI = getLevelUI();
        float x = levelUI.getWidth() / 2f - levelUI.getBoard().getWidth() / 2f;
        float y = levelUI.getHeight() / 2f - levelUI.getBoard().getHeight() / 2f;
        sequenceAction.addAction(Actions.moveTo(x, y, 0.2f));
        levelUI.getBoard().addAction(sequenceAction);
        return getDuration(sequenceAction);
    }

    public static float getDuration(SequenceAction sequenceAction) {
        float duration = .0f;
        for (Action action : sequenceAction.getActions()) {
            if (action instanceof TemporalAction) {
                duration += ((TemporalAction) action).getDuration();
            } else if (action instanceof DelayAction) {
                duration += ((DelayAction) action).getDuration();
            }
        }
        return duration;
    }

}
