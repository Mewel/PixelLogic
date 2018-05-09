package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIBoardSolvedAnimation;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelSwitcherChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicUserChangedBoardEvent;

public class PixelLogicUILevel extends PixelLogicUILevelGroup {

    // gui
    private PixelLogicUIRowGroup rowGroup;
    private PixelLogicUIColumnGroup columnGroup;
    private PixelLogicUIBoard board;

    // input
    private LevelListener levelListener;

    // game stuff
    private PixelLogicLevel level;
    private PixelLogicLevelStatus status;

    public PixelLogicUILevel(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.levelListener = new LevelListener(this);
        this.level = null;
        this.status = null;
    }

    public void load(PixelLogicLevel level) {
        this.level = level;

        // BOARD
        this.board = new PixelLogicUIBoard(getAssets(), getEventManager());
        addActor(this.board);
        this.board.addListener(this.levelListener);
        this.getEventManager().listen(this.levelListener);
        this.board.load(getLevel());

        // LINES
        this.rowGroup = new PixelLogicUIRowGroup(getAssets(), getEventManager());
        addActor(this.rowGroup);

        this.columnGroup = new PixelLogicUIColumnGroup(getAssets(), getEventManager());
        addActor(this.columnGroup);

        updateSpritePosition();
    }

    public void resetLevel() {
        if (this.level == null) {
            return;
        }
        this.level.reset();
        this.board.clear();
        this.getEventManager().fire(new PixelLogicUserChangedBoardEvent(this, level));
    }

    public void resize() {
        updateSpritePosition();
        Vector2 size = PixelLogicUIUtil.get(level).getLevelSize();
        this.setSize(size.x, size.y);
    }

    public boolean isSolved() {
        return this.level.isSolved();
    }

    @Override
    public void clear() {
        this.getEventManager().remove(this.levelListener);
        super.clear();
    }

    public PixelLogicLevel getLevel() {
        return this.level;
    }

    private void updateSpritePosition() {
        if (level == null) {
            return;
        }
        PixelLogicUILevelResolution resolution = PixelLogicUIUtil.get(level);
        int rowInfoWidth = resolution.getRowInfoWidth();
        int rowInfoHeight = resolution.getColumnInfoHeight();
        Vector2 boardSize = resolution.getBoardSize();

        int space = resolution.getGameSpaceSize() * 2;
        int boardOffsetX = rowInfoWidth + space;
        if (this.board != null) {
            this.board.setSize(boardSize.x, boardSize.y);
            if (!this.isSolved()) {
                this.board.setPosition(boardOffsetX, 0);
            } else {
                float x = this.getWidth() / 2f - this.board.getWidth() / 2f;
                float y = this.getHeight() / 2f - this.board.getHeight() / 2f;
                this.board.setPosition(x, y);
            }
        }
        if (this.rowGroup != null) {
            this.rowGroup.setBounds(0, 0, rowInfoWidth, boardSize.y);
        }
        if (this.columnGroup != null) {
            float boardOffsetY = boardSize.y + space;
            this.columnGroup.setBounds(boardOffsetX, boardOffsetY, boardSize.x, rowInfoHeight);
        }
    }

    private void startSolveAnimation() {
        // run solve animation
        new PixelLogicUIBoardSolvedAnimation(this).execute();
    }

    public PixelLogicUIBoard getBoard() {
        return board;
    }

    public PixelLogicUIRowGroup getRowGroup() {
        return rowGroup;
    }

    public PixelLogicUIColumnGroup getColumnGroup() {
        return columnGroup;
    }

    private static class LevelListener extends InputListener implements PixelLogicListener {

        private PixelLogicUILevel gui;
        private UserAction userAction;
        private boolean selectedPixelType;

        LevelListener(PixelLogicUILevel gui) {
            this.gui = gui;
            this.userAction = null;
            this.selectedPixelType = true;
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicLevelSwitcherChangedEvent) {
                selectedPixelType = ((PixelLogicLevelSwitcherChangedEvent) event).isFillPixel();
            }
            if (event instanceof PixelLogicLevelStatusChangeEvent) {
                PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
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
            this.userAction = new UserAction(gui, action, pixel, selected);
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
            PixelLogicUILevelResolution resolution = PixelLogicUIUtil.get(level);
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

        private PixelLogicUILevel levelUI;

        private Type type;

        private boolean selectedPixelType;

        private Vector2 startPixel;

        private Vector2 lastPixel;

        private Boolean horizontal;

        UserAction(PixelLogicUILevel levelUI, Type type, Vector2 startPixel, boolean selectedPixelType) {
            this.levelUI = levelUI;
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
            Boolean oldValue = level.get(row, col);
            if (pixelValue == oldValue) {
                return;
            }
            level.set(row, col, pixelValue);
            levelUI.getBoard().setPixel(row, col, pixelValue);
            levelUI.getEventManager().fire(new PixelLogicUserChangedBoardEvent(levelUI, level, row, col, pixelValue));
        }

    }

}
