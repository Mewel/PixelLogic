package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class PixelLogicGame extends Game {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen appScreen;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.appScreen = new PixelLogicUIAppScreen(assets, eventManager);
        this.setScreen(this.appScreen);
    }

    @Override
    public void dispose() {
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
        }
        super.dispose();
}

    public PixelLogicAssets getAssets() {
        return assets;
    }

    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

}
