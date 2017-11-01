package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.model.Pixel;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGameScreen implements Screen, InputProcessor {

    private static int PIXEL_SIZE = 32;
    private static int PIXEL_SPACE = 2;
    private static int PIXEL_SPACE_COMBINED = PIXEL_SIZE + PIXEL_SPACE;
    private static int PIXEL_FULL_PADDING = 0;

    // drawing stuff
    private SpriteBatch batch;

    private Sprite backgroundPixelSprite;
    private Sprite fullPixelSprite;
    private Sprite rowInfoSprite;
    private Sprite colInfoSprite;

    private Texture backgroundPixelTexture;
    private Texture fullPixelTexture;
    private Texture rowInfoTexture;
    private Texture colInfoTexture;

    private BitmapFont numberFont;

    // camera and viewport
    private OrthographicCamera camera;
    private FitViewport viewport;
    private int gameWidth, gameHeight, viewportWidth, viewportHeight;

    // gui
    private Stage stage;
    private Skin skin;

    // game stuff
    private Pixel selectedPixelType = Pixel.FULL;
    private Pixel currentPixelType = Pixel.FULL;
    private PixelLogicLevel level;

    public PixelLogicGameScreen() {
        level = new PixelLogicLevel(PixelLogicUtil.invalidSampleLevel());

        initViewport();
        initSprites();
        initFonts();
        initGUI();

        // activate user input
        Gdx.input.setInputProcessor(this);
    }

    private void checkGame() {
        if (level.isSolved()) {
            Gdx.app.log("Game", "SOLVED!");
        }
    }

    private void initViewport() {
        gameWidth = level.getColumns() * (PIXEL_SPACE_COMBINED) - PIXEL_SPACE;
        gameHeight = level.getRows() * (PIXEL_SPACE_COMBINED) - PIXEL_SPACE;
        viewportWidth = gameWidth + ((PIXEL_SIZE) * 3);
        viewportHeight = gameHeight + ((PIXEL_SIZE) * 3);
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        viewport = new FitViewport(viewportWidth, viewportHeight, camera);
        viewport.apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    private void initSprites() {
        batch = new SpriteBatch();
        // empty pixel
        Pixmap pixelPixmap = new Pixmap(PIXEL_SIZE, PIXEL_SIZE, Pixmap.Format.RGBA8888);
        pixelPixmap.setColor(Color.RED);
        pixelPixmap.fill();
        backgroundPixelTexture = new Texture(pixelPixmap);
        backgroundPixelSprite = new Sprite(backgroundPixelTexture);
        pixelPixmap.dispose();

        // full pixel
        Pixmap fullPixelPixmap = new Pixmap(PIXEL_SIZE - (PIXEL_FULL_PADDING * 2), PIXEL_SIZE - (PIXEL_FULL_PADDING * 2), Pixmap.Format.RGBA8888);
        fullPixelPixmap.setColor(Color.BLACK);
        fullPixelPixmap.fill();
        fullPixelTexture = new Texture(fullPixelPixmap);
        fullPixelSprite = new Sprite(fullPixelTexture);
        fullPixelPixmap.dispose();

        // row info
        Pixmap rowInfoPixmap = new Pixmap(PIXEL_SIZE * 2, PIXEL_SIZE, Pixmap.Format.RGBA8888);
        rowInfoPixmap.setColor(Color.LIGHT_GRAY);
        rowInfoPixmap.fill();
        rowInfoTexture = new Texture(rowInfoPixmap);
        rowInfoSprite = new Sprite(rowInfoTexture);
        rowInfoPixmap.dispose();

        // col info
        Pixmap colInfoPixmap = new Pixmap(PIXEL_SIZE, PIXEL_SIZE * 2, Pixmap.Format.RGBA8888);
        colInfoPixmap.setColor(Color.LIGHT_GRAY);
        colInfoPixmap.fill();
        colInfoTexture = new Texture(colInfoPixmap);
        colInfoSprite = new Sprite(colInfoTexture);
        colInfoPixmap.dispose();
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FFFFORWA.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 8;
        params.color = Color.BLACK;
        params.flip = true;
        numberFont = generator.generateFont(params);
        generator.dispose();
    }

    private void initGUI() {
        stage = new Stage();
        skin = new Skin(Gdx.files.internal("gameUI.json"));

        final TextButton solveButton = new TextButton("solve", skin, "default");
        solveButton.setSize(100f, 20f);

        stage.addActor(solveButton);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundPixelTexture.dispose();
        fullPixelTexture.dispose();
        rowInfoTexture.dispose();
        colInfoTexture.dispose();
    }

    private Vector2 getPixelPosition(int row, int col) {
        float centerX = camera.viewportWidth / 2;
        float centerY = camera.viewportHeight / 2;
        float x = (centerX - (gameWidth - PIXEL_SPACE_COMBINED * 2) / 2) + col * PIXEL_SPACE_COMBINED;
        float y = (centerY - (gameHeight - PIXEL_SPACE_COMBINED * 2) / 2) + row * PIXEL_SPACE_COMBINED;
        return new Vector2(x, y);
    }

    private void drawPixel(Vector2 pixelVector, Pixel pixelToDraw) {
        int row = (int) pixelVector.y;
        int col = (int) pixelVector.x;
        if (pixelToDraw != null) {
            level.set(row, col, pixelToDraw);
        } else {
            if (!Pixel.EMPTY.equals(level.get(row, col))) {
                level.set(row, col, Pixel.EMPTY);
                currentPixelType = Pixel.EMPTY;
            } else {
                level.set(row, col, Pixel.FULL);
                currentPixelType = selectedPixelType;
            }
        }
    }

    private Vector2 toPixel(int screenX, int screenY) {
        Vector3 viewportCoordinates = viewport.unproject(new Vector3(screenX, screenY, 0));
        Vector2 gameStartCoordinates = getPixelPosition(0, 0);
        Vector2 relativeToGame = new Vector2(viewportCoordinates.x - gameStartCoordinates.x, viewportCoordinates.y - gameStartCoordinates.y);
        if (relativeToGame.x < 0 || relativeToGame.y < 0 || relativeToGame.x > gameWidth || relativeToGame.y > gameHeight) {
            Gdx.app.debug("to Pixel", "out of game");
            return null;
        }
        int x = MathUtils.floor(relativeToGame.x) / (PIXEL_SIZE + PIXEL_SPACE);
        int y = MathUtils.floor(relativeToGame.y) / (PIXEL_SIZE + PIXEL_SPACE);
        boolean outOfPixelX = relativeToGame.x > ((x + 1) * (PIXEL_SIZE + PIXEL_SPACE) - PIXEL_SPACE);
        boolean outOfPixelY = relativeToGame.y > ((y + 1) * (PIXEL_SIZE + PIXEL_SPACE) - PIXEL_SPACE);
        if (outOfPixelX || outOfPixelY) {
            Gdx.app.debug("to Pixel", "out of pixel");
            return null;
        }
        return new Vector2(x, y);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // draw row info
        for (int row = 0; row < level.getRows(); row++) {
            Vector2 pixelPosition = getPixelPosition(row, -2);
            rowInfoSprite.setPosition(pixelPosition.x, pixelPosition.y);
            rowInfoSprite.setAlpha(level.isRowComplete(row) ? 0.7f : 1f);
            rowInfoSprite.draw(batch);

            List<Integer> rowLevelData = PixelLogicUtil.getNumbersOfRow(level.getLevelData(), row);
            Collections.reverse(rowLevelData);
            float x = (PIXEL_SIZE * 2) - PIXEL_FULL_PADDING + (pixelPosition.x) - 8;
            float y = pixelPosition.y + 12;
            // TODO: align each number right
            for (int i = 0; i < rowLevelData.size(); i++) {
                numberFont.draw(batch, String.valueOf(rowLevelData.get(i)), x - (i * 8), y);
            }
        }

        // draw column info
        for (int col = 0; col < level.getColumns(); col++) {
            Vector2 pixelPosition = getPixelPosition(-2, col);
            colInfoSprite.setPosition(pixelPosition.x, pixelPosition.y);
            colInfoSprite.setAlpha(level.isColumnComplete(col) ? 0.7f : 1f);
            colInfoSprite.draw(batch);

            List<Integer> colLevelData = PixelLogicUtil.getNumbersOfCol(level.getLevelData(), col);
            float x = pixelPosition.x + 15;
            float y = (pixelPosition.y) + (PIXEL_SIZE * 2) - 14;
            // TODO: center each number
            for (int i = 0; i < colLevelData.size(); i++) {
                numberFont.draw(batch, String.valueOf(colLevelData.get(i)), x, y - ((colLevelData.size() - 1 - i) * 14));
            }
        }

        // draw pixels
        for (int row = 0; row < level.getRows(); row++) {
            for (int col = 0; col < level.getColumns(); col++) {
                Vector2 pixelPosition = getPixelPosition(row, col);
                backgroundPixelSprite.setPosition(pixelPosition.x, pixelPosition.y);
                backgroundPixelSprite.draw(batch);
                if (Pixel.FULL.equals(level.get(row, col))) {
                    fullPixelSprite.setPosition(pixelPosition.x + PIXEL_FULL_PADDING, pixelPosition.y + PIXEL_FULL_PADDING);
                    fullPixelSprite.draw(batch);
                }
            }
        }
        batch.end();

        // gui
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void show() {

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
    public boolean keyDown(int keycode) {
        Gdx.app.debug("keyDown", "code " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 pixel = toPixel(screenX, screenY);
        if (pixel == null) {
            return false;
        }
        Gdx.app.debug("hit pixel", "pixel " + pixel);
        drawPixel(pixel, null);
        checkGame();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 pixel = toPixel(screenX, screenY);
        if (pixel == null) {
            return false;
        }
        drawPixel(pixel, currentPixelType);
        checkGame();
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
