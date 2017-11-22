package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGame;
import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

public class PixelLogicGUIBoard extends Group {

    private PixelLogicLevel level;

    private List<PixelLogicGUIBoardPixel> pixels;

    public PixelLogicGUIBoard(PixelLogicLevel level) {
        this.level = level;
        this.pixels = new ArrayList<PixelLogicGUIBoardPixel>();
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicGUIBoardPixel pixel = new PixelLogicGUIBoardPixel(level, row, col);
                float x = PIXEL_SPACE_COMBINED * col;
                float y = PIXEL_SPACE_COMBINED * row;
                pixel.setBounds(x, y, PIXEL_SIZE, PIXEL_SIZE);
                this.pixels.add(pixel);
                this.addActor(pixel);
            }
        }
    }

}
