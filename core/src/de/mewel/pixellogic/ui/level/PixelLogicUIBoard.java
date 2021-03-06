package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicUIBoard extends PixelLogicUILevelGroup {

    private PixelLogicLevel level;

    private PixelLogicUIBoardPixel[][] pixels;

    private PixelLogicUIBoardGrid grid;

    public PixelLogicUIBoard(PixelLogicGlobal global) {
        super(global);
        //this.setTransform(false);
    }

    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
        if (this.level == null) {
            return;
        }
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                float x = resolution.getGamePixelSizeCombined() * col;
                float y = resolution.getGamePixelSizeCombined() * row;
                PixelLogicUIBoardPixel pixel = this.pixels[row][col];
                pixel.setBounds(x, y, resolution.getGamePixelSize(), resolution.getGamePixelSize());
            }
        }
        this.grid.updateResolution(resolution);
    }

    public void setPixel(int row, int col, Boolean value) {
        this.pixels[row][col].set(value);
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        this.pixels = new PixelLogicUIBoardPixel[level.getRows()][level.getColumns()];
        this.grid = new PixelLogicUIBoardGrid(getGlobal(), this.level);
        this.addActor(this.grid);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicUIBoardPixel pixel = new PixelLogicUIBoardPixel(getGlobal(), row, col);
                pixel.set(level.get(row, col));
                this.pixels[row][col] = pixel;
                this.addActor(pixel);
            }
        }
        this.addAction(Actions.fadeIn(2f));
    }

    public void updateFromLevelPixels() {
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                this.pixels[row][col].set(level.get(row, col));
            }
        }
    }

    public void clear() {
        if (this.level == null) {
            return;
        }
        for (int row = 0; row < this.pixels.length; row++) {
            for (int col = 0; col < this.pixels[0].length; col++) {
                this.pixels[row][col].set(null);
            }
        }
    }

    public PixelLogicLevel getLevel() {
        return this.level;
    }

    public PixelLogicUIBoardGrid getGrid() {
        return grid;
    }

    public PixelLogicUIBoardPixel[][] getPixels() {
        return pixels;
    }

}
