package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.model.PixelLogicLevel;

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
        this.checkSolved();
    }

    private void initViewport() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        setViewport(new ExtendViewport(screenWidth, screenHeight, camera));
    }

    private void initSprites() {
        // BOARD
        this.board = new PixelLogicGUIBoard(level);
        getRoot().addActor(this.board);

        // LINES
        this.rowGroup = new PixelLogicGUIRowGroup(level);
        getRoot().addActor(this.rowGroup);

        this.columnGroup = new PixelLogicGUIColumnGroup(level);
        getRoot().addActor(this.columnGroup);

        updateSpritePosition();
    }

    private void updateSpritePosition() {
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), level);
        int offset = resolution.getGamePixelSizeCombined() * 2;
        if (this.board != null) {
            this.board.setPosition(offset, offset);
        }
        if (this.rowGroup != null) {
            this.rowGroup.setPosition(0, offset);
        }
        if (this.columnGroup != null) {
            this.columnGroup.setPosition(offset, 0);
        }
    }

    private void checkSolved() {
        if (this.level.isSolved()) {
            PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), level);
            // disable input
            Gdx.input.setInputProcessor(null);
            // center board
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.addAction(Actions.delay(0.2f));
            sequenceAction.addAction(Actions.moveBy(-(resolution.getGamePixelSizeCombined()), -(resolution.getGamePixelSizeCombined()), 0.2f));
            this.board.addAction(sequenceAction);
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
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), level);
        int boardWidth = level.getColumns() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        int boardHeight = level.getRows() * resolution.getGamePixelSizeCombined() - resolution.getGameSpaceSize();
        if (relativeToGame.x < 0 || relativeToGame.y < 0 ||
                relativeToGame.x > boardWidth || relativeToGame.y > boardHeight) {
            Gdx.app.log("to Pixel", "out of game");
            return null;
        }
        int x = MathUtils.floor(relativeToGame.x) / resolution.getGamePixelSizeCombined();
        int y = MathUtils.floor(relativeToGame.y) / resolution.getGamePixelSizeCombined();
        /*boolean outOfPixelX = relativeToGame.x > ((x + 1) * (resolution.getGamePixelSizeCombined()) - (resolution.getGamePixelSize()));
        boolean outOfPixelY = relativeToGame.y > ((y + 1) * (resolution.getGamePixelSizeCombined()) - (resolution.getGamePixelSize()));
        if (outOfPixelX || outOfPixelY) {
            Gdx.app.log("to Pixel", "out of pixel");
            return null;
        }*/
        return new Vector2(x, y);
    }

    public void resize(int width, int height) {
        getViewport().update(width, height);
        ((OrthographicCamera) getCamera()).setToOrtho(true, width, height);
        updateSpritePosition();
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
