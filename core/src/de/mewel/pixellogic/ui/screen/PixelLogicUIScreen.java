package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;

public abstract class PixelLogicUIScreen implements Screen, PixelLogicUIElement {

    protected final static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL30.GL_SRC_ALPHA, GL30.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

}
