package de.mewel.pixellogic.ui.level;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_SOUND_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.DRAW_SOUND_VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelSwitcherChangedEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevel extends PixelLogicUILevelGroup {

    // gui
    private PixelLogicUILevelResolution resolution;
    private PixelLogicUIRowGroup rowGroup;
    private PixelLogicUIColumnGroup columnGroup;
    private PixelLogicUIBoard board;

    // input
    private final LevelListener levelListener;
    private Boolean[][] enabled;

    // game stuff
    private PixelLogicLevel level;
    private PixelLogicLevelStatus status;
    private List<Boolean[][]> undoList;

    public PixelLogicUILevel(PixelLogicGlobal global) {
        super(global);
        this.levelListener = new LevelListener(this);
        this.level = null;
        this.status = null;
        this.enabled = null;
        this.undoList = new ArrayList<>();
    }

    public void load(PixelLogicLevel level) {
        this.level = level;
        this.undoList = new ArrayList<>();
        this.undoList.add(PixelLogicUtil.cloneLevel(this.level.getPixels()));

        // BOARD
        this.board = new PixelLogicUIBoard(getGlobal());
        addActor(this.board);
        this.board.addListener(this.levelListener);
        this.getEventManager().listen(this.levelListener);
        this.board.load(getLevel());

        // LINES
        this.rowGroup = new PixelLogicUIRowGroup(getGlobal());
        addActor(this.rowGroup);

        this.columnGroup = new PixelLogicUIColumnGroup(getGlobal());
        addActor(this.columnGroup);

        // enable all
        this.enabled = new Boolean[level.getRows()][level.getColumns()];
        setEnabled(true);
    }

    public void resetLevel() {
        if (this.level == null) {
            return;
        }
        this.level.reset();
        this.rowGroup.reset();
        this.columnGroup.reset();
        this.board.clear();
        this.getEventManager().fire(new PixelLogicBoardChangedEvent(this, level));
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

    @Override
    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
        this.resolution = resolution;
        int rowInfoWidth = resolution.getRowInfoWidth();
        int rowInfoHeight = resolution.getColumnInfoHeight();
        Vector2 boardSize = resolution.getBoardSize();

        int space = resolution.getGameSpaceSize() * 2;
        int boardOffsetX = rowInfoWidth + space;
        if (this.board != null) {
            this.board.setSize(boardSize.x, boardSize.y);
            this.board.updateLevelResolution(resolution);
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
            this.rowGroup.updateLevelResolution(resolution);
        }
        if (this.columnGroup != null) {
            float boardOffsetY = boardSize.y + space;
            this.columnGroup.setBounds(boardOffsetX, boardOffsetY, boardSize.x, rowInfoHeight);
            this.columnGroup.updateLevelResolution(resolution);
        }
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

    public PixelLogicUILevel setEnabled(boolean enabled) {
        for (int row = 0; row < this.level.getRows(); row++) {
            for (int col = 0; col < this.level.getColumns(); col++) {
                this.enabled[row][col] = enabled;
            }
        }
        return this;
    }

    public PixelLogicUILevel setEnabled(int row, int column, boolean enabled) {
        Gdx.app.log("setEnabled", "row=" + row + " col=" + column + " enabled=" + enabled);
        this.enabled[row][column] = enabled;
        return this;
    }

    public PixelLogicUILevel setEnabledRow(int row, boolean enabled) {
        for (int col = 0; col < this.level.getColumns(); col++) {
            this.enabled[row][col] = enabled;
        }
        return this;
    }

    public PixelLogicUILevel setEnabledColumn(int column, boolean enabled) {
        for (int row = 0; row < this.level.getRows(); row++) {
            this.enabled[row][column] = enabled;
        }
        return this;
    }

    public boolean isEnabled(int row, int col) {
        return this.enabled[row][col];
    }

    /**
     * Sets a pixel on the board
     *
     * @param row   row of the board
     * @param col   column of the board
     * @param pixel the pixel type
     */
    public void setPixel(int row, int col, Boolean pixel) {
        level.set(row, col, pixel);
        getBoard().setPixel(row, col, pixel);
        getEventManager().fire(new PixelLogicBoardChangedEvent(this, level, row, col, pixel));
    }

    private static class LevelListener extends InputListener implements PixelLogicListener {

        private final PixelLogicUILevel levelUI;
        private UserAction userAction;
        private boolean selectedPixelType;

        LevelListener(PixelLogicUILevel levelUI) {
            this.levelUI = levelUI;
            this.userAction = null;
            this.selectedPixelType = true;
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicUserEvent) {
                PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
                if (userEvent.getType().equals(PixelLogicUserEvent.Type.LEVEL_UNDO_CLICKED)) {
                    List<Boolean[][]> list = levelUI.undoList;
                    if (list.size() > 1) {
                        Boolean[][] last = list.get(list.size() - 2);
                        setLevel(levelUI, last);
                        list.remove(list.size() - 1);
                    }
                }
            }
            if (event instanceof PixelLogicLevelSwitcherChangedEvent) {
                selectedPixelType = ((PixelLogicLevelSwitcherChangedEvent) event).isFillPixel();
            }
            if (event instanceof PixelLogicLevelStatusChangeEvent) {
                PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
                levelUI.status = changeEvent.getStatus();
            }
        }

        @Override
        public boolean touchDown(InputEvent event, float boardX, float boardY, int pointer, int button) {
            if (!PixelLogicLevelStatus.PLAYABLE.equals(levelUI.status)) {
                return false;
            }
            Vector2 pixel = toPixel(boardX, boardY);
            if (pixel == null || !levelUI.isEnabled((int) pixel.y, (int) pixel.x)) {
                return false;
            }
            Boolean currentPixel = levelUI.level.get((int) pixel.y, (int) pixel.x);
            boolean selected = button != 1 && this.selectedPixelType;
            UserAction.Type action = currentPixel == null ? (selected ? UserAction.Type.FILL : UserAction.Type.BLOCK) : UserAction.Type.EMPTY;
            this.userAction = new UserAction(levelUI, action, new Vector2(boardX, boardY), pixel, selected);
            update(new Vector2(boardX, boardY), pixel);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float boardX, float boardY, int pointer, int button) {
            this.userAction = null;
            levelUI.undoList.add(PixelLogicUtil.cloneLevel(levelUI.getLevel().getPixels()));
        }

        @Override
        public void touchDragged(InputEvent event, float boardX, float boardY, int pointer) {
            Vector2 pixel = toPixel(boardX, boardY);
            if (pixel == null || !levelUI.isEnabled((int) pixel.y, (int) pixel.x)) {
                return;
            }
            update(new Vector2(boardX, boardY), pixel);
        }

        private void update(Vector2 boardPosition, Vector2 pixel) {
            String before = levelUI.level.toPixelString();
            drawPixel(boardPosition, pixel);
            String after = levelUI.level.toPixelString();
            if (!before.equals(after)) {
                playSound(pixel);
            }
        }

        private Vector2 toPixel(float boardX, float boardY) {
            PixelLogicLevel level = levelUI.level;
            PixelLogicUILevelResolution resolution = levelUI.resolution;
            int x = MathUtils.floor(boardX) / resolution.getGamePixelSizeCombined();
            int y = MathUtils.floor(boardY) / resolution.getGamePixelSizeCombined();
            if (x < 0 || y < 0 || x >= level.getColumns() || y >= level.getRows()) {
                return null;
            }
            return new Vector2(x, y);
        }

        private void drawPixel(Vector2 boardPosition, Vector2 pixel) {
            if (pixel == null || this.userAction == null) {
                return;
            }
            this.userAction.update(boardPosition, pixel, levelUI.level);
        }

        private void playSound(Vector2 pixel) {
            if (pixel == null || this.userAction == null) {
                return;
            }
            boolean type = this.userAction.selectedPixelType;
            levelUI.getAudio().playSound(type ? DRAW_SOUND : BLOCK_SOUND, type ? DRAW_SOUND_VOLUME : BLOCK_SOUND_VOLUME);
        }

    }

    public static void setLevel(PixelLogicUILevel levelUI, Boolean[][] pixel) {
        for (int row = 0; row < levelUI.getLevel().getRows(); row++) {
            for (int col = 0; col < levelUI.getLevel().getColumns(); col++) {
                if (levelUI.getLevel().get(row, col) != pixel[row][col]) {
                    levelUI.setPixel(row, col, pixel[row][col]);
                }
            }
        }
    }

    private static class UserAction {

        enum Type {
            EMPTY, FILL, BLOCK
        }

        private final Boolean[][] levelBefore;

        private final PixelLogicUILevel levelUI;

        private final Type type;

        private final boolean selectedPixelType;

        private final Vector2 startPosition;

        private final Vector2 startPixel;

        UserAction(PixelLogicUILevel levelUI, Type type, Vector2 startPosition, Vector2 startPixel, boolean selectedPixelType) {
            this.levelUI = levelUI;
            this.levelBefore = PixelLogicUtil.cloneLevel(levelUI.getLevel().getPixels());
            this.startPosition = startPosition;
            this.startPixel = startPixel;
            this.type = type;
            this.selectedPixelType = selectedPixelType;
        }

        void update(Vector2 boardPosition, Vector2 pixel, PixelLogicLevel level) {
            int startColumn = (int) startPixel.y;
            int startRow = (int) startPixel.x;

            if (isDrawable(level, startColumn, startRow)) {
                this.draw(startColumn, startRow, level, type);
            }
            float angle = MathUtils.atan2(boardPosition.y - startPosition.y, boardPosition.x - startPosition.x) * 180 / MathUtils.PI;
            boolean horizontal = (angle > -45 && angle < 45) || ((angle > 135 && angle <= 180) || (angle < -135 && angle >= -180));

            // reset original level every time
            setLevel(levelUI, levelBefore);

            // draw pixel
            int startFrom = horizontal ? (int) Math.min(startRow, pixel.x) : (int) Math.min(startColumn, pixel.y);
            int startTo = horizontal ? (int) Math.max(startRow, pixel.x) : (int) Math.max(startColumn, pixel.y);
            for (int i = startFrom; i <= startTo; i++) {
                int row = horizontal ? (int) startPixel.y : i;
                int col = horizontal ? i : (int) startPixel.x;
                if (isDrawable(level, row, col)) {
                    draw(row, col, level, type);
                }
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
            return !Type.EMPTY.equals(this.type) ||
                    ((!this.selectedPixelType || !blocked) && (this.selectedPixelType || !filled));
        }

        private void draw(int row, int col, PixelLogicLevel level, Type type) {
            Boolean pixelValue = Type.EMPTY.equals(type) ? null : Type.FILL.equals(type);
            Boolean oldValue = level.get(row, col);
            if (pixelValue == oldValue) {
                return;
            }
            levelUI.setPixel(row, col, pixelValue);
        }

    }

}
