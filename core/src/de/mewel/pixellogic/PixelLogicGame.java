package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenManager;
import de.mewel.pixellogic.ui.screen.event.PixelLogicStartTimeTrialModeEvent;
import de.mewel.pixellogic.ui.screen.event.PixelLogicTimeTrialFinishedEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreen;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenData;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialFinishedScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialScreen;

public class PixelLogicGame extends Game implements PixelLogicListener {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIScreenManager screenManager;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();
        this.eventManager.listen(this);

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.screenManager = new PixelLogicUIScreenManager();

        PixelLogicUILevelScreen levelScreen = new PixelLogicUILevelScreen(assets, eventManager);
        PixelLogicUITimeTrialScreen timeTrialScreen = new PixelLogicUITimeTrialScreen(assets, eventManager);
        PixelLogicUITimeTrialFinishedScreen timeTrialFinishedScreen = new PixelLogicUITimeTrialFinishedScreen(assets, eventManager);

        this.screenManager.add("levelScreen", levelScreen);
        this.screenManager.add("timeTrialScreen", timeTrialScreen);
        this.screenManager.add("timeTrialFinishedScreen", timeTrialFinishedScreen);

        this.screenManager.activate(timeTrialScreen, null);
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
