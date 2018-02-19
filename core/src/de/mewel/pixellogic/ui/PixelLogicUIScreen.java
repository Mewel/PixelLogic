package de.mewel.pixellogic.ui;

import com.badlogic.gdx.Screen;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public abstract class PixelLogicUIScreen implements PixelLogicUIElement, Screen {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
    }

    public abstract void activate();

    public void deactivate() {
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

}
