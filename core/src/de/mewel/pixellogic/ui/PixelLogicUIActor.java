package de.mewel.pixellogic.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public class PixelLogicUIActor extends Actor implements PixelLogicUIElement {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIActor(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
    }

    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }
}
