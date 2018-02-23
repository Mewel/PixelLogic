package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;

public abstract class PixelLogicUIScreen implements PixelLogicUIElement, Screen {

    protected final static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private Stage stage;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.stage = new Stage();
    }

    public abstract void activate(PixelLogicUIScreenData data);

    public void deactivate(Runnable after) {
        after.run();
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
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public Group getRoot() {
        return getStage().getRoot();
    }

    @Override
    public void dispose() {
        this.getStage().dispose();
    }

    protected void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        getStage().setViewport(new ExtendViewport(width, height, camera));
    }

    @Override
    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.getStage().getViewport().update(width, height);
    }

}
