package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUIColoredSurface extends PixelLogicUIActor {

    int borderWidth;

    Color borderColor;

    public PixelLogicUIColoredSurface(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.borderWidth = 0;
        this.borderColor = null;
    }

    public PixelLogicUIColoredSurface setBorder(int width, Color color) {
        this.borderWidth = width;
        this.borderColor = color;
        return this;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        Color color = getColor();
        ShapeRenderer renderer = this.getAssets().getShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(getX(), getY(), 0);

        // render surface
        renderer.setColor(new Color(color.r, color.g, color.b, color.a * parentAlpha));
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.box(0f, 0f, 0, getWidth(), getHeight(), 0f);
        renderer.end();

        // render border
        if (this.borderColor != null && this.borderWidth != 0) {
            renderer.setColor(new Color(borderColor.r, borderColor.g, borderColor.b, borderColor.a * parentAlpha));
            renderer.begin(ShapeRenderer.ShapeType.Line);
            for (int i = 0; i < borderWidth; i++) {
                renderer.box(0f + i, 0f + i, 0, getWidth() - (2 * i), getHeight() - (2 * i), 0f);
            }
            renderer.end();
        }

        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

}
