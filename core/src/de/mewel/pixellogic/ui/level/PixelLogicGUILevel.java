package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicSwitcherChangedEvent;
import de.mewel.pixellogic.ui.PixelLogicLevelStatus;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUILevel extends Group {

    // gui
    private PixelLogicGUIRowGroup rowGroup;
    private PixelLogicGUIColumnGroup columnGroup;
    private PixelLogicGUIBoard board;

    // input
    private LevelListener levelListener;

    // game stuff
    private PixelLogicLevel level;
    private PixelLogicLevelStatus status;

    public PixelLogicGUILevel() {
        this.levelListener = new LevelListener(this);
        this.level = null;
        this.status = null;
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        initSprites();
    }

    public void resetLevel() {
        if (this.level == null) {
            return;
        }
        this.level.reset();
    }

    public void resize(int width, int height) {
        PixelLogicGUILevelResolutionManager.instance().setWidth(width);
        PixelLogicGUILevelResolutionManager.instance().setHeight(height);
        updateSpritePosition();
        Vector2 size = PixelLogicGUILevelResolutionManager.instance().getLevelSize(level);
        this.setSize(size.x, size.y);
    }

    public boolean isSolved() {
        return this.level.isSolved();
    }

    @Override
    public void clear() {
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        PixelLogicEventManager.instance().remove(this.levelListener);
        super.clear();
    }

    public PixelLogicLevel getLevel() {
        return this.level;
    }

    private void initSprites() {
        // BOARD
        this.board = new PixelLogicGUIBoard();
        addActor(this.board);
        this.board.addListener(this.levelListener);
        PixelLogicEventManager.instance().listen(this.levelListener);

        // LINES
        this.rowGroup = new PixelLogicGUIRowGroup();
        addActor(this.rowGroup);

        this.columnGroup = new PixelLogicGUIColumnGroup();
        addActor(this.columnGroup);

        updateSpritePosition();
    }

    private void updateSpritePosition() {
        if (level == null) {
            return;
        }
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

    private void startSolveAnimation() {
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        // center board
        SequenceAction sequenceAction = new SequenceAction();
        sequenceAction.addAction(Actions.delay(0.2f));
        int moveBy = resolution.getGamePixelSizeCombined();
        Gdx.app.log("level start solve", "" + moveBy);
        sequenceAction.addAction(Actions.moveBy(-moveBy, -moveBy, 0.2f));
        this.board.addAction(sequenceAction);
    }

    private static class LevelListener extends InputListener implements PixelLogicListener {

        private PixelLogicGUILevel gui;
        private UserAction userAction;
        private boolean selectedPixelType;

        LevelListener(PixelLogicGUILevel gui) {
            this.gui = gui;
            this.userAction = null;
            this.selectedPixelType = true;
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicSwitcherChangedEvent) {
                selectedPixelType = ((PixelLogicSwitcherChangedEvent) event).isFillPixel();
            }
            if (event instanceof PixelLogicLevelChangeEvent) {
                PixelLogicLevelChangeEvent changeEvent = (PixelLogicLevelChangeEvent) event;
                gui.status = changeEvent.getStatus();
                if (PixelLogicLevelStatus.solved.equals(gui.status)) {
                    gui.startSolveAnimation();
                }
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float boardX, float boardY, int pointer, int button) {
            if (!PixelLogicLevelStatus.playable.equals(gui.status)) {
                return true;
            }
            Vector2 pixel = toPixel(boardX, boardY);
            if (pixel == null) {
                return false;
            }
            Boolean currentPixel = gui.level.get((int) pixel.y, (int) pixel.x);
            boolean selected = button != 1 && this.selectedPixelType;
            UserAction.Type action = currentPixel == null ? (selected ? UserAction.Type.FILL : UserAction.Type.BLOCK) : UserAction.Type.EMPTY;
            this.userAction = new UserAction(action, pixel, selected);
            drawPixel(pixel);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float boardX, float boardY, int pointer, int button) {
            this.userAction = null;
        }

        @Override
        public void touchDragged(InputEvent event, float boardX, float boardY, int pointer) {
            drawPixel(toPixel(boardX, boardY));
        }

        private Vector2 toPixel(float boardX, float boardY) {
            PixelLogicLevel level = gui.level;
            PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
            int x = MathUtils.floor(boardX) / resolution.getGamePixelSizeCombined();
            int y = MathUtils.floor(boardY) / resolution.getGamePixelSizeCombined();
            if (x < 0 || y < 0 || x >= level.getColumns() || y >= level.getRows()) {
                return null;
            }
            return new Vector2(x, y);
        }

        private void drawPixel(Vector2 pixel) {
            if (pixel == null || this.userAction == null) {
                return;
            }
            this.userAction.update(pixel, gui.level);
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
