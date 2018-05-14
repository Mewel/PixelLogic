package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;

public abstract class PixelLogicUIPage implements PixelLogicUIElement {

    private Stage stage;

    private PixelLogicUIPageId pageId;

    private PixelLogicUIPageProperties properties;

    private PixelLogicGlobal global;

    public PixelLogicUIPage(PixelLogicGlobal global, PixelLogicUIPageId pageId) {
        this.global = global;
        this.pageId = pageId;
        this.stage = new Stage();
        this.properties = new PixelLogicUIPageProperties();
    }

    public void activate(PixelLogicUIPageProperties properties) {
        this.properties = properties != null ? properties : new PixelLogicUIPageProperties();
        Gdx.input.setInputProcessor(getStage());
    }

    public void deactivate(Runnable after) {
        after.run();
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

    public PixelLogicUIPageProperties getProperties() {
        return properties;
    }

    public PixelLogicUIPageId getPageId() {
        return pageId;
    }

    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public Group getRoot() {
        return getStage().getRoot();
    }

    public void dispose() {
        this.getStage().dispose();
    }

    protected void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        getStage().setViewport(new ExtendViewport(width, height, camera));
    }

    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.getStage().getViewport().update(width, height);
    }

}
