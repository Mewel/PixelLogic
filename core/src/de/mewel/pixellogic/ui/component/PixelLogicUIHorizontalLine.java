package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUIHorizontalLine extends PixelLogicUIActor {

    private ShapeRenderer renderer;

    public PixelLogicUIHorizontalLine(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.renderer = new ShapeRenderer();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        this.renderer.setProjectionMatrix(batch.getProjectionMatrix());
        this.renderer.setTransformMatrix(batch.getTransformMatrix());
        this.renderer.setColor(getColor());
        this.renderer.translate(getX(), getY(), 0);
        this.renderer.begin(ShapeRenderer.ShapeType.Filled);
        this.renderer.box(0f, 0f, 0, getWidth(), getHeight(), 0f);
        this.renderer.end();
        batch.begin();
    }

    @Override
    public void clear() {
        this.renderer.dispose();
        super.clear();
    }

}
