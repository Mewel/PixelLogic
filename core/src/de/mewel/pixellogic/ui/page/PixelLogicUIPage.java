package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIStage;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public abstract class PixelLogicUIPage implements PixelLogicUIElement {

    private Stage stage;

    private PixelLogicUIPageId pageId;

    private PixelLogicUIPageProperties properties;

    private PixelLogicGlobal global;

    public PixelLogicUIPage(PixelLogicGlobal global, PixelLogicUIPageId pageId) {
        this.global = global;
        this.pageId = pageId;
        this.stage = new PixelLogicUIStage(global);
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

    @Override
    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

    public PixelLogicUIAppScreen getAppScreen() {
        return global.getAppScreen();
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

    public void pause() {
    }

    public void resume() {
    }

    protected void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        getStage().setViewport(new ExtendViewport(width, height, camera));
    }

    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.getStage().getViewport().update(width, height);
        this.getStage().getRoot().setBounds(0, 0, width, height);
    }

    public float getWidth() {
        return this.getStage().getWidth();
    }

    public float getHeight() {
        return this.getStage().getHeight();
    }

    protected void fadeIn(final Runnable after) {
        getStage().getRoot().getColor().a = 0f;
        AlphaAction fadeIn = Actions.fadeIn(.2f, Interpolation.fade);
        Action action = after != null ? Actions.sequence(fadeIn, Actions.run(after)) : fadeIn;
        getStage().addAction(action);
    }

    protected void fadeOut(Runnable after) {
        AlphaAction fadeOut = Actions.fadeOut(.2f);
        Action action = after != null ? Actions.sequence(fadeOut, Actions.run(after)) : fadeOut;
        getStage().addAction(action);
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        PixelLogicUIUtil.changeStyle(this.stage.getRoot(), style);
    }

}
