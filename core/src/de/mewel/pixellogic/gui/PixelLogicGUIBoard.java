package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.model.PixelLogicLevel;

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
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), level);
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
