package de.mewel.pixellogic.spike;


import com.badlogic.gdx.Game;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyleController;

public abstract class AbstractSpikeGame extends Game implements PixelLogicGlobal {

    protected PixelLogicAssets assets;

    protected PixelLogicEventManager eventManager;

    protected PixelLogicUIAppScreen appScreen;

    protected PixelLogicAchievements achievements;

    protected PixelLogicAudio audio;

    protected PixelLogicUIStyleController styleController;

    @Override
    public void create() {
        this.styleController = new PixelLogicUIStyleController();
        styleController.init();
    }

    @Override
    public void dispose() {
        if (this.audio != null) {
            this.audio.dispose();
        }
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

    @Override
    public PixelLogicAudio getAudio() {
        return audio;
    }

    @Override
    public PixelLogicUIStyle getStyle() {
        return styleController.get();
    }

    @Override
    public PixelLogicUIStyleController getStyleController() {
        return styleController;
    }

}
