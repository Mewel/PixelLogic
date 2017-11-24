package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;

import de.mewel.pixellogic.gui.PixelLogicLevelStage;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicLevelScreen implements Screen {

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private PixelLogicLevelStage levelStage;

    public PixelLogicLevelScreen() {
        this.levelStage = new PixelLogicLevelStage();
        Gdx.input.setInputProcessor(this.levelStage);
    }

    public void load(PixelLogicLevel level) {
        this.levelStage.load(level);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        levelStage.act(delta);
        levelStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        levelStage.resize(width, height);
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
        levelStage.dispose();
    }

}
