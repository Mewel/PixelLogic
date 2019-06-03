package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.ui.style.PixelLogicUILightStyle;

public class PixelLogicUISplashScreen implements Screen {

    private Stage stage;

    private SpriteBatch batch;

    private Texture logoTexture;

    private Image logoImage;

    private Color backgroundColor;

    public PixelLogicUISplashScreen() {
        stage = new Stage();
        batch = new SpriteBatch();
        logoTexture = new Texture("logo_light.png");
        logoImage = new Image(logoTexture);
        logoImage.setOrigin(Align.center);
        stage.addActor(logoImage);
        backgroundColor = new PixelLogicUILightStyle().getBackgroundColor();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(backgroundColor.r, backgroundColor.g, backgroundColor.b, backgroundColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        stage.act(delta);
        batch.begin();
        stage.draw();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.stage.getViewport().update(width, height);
        this.stage.getRoot().setBounds(0, 0, width, height);

        this.logoImage.setScale((int) (height / (this.logoImage.getHeight() * 6f)));
        this.logoImage.setX(width / 2);
        this.logoImage.setY(height / 1.667f);
    }

    private void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        this.stage.setViewport(new ExtendViewport(width, height, camera));
    }

    public void close(float fadeOutTime, Runnable after) {
        SequenceAction sequence = Actions.sequence(Actions.fadeOut(fadeOutTime), Actions.run(after));
        this.logoImage.addAction(sequence);
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
        if (this.logoTexture != null) {
            this.logoTexture.dispose();
        }
    }
}
