package de.mewel.pixellogic.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public class PixelLogicUIActor extends Actor implements PixelLogicUIElement {

    private PixelLogicGlobal global;

    public PixelLogicUIActor(PixelLogicGlobal global) {
        this.global = global;
    }

    @Override
    public PixelLogicAssets getAssets() {
        return global.getAssets();
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return global.getEventManager();
    }

    @Override
    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

    public PixelLogicGlobal getGlobal() {
        return global;
    }

}
