package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIElement;

public class PixelLogicUIStage extends Stage implements PixelLogicUIElement {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIStage(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            getEventManager().fire(new PixelLogicUserEvent(this, PixelLogicUserEvent.Type.BACK_BUTTON_CLICKED));
            return true;
        }
        return super.keyDown(keycode);
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

}
