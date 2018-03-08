package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUIHorizontalLine extends PixelLogicUIActor {

    private ShapeRenderer renderer;

    public PixelLogicUIHorizontalLine(PixelLogicAssets assets, PixelLogicEventManager eventManager, int height) {
        super(assets, eventManager);
        this.setHeight(height);
        this.renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();

        this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.renderer.setTransformMatrix(batch.getTransformMatrix());
        this.renderer.translate(getX(), getY(), 0);
        this.renderer.begin(ShapeRenderer.ShapeType.Line);
        this.renderer.box(0f, 0f, 0, getWidth(), getHeight(), 0f);
        this.renderer.end();

        batch.begin();

        /*batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

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
        }*/
    }

}
