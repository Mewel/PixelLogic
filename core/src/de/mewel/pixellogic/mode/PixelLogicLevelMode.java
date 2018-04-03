package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;

public abstract class PixelLogicLevelMode implements PixelLogicListener {

    protected PixelLogicLevel level;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.getEventManager().listen(this);
    }

    protected void dispose() {
        this.getEventManager().remove(this);
    }

    abstract void run();

    public void runLevel(final PixelLogicLevel level) {
        this.level = level;
        getEventManager().fire(new PixelLogicNextLevelEvent(PixelLogicLevelMode.this, level));
    }

    public PixelLogicLevel getLevel() {
        return level;
    }

    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    public PixelLogicAssets getAssets() {
        return assets;
    }

}
