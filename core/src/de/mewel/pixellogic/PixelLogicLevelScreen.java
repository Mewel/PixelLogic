package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.gui.PixelLogicGUILevel;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicLevelScreen implements Screen {

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private Stage stage;

    private PixelLogicGUILevel level;

    public PixelLogicLevelScreen() {
        this.level = new PixelLogicGUILevel();
        this.stage = new Stage();
        this.stage.addActor(level);
        Gdx.input.setInputProcessor(this.stage);
    }

    private void initViewport() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        stage.setViewport(new ExtendViewport(screenWidth, screenHeight, camera));
    }

    public void load(PixelLogicLevel level) {
        initViewport();
        this.level.load(level);
        this.updateLevelBounds();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
        ((OrthographicCamera) stage.getCamera()).setToOrtho(true, width, height);
        this.updateLevelBounds();
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
        stage.dispose();
    }

    private void updateLevelBounds() {
        int levelMaxWidth = Gdx.graphics.getWidth();
        int levelMaxHeight = Gdx.graphics.getHeight() - 50;
        this.level.resize(levelMaxWidth, levelMaxHeight);
        Gdx.app.log("updatelevelbound", "width " + this.level.getWidth());
        float x = levelMaxWidth / 2f - this.level.getWidth() / 2f;
        float y = levelMaxHeight / 2f - this.level.getHeight() / 2f;
        this.level.setPosition(x, y);
    }

}
