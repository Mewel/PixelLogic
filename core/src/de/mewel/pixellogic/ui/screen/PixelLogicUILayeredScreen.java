package de.mewel.pixellogic.ui.screen;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.layer.PixelLogicUILayer;

public abstract class PixelLogicUILayeredScreen extends PixelLogicUIScreen {

    protected List<PixelLogicUILayer> layers;

    public PixelLogicUILayeredScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.layers = new ArrayList<PixelLogicUILayer>();
    }

    public void add(PixelLogicUILayer layer) {
        this.layers.add(layer);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        for (PixelLogicUILayer layer : layers) {
            layer.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (PixelLogicUILayer layer : layers) {
            layer.resize(width, height);
        }
    }

    @Override
    public void pause() {
        super.pause();
        for (PixelLogicUILayer layer : layers) {
            layer.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        for (PixelLogicUILayer layer : layers) {
            layer.resume();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        for (PixelLogicUILayer layer : layers) {
            layer.dispose();
        }
    }

}
