package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.g2d.Batch;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUIHorizontalLine extends PixelLogicUIActor {

    public PixelLogicUIHorizontalLine(PixelLogicAssets assets, PixelLogicEventManager eventManager, int height) {
        super(assets, eventManager);
        this.setHeight(height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

}
