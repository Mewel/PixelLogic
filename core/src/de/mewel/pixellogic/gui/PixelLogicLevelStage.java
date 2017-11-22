package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

public class PixelLogicLevelStage extends Stage {

    // level width
    private int gameWidth, gameHeight;

    // gui
    private PixelLogicGUIRowGroup rowGroup;
    private PixelLogicGUIColumnGroup columnGroup;
    private PixelLogicGUIBoard board;

    // game stuff
    private UserAction userAction;
    private boolean selectedPixelType;
    private PixelLogicLevel level;

    public PixelLogicLevelStage() {
        this.selectedPixelType = true;
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        initViewport();
        initSprites();
        this.userAction = null;
    }

    private void initViewport() {
        gameWidth = level.getColumns() * (PIXEL_SPACE_COMBINED) - PIXEL_SPACE;
        gameHeight = level.getRows() * (PIXEL_SPACE_COMBINED) - PIXEL_SPACE;
        int viewportWidth = gameWidth + ((PIXEL_SIZE) * 3);
        int viewportHeight = gameHeight + ((PIXEL_SIZE) * 3);
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        setViewport(new FitViewport(viewportWidth, viewportHeight, camera));
        getViewport().apply();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
    }

    private void initSprites() {
        // BOARD
        this.board = new PixelLogicGUIBoard(level);
        this.board.setPosition(PIXEL_SPACE_COMBINED * 2, PIXEL_SPACE_COMBINED * 2);
        getRoot().addActor(this.board);

        // LINES
        this.rowGroup = new PixelLogicGUIRowGroup(level);
        this.rowGroup.setPosition(0, PIXEL_SPACE_COMBINED * 2);
        getRoot().addActor(this.rowGroup);

        this.columnGroup = new PixelLogicGUIColumnGroup(level);
        this.columnGroup.setPosition(PIXEL_SPACE_COMBINED * 2, 0);
        getRoot().addActor(this.columnGroup);
    }

    private void checkSolved() {
        if (this.level.isSolved()) {
            Gdx.input.setInputProcessor(null);
        }
    }

    private void drawPixel(Vector2 pixel) {
        if (this.userAction == null) {
            return;
        }
        this.userAction.update(pixel, this.level);
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
        this.userAction = new UserAction(action, pixel, this.selectedPixelType);
        drawPixel(pixel);
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.userAction = null;
        checkSolved();
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Vector2 pixel = toPixel(screenX, screenY);
        if (pixel == null) {
            return false;
        }
        drawPixel(pixel);
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

    private Vector2 toPixel(int screenX, int screenY) {
        Vector3 viewportCoordinates = getViewport().unproject(new Vector3(screenX, screenY, 0));
        Vector2 boardCoordinates = new Vector2(this.board.getX(), this.board.getY());
        Vector2 relativeToGame = new Vector2(viewportCoordinates.x - boardCoordinates.x,
                viewportCoordinates.y - boardCoordinates.y);
        if (relativeToGame.x < 0 || relativeToGame.y < 0 ||
                relativeToGame.x > gameWidth || relativeToGame.y > gameHeight) {
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

    private static class UserAction {

        enum Type {
            EMPTY, FILL, BLOCK
        }

        private Type type;

        private boolean selectedPixelType;

        private Vector2 startPixel;

        private Vector2 lastPixel;

        private Boolean horizontal;

        UserAction(Type type, Vector2 startPixel, boolean selectedPixelType) {
            this.startPixel = startPixel;
            this.type = type;
            this.selectedPixelType = selectedPixelType;
            this.horizontal = null;
        }

        void update(Vector2 pixel, PixelLogicLevel level) {
            int startColumn = (int) startPixel.y;
            int startRow = (int) startPixel.x;
            if (isDrawable(level, startColumn, startRow)) {
                this.draw(startColumn, startRow, level, type);
            }
            if (horizontal == null && this.startPixel.equals(pixel)) {
                return;
            }
            if (horizontal == null) {
                horizontal = pixel.x != this.startPixel.x;
            }
            // draw pixel
            int startFrom = horizontal ? (int) Math.min(startRow, pixel.x) : (int) Math.min(startColumn, pixel.y);
            int startTo = horizontal ? (int) Math.max(startRow, pixel.x) : (int) Math.max(startColumn, pixel.y);
            for (int i = startFrom; i <= startTo; i++) {
                draw(level, i, type);
            }
            // empty old pixel
            if (this.lastPixel != null) {
                int lastFrom = horizontal ? (int) Math.min(lastPixel.x, pixel.x) : (int) Math.min(lastPixel.y, pixel.y);
                for (int i = lastFrom; i < startFrom; i++) {
                    draw(level, i, Type.EMPTY);
                }
                int lastTo = horizontal ? (int) Math.max(lastPixel.x, pixel.x) : (int) Math.max(lastPixel.y, pixel.y);
                for (int i = startTo + 1; i <= lastTo; i++) {
                    draw(level, i, Type.EMPTY);
                }
            }
            this.lastPixel = pixel;
        }

        private void draw(PixelLogicLevel level, int i, Type drawType) {
            int row = horizontal ? (int) startPixel.y : i;
            int col = horizontal ? i : (int) startPixel.x;
            if (isDrawable(level, row, col)) {
                draw(row, col, level, drawType);
            }
        }

        private boolean isDrawable(PixelLogicLevel level, int row, int col) {
            boolean blocked = level.isBlocked(row, col);
            boolean filled = level.isFilled(row, col);
            if (Type.FILL.equals(this.type) && blocked) {
                return false;
            }
            if (Type.BLOCK.equals(this.type) && filled) {
                return false;
            }
            if (Type.EMPTY.equals(this.type) &&
                    ((this.selectedPixelType && blocked) || (!this.selectedPixelType && filled))) {
                return false;
            }
            return true;
        }

        private void draw(int row, int col, PixelLogicLevel level, Type type) {
            Boolean pixelValue = Type.EMPTY.equals(type) ? null : Type.FILL.equals(type);
            level.set(row, col, pixelValue);
        }

    }

}
