package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SCALE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

public class PixelLogicGUIBoard extends Group {

    private PixelLogicLevel level;

    private PixelLogicGUIBoardPixel[][] pixels;

    public PixelLogicGUIBoard(PixelLogicLevel level) {
        this.level = level;
        this.pixels = new PixelLogicGUIBoardPixel[level.getRows()][level.getColumns()];
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicGUIBoardPixel pixel = new PixelLogicGUIBoardPixel(level, row, col);
                this.pixels[row][col] = pixel;
                this.addActor(pixel);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                float x = (PIXEL_SPACE_COMBINED * PIXEL_SCALE) * col;
                float y = (PIXEL_SPACE_COMBINED * PIXEL_SCALE) * row;
                PixelLogicGUIBoardPixel pixel = this.pixels[row][col];
                pixel.setBounds(x, y, (PIXEL_SIZE * PIXEL_SCALE), (PIXEL_SIZE * PIXEL_SCALE));
            }
        }
    }

}
