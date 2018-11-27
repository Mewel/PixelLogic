package de.mewel.pixellogic.spike;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicCharactersMode;
import de.mewel.pixellogic.mode.PixelLogicPictureMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class PicturesSpike extends Game implements PixelLogicGlobal {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen appScreen;

    private PixelLogicAchievements achievements;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(eventManager);

        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        PixelLogicLevelCollection collection = getAssets().getLevelCollection("pictures/davinci");

        final PixelLogicPictureMode mode = new PixelLogicPictureMode(collection);
        mode.setup(this);
        mode.reset();
        mode.activate();
        mode.run();

        PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
        pageProperties.put("solvedAnimation", "picture");
        pageProperties.put("pictureCollection", mode.getCollection());
        pageProperties.put("menuBackId", PixelLogicUIPageId.mainMenu);
        getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties);
    }

    @Override
    public void dispose() {
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
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

    @Override
    public PixelLogicUIAppScreen getAppScreen() {
        return appScreen;
    }

    @Override
    public PixelLogicAchievements getAchievements() {
        return achievements;
    }


}
