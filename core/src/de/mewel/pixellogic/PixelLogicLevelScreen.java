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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicLevelScreen implements Screen, InputProcessor {

    private static int PIXEL_SIZE = 32;
    private static int PIXEL_SPACE = 2;
    private static int PIXEL_SPACE_COMBINED = PIXEL_SIZE + PIXEL_SPACE;

    // drawing stuff
    private SpriteBatch batch;

    private Sprite backgroundPixelSprite;
    private Sprite filledPixelSprite;
    private Sprite blockedPixelSprite;
    private Sprite rowInfoSprite;
    private Sprite colInfoSprite;

    private Texture backgroundPixelTexture;
    private Texture filledPixelTexture;
    private Texture blockedPixelTexture;
    private Texture rowInfoTexture;
    private Texture colInfoTexture;

    private BitmapFont numberFont;

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");
    private static Color PIXEL_EMPTY_COLOR = Color.valueOf("#CEE3F6");
    private static Color PIXEL_FILLED_COLOR = Color.valueOf("#1C1C1C");
    private static Color PIXEL_BLOCKED_COLOR = Color.valueOf("#FA5882");
    private static Color LINE_COLOR = Color.valueOf("#F5DA81");
    private static Color LINE_COMPLETE_COLOR = Color.valueOf("#FF8000");
    private static Color TEXT_COLOR = Color.valueOf("#424242");

    // camera and viewport
    private OrthographicCamera camera;
    private FitViewport viewport;
    private int gameWidth, gameHeight, viewportWidth, viewportHeight;

    // gui
    private Stage stage;
    private Skin skin;

    // game stuff
    private UserAction userAction;
    private Boolean selectedPixelType = true;
    private PixelLogicLevel level;

    public PixelLogicLevelScreen() {
        Gdx.input.setInputProcessor(this);
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        initViewport();
        initSprites();
        initFonts();
        initGUI();
        this.userAction = null;
    }

    private void isSolved() {
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

        // PIXELS
        Pixmap pixelPixmap = new Pixmap(PIXEL_SIZE, PIXEL_SIZE, Pixmap.Format.RGBA8888);
        // empty pixel
        pixelPixmap.setColor(PIXEL_EMPTY_COLOR);
        pixelPixmap.fill();
        backgroundPixelTexture = new Texture(pixelPixmap);
        backgroundPixelSprite = new Sprite(backgroundPixelTexture);

        // filled pixel
        pixelPixmap.setColor(PIXEL_FILLED_COLOR);
        pixelPixmap.fill();
        filledPixelTexture = new Texture(pixelPixmap);
        filledPixelSprite = new Sprite(filledPixelTexture);

        // blocked pixel
        pixelPixmap.setColor(PIXEL_BLOCKED_COLOR);
        pixelPixmap.fill();
        blockedPixelTexture = new Texture(pixelPixmap);
        blockedPixelSprite = new Sprite(blockedPixelTexture);

        pixelPixmap.dispose();

        // LINES
        Pixmap linePixmap = new Pixmap(PIXEL_SIZE * 2, PIXEL_SIZE, Pixmap.Format.RGBA8888);

        // row info
        linePixmap.setColor(LINE_COLOR);
        linePixmap.fill();
        rowInfoTexture = new Texture(linePixmap);
        rowInfoSprite = new Sprite(rowInfoTexture);

        // col info
        linePixmap.setColor(LINE_COLOR);
        linePixmap.fill();
        colInfoTexture = new Texture(linePixmap);
        colInfoSprite = new Sprite(colInfoTexture);

        linePixmap.dispose();
    }

    private void initFonts() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FFFFORWA.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 8;
        params.color = TEXT_COLOR;
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
        filledPixelTexture.dispose();
        blockedPixelTexture.dispose();
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

    private void drawPixel(Vector2 pixelVector) {
        int row = (int) pixelVector.y;
        int col = (int) pixelVector.x;
        level.set(row, col, this.userAction.update(pixelVector));
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
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        // draw row info
        for (int row = 0; row < level.getRows(); row++) {
            Vector2 pixelPosition = getPixelPosition(row, -2);
            rowInfoSprite.setPosition(pixelPosition.x, pixelPosition.y);
            rowInfoSprite.setColor(level.isRowComplete(row) ? LINE_COMPLETE_COLOR : LINE_COLOR);
            rowInfoSprite.draw(batch);

            List<Integer> rowLevelData = PixelLogicUtil.getNumbersOfRow(level.getLevelData(), row);
            Collections.reverse(rowLevelData);
            float x = (PIXEL_SIZE * 2) - (pixelPosition.x) - 8;
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
            colInfoSprite.setColor(level.isColumnComplete(col) ? LINE_COMPLETE_COLOR : LINE_COLOR);
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
                Boolean pixel = level.get(row, col);
                if (pixel != null && pixel) {
                    filledPixelSprite.setPosition(pixelPosition.x, pixelPosition.y);
                    filledPixelSprite.draw(batch);
                } else if (pixel != null && !pixel) {
                    blockedPixelSprite.setPosition(pixelPosition.x, pixelPosition.y);
                    blockedPixelSprite.draw(batch);
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
        Boolean currentPixel = level.get((int) pixel.y, (int) pixel.x);
        this.selectedPixelType = button == 0;
        UserAction.Type action = currentPixel == null ? (this.selectedPixelType ? UserAction.Type.FILL : UserAction.Type.BLOCK) : UserAction.Type.EMPTY;
        this.userAction = new UserAction(action, pixel);
        drawPixel(pixel);
        isSolved();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.userAction = null;
        Gdx.app.log("touchUp", "touch up");
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 pixel = toPixel(screenX, screenY);
        if (pixel == null) {
            return false;
        }
        drawPixel(pixel);
        isSolved();
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

    private static class UserAction {

        enum Type {
            EMPTY, FILL, BLOCK
        }

        private Type type;

        private Vector2 activePixel;

        private List<Vector2> pixels;

        private Boolean lastAction;

        public UserAction(Type type, Vector2 startPixel) {
            this.activePixel = startPixel;
            this.pixels = new ArrayList<Vector2>();
            this.pixels.add(this.activePixel);
            this.type = type;
            this.lastAction = true;
        }

        public Boolean update(Vector2 pixel) {
            if (Type.EMPTY.equals(this.type)) {
                return null;
            }
            if (pixel.equals(this.activePixel)) {
                return this.lastAction;
            }
            // pixel changed
            this.activePixel = pixel;
            if (pixels.contains(pixel)) {
                pixels.remove(pixel);
                return this.lastAction = null;
            } else {
                pixels.add(pixel);
                return this.lastAction = Type.FILL.equals(this.type);
            }
        }

    }

}
