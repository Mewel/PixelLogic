package de.mewel.pixellogic.mode;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public abstract class PixelLogicLevelMode implements PixelLogicListener {

    protected PixelLogicLevel level;

    private PixelLogicGlobal global;

    protected boolean setupDone;

    public PixelLogicLevelMode() {
        this.setupDone = false;
    }

    public void setup(PixelLogicGlobal global) {
        this.global = global;
    }

    public abstract void run();

    public void activate() {
        this.getEventManager().listen(this);
    }

    public void deactivate() {
        this.getEventManager().remove(this);
    }

    protected void runLevel(final PixelLogicLevel level) {
        this.level = level;
        getEventManager().fire(new PixelLogicNextLevelEvent(PixelLogicLevelMode.this, level));
    }

    public PixelLogicLevel getLevel() {
        return level;
    }

    public PixelLogicEventManager getEventManager() {
        return global.getEventManager();
    }

    public PixelLogicAssets getAssets() {
        return global.getAssets();
    }

    public PixelLogicUIAppScreen getAppScreen() {
        return global.getAppScreen();
    }

}
