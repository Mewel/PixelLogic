package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicUIBoardGrid extends Actor {

    private static Map<Integer, Integer> GRID;

    private PixelLogicLevel level;

    private Texture gridTexture;

    private PixelLogicUILevelResolution resolution;

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

    public PixelLogicUIBoardGrid(PixelLogicLevel level) {
        this.level = level;
        this.setColor(PixelLogicUIConstants.GRID_COLOR);
        this.gridTexture = PixelLogicUIUtil.getWhiteTexture();
        this.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int spaceSize = resolution.getGameSpaceSize();
        int combined = resolution.getGamePixelSizeCombined();
        int xWidth = combined * level.getColumns() - spaceSize;
        int yWidth = combined * level.getRows() - spaceSize;

        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        Integer gridY = GRID.get(level.getRows());
        if (gridY != null) {
            for (int y = gridY; y < level.getRows(); y += gridY) {
                batch.draw(gridTexture, getX(), getY() + (combined * y) - spaceSize, xWidth, spaceSize);
            }
        }
        Integer gridX = GRID.get(level.getColumns());
        if (gridX != null) {
            for (int x = gridX; x < level.getColumns(); x += gridX) {
                batch.draw(gridTexture, getX() + (combined * x) - spaceSize, getY(), spaceSize, yWidth);
            }
        }
        batch.setColor(color);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.update();
    }

    protected void update() {
        this.resolution = PixelLogicUIUtil.get(level);
    }

    @Override
    public void clear() {
        super.clear();
        this.gridTexture.dispose();
    }

}
