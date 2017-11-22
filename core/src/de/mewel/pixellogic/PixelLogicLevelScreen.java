package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import de.mewel.pixellogic.gui.PixelLogicLevelStage;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.LINE_COLOR;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;

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

        levelStage.getViewport().update(width, height);
        Camera camera = levelStage.getCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
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
