package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUILevel extends Group {

    // gui
    private PixelLogicGUIRowGroup rowGroup;
    private PixelLogicGUIColumnGroup columnGroup;
    private PixelLogicGUIBoard board;

    // input
    private LevelInputListener boardInputListener;

    // game stuff
    private PixelLogicLevel level;

    public PixelLogicGUILevel() {
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        this.boardInputListener = new LevelInputListener(this);
        initSprites();
        this.checkSolved();
    }

    private void initSprites() {
        // BOARD
        this.board = new PixelLogicGUIBoard(level);
        addActor(this.board);
        this.board.addListener(this.boardInputListener);

        // LINES
        this.rowGroup = new PixelLogicGUIRowGroup(level);
        addActor(this.rowGroup);

        this.columnGroup = new PixelLogicGUIColumnGroup(level);
        addActor(this.columnGroup);

        updateSpritePosition();
    }

    private void updateSpritePosition() {
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
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
            PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
            // disable input
            Gdx.input.setInputProcessor(null);
            // center board
            SequenceAction sequenceAction = new SequenceAction();
            sequenceAction.addAction(Actions.delay(0.2f));
            sequenceAction.addAction(Actions.moveBy(-(resolution.getGamePixelSizeCombined()), -(resolution.getGamePixelSizeCombined()), 0.2f));
            this.board.addAction(sequenceAction);
        }
    }

    public void resize(int width, int height) {
        PixelLogicGUILevelResolutionManager.instance().setWidth(width);
        PixelLogicGUILevelResolutionManager.instance().setHeight(height);
        updateSpritePosition();
        Vector2 size = PixelLogicGUILevelResolutionManager.instance().getLevelSize(level);
        this.setSize(size.x, size.y);
    }

    private static class LevelInputListener extends InputListener {

        private PixelLogicGUILevel gui;
        private PixelLogicLevel level;
        private UserAction userAction;
        private boolean selectedPixelType;

        public LevelInputListener(PixelLogicGUILevel gui) {
            this.gui = gui;
            this.level = gui.level;
            this.userAction = null;
            this.selectedPixelType = true;
        }

        @Override
        public boolean touchDown(InputEvent event, float boardX, float boardY, int pointer, int button) {
            Vector2 pixel = toPixel(boardX, boardY);
            Boolean currentPixel = gui.level.get((int) pixel.y, (int) pixel.x);
            this.selectedPixelType = button == 0;
            UserAction.Type action = currentPixel == null ? (this.selectedPixelType ? UserAction.Type.FILL : UserAction.Type.BLOCK) : UserAction.Type.EMPTY;
            this.userAction = new UserAction(action, pixel, this.selectedPixelType);
            drawPixel(pixel);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float boardX, float boardY, int pointer, int button) {
            this.userAction = null;
            gui.checkSolved();
        }

        @Override
        public void touchDragged(InputEvent event, float boardX, float boardY, int pointer) {
            drawPixel(toPixel(boardX, boardY));
        }

        private Vector2 toPixel(float boardX, float boardY) {
            PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
            int x = MathUtils.floor(boardX) / resolution.getGamePixelSizeCombined();
            int y = MathUtils.floor(boardY) / resolution.getGamePixelSizeCombined();
            return new Vector2(x, y);
        }

        private void drawPixel(Vector2 pixel) {
            if (this.userAction == null) {
                return;
            }
            this.userAction.update(pixel, this.level);
        }

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
