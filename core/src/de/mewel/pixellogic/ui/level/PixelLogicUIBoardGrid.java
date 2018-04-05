package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIActor;
import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIBoardGrid extends PixelLogicUIActor {

    private static Map<Integer, Integer> GRID;

    private PixelLogicLevel level;

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

    public PixelLogicUIBoardGrid(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicLevel level) {
        super(assets, eventManager);
        this.level = level;
        this.setColor(PixelLogicUIConstants.GRID_COLOR);
        this.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        int spaceSize = resolution.getGameSpaceSize();
        int combined = resolution.getGamePixelSizeCombined();
        int xWidth = combined * level.getColumns() - spaceSize;
        int yWidth = combined * level.getRows() - spaceSize;

        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Color color = getColor();
        ShapeRenderer renderer = this.getAssets().getShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.setColor(new Color(color.r, color.g, color.b, color.a * parentAlpha));
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        Integer gridY = GRID.get(level.getRows());
        if (gridY != null) {
            for (int y = gridY; y < level.getRows(); y += gridY) {
                renderer.box(getX(), getY() + (combined * y) - spaceSize, 0, xWidth, spaceSize, 0f);
            }
        }
        Integer gridX = GRID.get(level.getColumns());
        if (gridX != null) {
            for (int x = gridX; x < level.getColumns(); x += gridX) {
                renderer.box(getX() + (combined * x) - spaceSize, getY(), 0, spaceSize, yWidth, 0f);
            }
        }

        renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

    public void update() {
        this.resolution = PixelLogicUIUtil.get(level);
    }

}
