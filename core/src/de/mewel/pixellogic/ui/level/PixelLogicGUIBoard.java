package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIBoard extends PixelLogicUILevelGroup {

    private PixelLogicLevel level;

    private PixelLogicGUIBoardPixel[][] pixels;

    private PixelLogicGUIBoardGrid grid;

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        this.pixels = new PixelLogicGUIBoardPixel[level.getRows()][level.getColumns()];
        this.grid = new PixelLogicGUIBoardGrid(this.level);
        this.addActor(this.grid);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicGUIBoardPixel pixel = new PixelLogicGUIBoardPixel(level, row, col);
                this.pixels[row][col] = pixel;
                this.addActor(pixel);
            }
        }
        this.addAction(Actions.fadeIn(2f));
        this.updateChildrenBounds();
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
        this.grid.addAction(Actions.fadeOut(.2f));
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event) {

    }

    @Override
    public void onLevelChange(PixelLogicLevelStatusChangeEvent event) {
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                this.pixels[row][col].updateLevelStatus(event.getStatus());
            }
        }
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateChildrenBounds();
    }

    private void updateChildrenBounds() {
        if(this.level == null) {
            return;
        }
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(this.level);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                float x = resolution.getGamePixelSizeCombined() * col;
                float y = resolution.getGamePixelSizeCombined() * row;
                PixelLogicGUIBoardPixel pixel = this.pixels[row][col];
                pixel.setBounds(x, y, resolution.getGamePixelSize(), resolution.getGamePixelSize());
            }
        }

    }

}
