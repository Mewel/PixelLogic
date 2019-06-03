package de.mewel.pixellogic.ui.screen;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.layer.PixelLogicUILayer;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public abstract class PixelLogicUILayeredScreen extends PixelLogicUIScreen {

    protected List<PixelLogicUILayer> layers;

    public PixelLogicUILayeredScreen(PixelLogicGlobal global) {
        super(global);
        this.layers = new ArrayList<PixelLogicUILayer>();
    }

    public void add(PixelLogicUILayer layer) {
        this.layers.add(layer);
    }

    public List<PixelLogicUILayer> getLayers() {
        return layers;
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        for (PixelLogicUILayer layer : getLayers()) {
            layer.styleChanged(style);
        }
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
