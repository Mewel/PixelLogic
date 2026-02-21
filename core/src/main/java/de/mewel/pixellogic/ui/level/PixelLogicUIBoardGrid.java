package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIActor;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUIBoardGrid extends PixelLogicUIActor {

    private static final int DISTANCE = 5;

    private final PixelLogicLevel level;

    private PixelLogicUILevelResolution resolution;

    public PixelLogicUIBoardGrid(PixelLogicGlobal global, PixelLogicLevel level) {
        super(global);
        this.level = level;
        this.styleChanged(global.getStyle());
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
        renderer.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        renderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int y = level.getRows() - DISTANCE; y >= 1; y -= DISTANCE) {
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

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        this.setColor(new Color(style.getGridColor()));
    }

}
