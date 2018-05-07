package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUIBoard extends PixelLogicUILevelGroup {

    private PixelLogicLevel level;

    private PixelLogicUIBoardPixel[][] pixels;

    private PixelLogicUIBoardGrid grid;

    public PixelLogicUIBoard(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        this.pixels = new PixelLogicUIBoardPixel[level.getRows()][level.getColumns()];
        this.grid = new PixelLogicUIBoardGrid(getAssets(), getEventManager(), this.level);
        this.addActor(this.grid);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicUIBoardPixel pixel = new PixelLogicUIBoardPixel(getAssets(), getEventManager(), row, col);
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
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateChildrenBounds();
    }

    private void updateChildrenBounds() {
        if (this.level == null) {
            return;
        }
        PixelLogicUILevelResolution resolution = PixelLogicUIUtil.get(this.level);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                float x = resolution.getGamePixelSizeCombined() * col;
                float y = resolution.getGamePixelSizeCombined() * row;
                PixelLogicUIBoardPixel pixel = this.pixels[row][col];
                pixel.setBounds(x, y, resolution.getGamePixelSize(), resolution.getGamePixelSize());
            }
        }
        this.grid.update();
    }

    public void setPixel(int row, int col, Boolean value) {
        this.pixels[row][col].set(value);
    }

    public PixelLogicLevel getLevel() {
        return this.level;
    }

    public PixelLogicUIBoardPixel[][] getPixels() {
        return pixels;
    }

}
