package de.mewel.pixellogic.ui.level.animation;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.PixelLogicUIColumnGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUIRowGroup;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;

public abstract class PixelLogicUILevelAnimation {

    private PixelLogicUILevelPage page;

    public PixelLogicUILevelAnimation(PixelLogicUILevelPage levelPage) {
        this.page = levelPage;
    }

    public PixelLogicUILevel getLevelUI() {
        return page.getLevelUI();
    }

    public void execute() {
        centerBoard();
        getLevelUI().getBoard().getGrid().addAction(Actions.fadeOut(.2f));

        PixelLogicUIBoardPixel[][] pixels = getLevelUI().getBoard().getPixels();
        for (int row = 0; row < pixels.length; row++) {
            for (int col = 0; col < pixels[0].length; col++) {
                animatePixel(pixels[row][col]);
            }
        }
        animateRowGroup(getLevelUI().getRowGroup());
        animateColumnGroup(getLevelUI().getColumnGroup());
    }

    protected abstract void animatePixel(PixelLogicUIBoardPixel pixel);

    protected abstract void animateRowGroup(PixelLogicUIRowGroup group);

    protected abstract void animateColumnGroup(PixelLogicUIColumnGroup group);

    protected void centerBoard() {
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(0.2f));
        PixelLogicUILevel levelUI = getLevelUI();
        float x = levelUI.getWidth() / 2f - levelUI.getBoard().getWidth() / 2f;
        float y = levelUI.getHeight() / 2f - levelUI.getBoard().getHeight() / 2f;
        sequenceAction.addAction(Actions.moveTo(x, y, 0.2f));
        levelUI.getBoard().addAction(sequenceAction);
    }

}
