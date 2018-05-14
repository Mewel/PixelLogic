package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class PixelLogicGame extends Game implements PixelLogicGlobal {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen appScreen;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();
        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        this.appScreen.setPage(PixelLogicUIPageId.timeTrial, new PixelLogicUIPageProperties());
    }

    @Override
    public void dispose() {
        if (this.eventManager != null) {
            this.eventManager.dispose();
        }
        if (this.appScreen != null) {
            this.appScreen.dispose();
        }
        if (this.assets != null) {
            this.assets.dispose();
        }
        super.dispose();
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public PixelLogicUIAppScreen getAppScreen() {
        return appScreen;
    }

}
