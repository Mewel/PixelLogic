package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIScreen;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;

public class PixelLogicGame extends Game implements PixelLogicListener {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIScreen activeScreen;

    private PixelLogicUILevelScreen levelScreen;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();
        this.eventManager.listen(this);

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.levelScreen = new PixelLogicUILevelScreen(assets, eventManager);
        activateScreen(this.levelScreen);

        PixelLogicLevelMode mode = new PixelLogicCampaignMode();
        mode.setup(assets, eventManager);
        mode.run();
    }

    private void activateScreen(PixelLogicUILevelScreen screen) {
        if (this.activeScreen != null) {
            this.activeScreen.deactivate();
        }
        this.activeScreen = screen;
        this.activeScreen.activate();
        this.setScreen(this.activeScreen);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicNextLevelEvent) {
            PixelLogicNextLevelEvent nextLevelEvent = (PixelLogicNextLevelEvent) event;
            PixelLogicLevel nextLevel = nextLevelEvent.getNextLevel();
            if (this.activeScreen == this.levelScreen) {
                this.levelScreen.loadLevel(nextLevel);
            }
        }
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
