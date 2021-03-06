package de.mewel.pixellogic.mode;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
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

    /**
     * Starts the mode.
     */
    public abstract void run();

    public void activate() {
        this.getEventManager().listen(this);
    }

    public void deactivate() {
        this.getEventManager().remove(this);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        // deactivate by default if page is changed
        if (event instanceof PixelLogicUIPageChangeEvent) {
            PixelLogicUIPageChangeEvent pageChangedEvent = (PixelLogicUIPageChangeEvent) event;
            if (!pageChangedEvent.getPageId().equals(PixelLogicUIPageId.level)) {
                this.deactivate();
            }
        }
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

    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

    public PixelLogicUIAppScreen getAppScreen() {
        return global.getAppScreen();
    }

    public PixelLogicGlobal getGlobal() {
        return global;
    }

}
