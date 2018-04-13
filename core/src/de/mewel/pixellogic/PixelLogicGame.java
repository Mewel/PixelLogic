package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenId;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenManager;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialFinishedScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialScreen;

public class PixelLogicGame extends Game {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIScreenManager screenManager;

    private PixelLogicAchievements achievements;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(eventManager);

        this.screenManager = new PixelLogicUIScreenManager(this, assets, eventManager);

        PixelLogicUILevelScreen levelScreen = new PixelLogicUILevelScreen(assets, eventManager);
        PixelLogicUITimeTrialScreen timeTrialScreen = new PixelLogicUITimeTrialScreen(assets, eventManager);
        PixelLogicUITimeTrialFinishedScreen timeTrialFinishedScreen = new PixelLogicUITimeTrialFinishedScreen(assets, eventManager);

        this.screenManager.add(PixelLogicScreenId.level, levelScreen);
        this.screenManager.add(PixelLogicScreenId.timeTrial, timeTrialScreen);
        this.screenManager.add(PixelLogicScreenId.timeTrialFinished, timeTrialFinishedScreen);

        this.screenManager.activate(timeTrialScreen, new PixelLogicUIScreenProperties());
    }

    @Override
    public void dispose() {
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
        }
        if (this.screenManager != null) {
            this.screenManager.dispose();
        }
        if (this.achievements != null) {
            this.achievements.dispose();
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
