package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.gui.PixelLogicGUILevel;
import de.mewel.pixellogic.gui.PixelLogicGUILevelToolbar;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicLevelScreen implements Screen {

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private final SpriteBatch spriteBatch;

    private Stage stage;

    private PixelLogicGUILevel level;

    private Table table;

    private PixelLogicGUILevelToolbar toolbar;

    private Texture backgroundTexture;

    private Image backgroundImage;

    public PixelLogicLevelScreen() {
        this.spriteBatch = new SpriteBatch();
        this.backgroundTexture = new Texture(Gdx.files.internal("background/level_1.jpg"));

        this.stage = new Stage();
        this.level = new PixelLogicGUILevel();
        this.table = new Table();
        this.table.setFillParent(true);
        this.toolbar = new PixelLogicGUILevelToolbar();
        this.toolbar.setColor(1, 1, 1, 0.7f);
        this.backgroundImage = new Image(backgroundTexture);
        this.backgroundImage.setFillParent(true);
        this.backgroundImage.setScaling(Scaling.fill);
        this.backgroundImage.setPosition(this.backgroundImage.getImageWidth(), 0);

        this.stage.addActor(backgroundImage);
        this.stage.addActor(level);
        this.stage.addActor(table);
        this.table.addActor(toolbar);

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
        this.updateBounds();
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
        this.updateBounds();
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
        this.stage.dispose();
        this.spriteBatch.dispose();
        this.backgroundTexture.dispose();
    }

    private void updateBounds() {
        // toolbar
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        int toolbarHeight = Math.max(MathUtils.floor(screenHeight / 240f) * 24, 48);
        int toolbarPaddingTop = toolbarHeight / 10;
        this.toolbar.setBounds(0, screenHeight - toolbarHeight, screenWidth, toolbarHeight);

        // level
        int levelMaxHeight = screenHeight - (int) this.toolbar.getHeight() - toolbarPaddingTop;
        this.level.resize(screenWidth, levelMaxHeight);
        float x = screenWidth / 2f - this.level.getWidth() / 2f;
        float y = levelMaxHeight / 2f - this.level.getHeight() / 2f;
        this.level.setPosition(x, y);
    }

}
