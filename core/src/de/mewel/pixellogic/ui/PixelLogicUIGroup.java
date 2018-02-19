package de.mewel.pixellogic.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public class PixelLogicUIGroup extends Group implements PixelLogicUIElement {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIGroup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public void clear() {
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        super.clear();
    }

}
