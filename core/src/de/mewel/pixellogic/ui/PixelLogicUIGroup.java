package de.mewel.pixellogic.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public abstract class PixelLogicUIGroup extends Group implements PixelLogicUIElement {

    private PixelLogicGlobal global;

    public PixelLogicUIGroup(PixelLogicGlobal global) {
        this.global = global;
    }

    public PixelLogicGlobal getGlobal() {
        return global;
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

    @Override
    public void styleChanged(PixelLogicUIStyle style) {

    }

    @Override
    public void clear() {
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        super.clear();
    }

}
