package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

import static de.mewel.pixellogic.PixelLogicConstants.GRID_COLOR;

public class PixelLogicUIBoardGrid extends PixelLogicUIActor {

    private static int DISTANCE = 5;

    private PixelLogicLevel level;

    private PixelLogicUILevelResolution resolution;

    public PixelLogicUIBoardGrid(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicLevel level) {
        super(assets, eventManager);
        this.level = level;
        this.setColor(new Color(GRID_COLOR));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        if (resolution == null) {
            return;
        }

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

        for (int y = level.getRows() - DISTANCE; y >= 0; y -= DISTANCE) {
            renderer.box(getX(), getY() + (combined * y) - spaceSize, 0, xWidth, spaceSize, 0f);
        }
        for (int x = DISTANCE; x < level.getColumns(); x += DISTANCE) {
            renderer.box(getX() + (combined * x) - spaceSize, getY(), 0, spaceSize, yWidth, 0f);
        }

        renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

    public void updateResolution(PixelLogicUILevelResolution resolution) {
        this.resolution = resolution;
    }

}
