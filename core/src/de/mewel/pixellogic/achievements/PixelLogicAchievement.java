package de.mewel.pixellogic.achievements;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;

public abstract class PixelLogicAchievement {

    private PixelLogicAssets assets;

    private boolean done;

    public PixelLogicAchievement(PixelLogicAssets assets) {
        this.done = false;
        this.assets = assets;
    }

    public abstract String getId();

    public String getName() {
        return getAssets().translate("achievement." + getId() + ".title");
    }

    public String getDescription() {
        return getAssets().translate("achievement." + getId() + ".description");
    }

    abstract boolean check(PixelLogicEvent event);

    public void achieved() {
        this.done = true;
    }

    public boolean isDone() {
        return this.done;
    }

    public PixelLogicAssets getAssets() {
        return assets;
    }

}
