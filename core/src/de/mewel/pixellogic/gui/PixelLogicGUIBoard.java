package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.gui.screen.PixelLogicLevelStatus;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIBoard extends Group implements PixelLogicLevelChangeListener{

    private static Map<Integer, Integer> GRID;

    static {
        GRID = new HashMap<Integer, Integer>();
        GRID.put(6, 3);
        GRID.put(7, 3);
        GRID.put(8, 4);
        GRID.put(9, 3);
        GRID.put(10, 5);
        GRID.put(11, 5);
        GRID.put(12, 4);
        GRID.put(13, 5);
        GRID.put(14, 5);
        GRID.put(15, 5);
        GRID.put(16, 4);
    }

    private PixelLogicLevel level;

    private PixelLogicLevelStatus levelStatus;

    private PixelLogicGUIBoardPixel[][] pixels;

    private Texture gridTexture;

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicGUIBoard() {
        this.gridTexture = PixelLogicGUIUtil.getWhiteTexture();
        this.setColor(PixelLogicGUIConstants.GRID_COLOR);
        this.changeAdapter = new PixelLogicLevelChangeAdapter();
        this.changeAdapter.bind(this);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelChangeEvent event) {
        this.level = event.getLevel();
        this.pixels = new PixelLogicGUIBoardPixel[level.getRows()][level.getColumns()];
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                PixelLogicGUIBoardPixel pixel = new PixelLogicGUIBoardPixel(level, row, col);
                this.pixels[row][col] = pixel;
                this.addActor(pixel);
            }
        }
        this.addAction(Actions.fadeIn(2f));
    }

    @Override
    public void onLevelSolved(PixelLogicLevelChangeEvent event) {

    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelChangeEvent event) {

    }

    @Override
    public void onLevelChange(PixelLogicLevelChangeEvent event) {
        this.levelStatus = event.getStatus();
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                this.pixels[row][col].updateLevelStatus(event.getStatus());
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if(PixelLogicLevelStatus.playable.equals(this.levelStatus)) {
            drawGrid(batch, parentAlpha);
        }
    }

    private void drawGrid(Batch batch, float parentAlpha) {
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        int spaceSize = resolution.getGameSpaceSize();
        int combined = resolution.getGamePixelSizeCombined();
        int xWidth = combined * level.getColumns() - spaceSize;
        int yWidth = combined * level.getRows() - spaceSize;

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        Integer gridY = GRID.get(level.getRows());
        for (int y = gridY; y < level.getRows(); y += gridY) {
            batch.draw(gridTexture, getX(), getY() + (combined * y) - spaceSize, xWidth, spaceSize);
        }
        Integer gridX = GRID.get(level.getColumns());
        for (int x = gridX; x < level.getColumns(); x += gridX) {
            batch.draw(gridTexture, getX() + (combined * x) - spaceSize, getY(), spaceSize, yWidth);
        }
        batch.setColor(color);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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

    @Override
    public void clear() {
        super.clear();
        this.gridTexture.dispose();
        this.changeAdapter.unbind();
    }
}
