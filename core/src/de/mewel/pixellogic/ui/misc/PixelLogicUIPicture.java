package de.mewel.pixellogic.ui.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoard;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoardPixel;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIBaseLevelAnimation;


public class PixelLogicUIPicture extends PixelLogicUIGroup {

    private Background background;

    public PixelLogicUIPicture(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicLevelCollection collection) {
        super(assets, eventManager);
        this.background = new Background(collection);
        this.addActor(this.background);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.background.setSize(getWidth(), getHeight());
    }

    public void update(int levelIndex) {
        this.background.update(levelIndex);
    }

    public void update(int levelIndex, int oldLevelIndex, float blendTime) {
        this.background.update(levelIndex, oldLevelIndex, blendTime);
    }

    public PixelLogicLevelCollection getCollection() {
        return this.background.collection;
    }

    public void animate(final PixelLogicUIBoard board, final int levelIndex, final int oldLevelIndex, float delay) {
        int rows = background.getRows();
        int cols = background.getCols();
        int col = levelIndex % cols;
        int row = (rows - 1) - (levelIndex / cols);

        float width = (getWidth() / background.getCols());
        float height = (getHeight() / rows);

        Vector2 coords = new Vector2(board.getX(), board.getY());
        board.localToStageCoordinates(coords);

        final ResizeableBoard fakeBoard = new ResizeableBoard(board, getAssets(), getEventManager());
        fakeBoard.setPosition(coords.x - this.getX() - board.getX(),
                coords.y - this.getY() - board.getY());
        fakeBoard.setSize(board.getWidth(), board.getHeight());
        fakeBoard.getColor().a = 0f;

        PixelLogicUIPicture.this.addActor(fakeBoard);

        SequenceAction boardAnimation = new SequenceAction();
        boardAnimation.addAction(Actions.delay(delay));
        boardAnimation.addAction(Actions.fadeIn(0f));
        boardAnimation.addAction(new ParallelAction(
                Actions.sizeTo(width, height, .2f),
                Actions.moveTo(width * col, height * row, .2f)
        ));
        boardAnimation.addAction(Actions.delay(.5f));
        boardAnimation.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                fakeBoard.animate();
            }
        }));
        boardAnimation.addAction(Actions.run(new Runnable() {
            @Override
            public void run() {
                background.update(levelIndex, oldLevelIndex, .5f);
            }
        }));
        boardAnimation.addAction(Actions.delay(1.6f));
        boardAnimation.addAction(Actions.fadeOut(0));
        fakeBoard.addAction(boardAnimation);
        board.addAction(new SequenceAction(Actions.delay(delay), Actions.fadeOut(0f)));
    }

    private static class Background extends Actor {

        private PixelLogicLevelCollection collection;

        private Pixmap unsolved;

        private Sprite sprite;

        private Sprite newSprite;

        private float blendTime = 0f;

        private float currentBlendTime = 0f;

        public Background(PixelLogicLevelCollection collection) {
            this.collection = collection;
            this.unsolved = collection.getUnsolved();
        }

        public void update(int solvedIndex, int oldSolvedIndex, float blendTime) {
            Pixmap base = collection.getPixmap();
            Pixmap mixed = new Pixmap(base.getWidth(), base.getHeight(), base.getFormat());
            mixed.drawPixmap(base, 0, 0);

            int pixmapWidth = collection.getPixmapWidth();
            int pixmapHeight = collection.getPixmapHeight();
            int rows = mixed.getHeight() / pixmapHeight;
            int cols = mixed.getWidth() / pixmapWidth;

            int solvedCounter = -1;
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (solvedCounter++ >= solvedIndex)
                        mixed.drawPixmap(unsolved, col * pixmapWidth, row * pixmapHeight,
                                pixmapWidth, pixmapHeight, col * pixmapWidth, row * pixmapHeight,
                                pixmapWidth, pixmapHeight);
                }
            }
            if (this.sprite == null || solvedIndex == oldSolvedIndex || blendTime == 0f) {
                this.sprite = new Sprite(new Texture(mixed));
            } else {
                this.blendTime = blendTime;
                this.currentBlendTime = blendTime;
                this.newSprite = new Sprite(new Texture(mixed));
            }
        }

        public void update(int solvedIndex) {
            this.update(solvedIndex, solvedIndex, 0);
        }

        public float getPixmapHeight() {
            return this.sprite.getHeight();
        }

        public float getPixmapWidth() {
            return this.sprite.getWidth();
        }

        public int getRows() {
            return (int) (getPixmapHeight() / collection.getPixmapHeight());
        }

        public int getCols() {
            return (int) (getPixmapWidth() / collection.getPixmapWidth());
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            float alpha = parentAlpha * color.a;
            batch.setColor(new Color(color.r, color.g, color.b, alpha));
            if (sprite != null) {
                batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
            }
            if (this.newSprite != null) {
                alpha *= 1 - this.currentBlendTime / this.blendTime;
                batch.setColor(new Color(color.r, color.g, color.b, alpha));
                batch.draw(newSprite, getX(), getY(), getWidth(), getHeight());
            }
            batch.setColor(color);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (this.newSprite != null) {
                this.currentBlendTime -= delta;
                if (this.currentBlendTime <= 0) {
                    this.currentBlendTime = 0f;
                    this.blendTime = 0f;
                    this.sprite = newSprite;
                    this.newSprite = null;
                }
            }
        }

        public Pixmap getUnsolved() {
            return unsolved;
        }

    }

    private static class ResizeableBoard extends PixelLogicUIGroup {

        private PixelLogicUIBoard board;

        private PixelLogicUIBoardPixel[][] pixels;

        public ResizeableBoard(PixelLogicUIBoard board, PixelLogicAssets assets, PixelLogicEventManager eventManager) {
            super(assets, eventManager);
            this.board = board;
            PixelLogicLevel level = board.getLevel();
            this.pixels = new PixelLogicUIBoardPixel[level.getRows()][level.getColumns()];
            for (int row = 0; row < level.getRows(); row++) {
                for (int col = 0; col < level.getColumns(); col++) {
                    PixelLogicUIBoardPixel boardPixel = this.board.getPixels()[row][col];
                    this.pixels[row][col] = new PixelLogicUIBoardPixel(getAssets(), row, col);
                    this.pixels[row][col].setColor(boardPixel.getColor());
                    this.addActor(this.pixels[row][col]);
                }
            }
        }

        public float animate() {
            float executionTime = 0f;
            for (int row = 0; row < pixels.length; row++) {
                for (int col = 0; col < pixels[0].length; col++) {
                    executionTime = Math.max(animatePixel(board.getLevel(), pixels[row][col]), executionTime);
                }
            }
            return executionTime;
        }

        protected float animatePixel(PixelLogicLevel level, PixelLogicUIBoardPixel pixel) {
            int row = pixel.getRow();
            int col = pixel.getCol();

            Color pixelColor = level.getColor(row, col);
            Color transColor = new Color(pixelColor);
            transColor.a = 0f;
            SequenceAction sequenceAction = new SequenceAction();
            float mult = (float) (col + row) / (float) (level.getColumns() + level.getRows());
            if (!level.isFilled(pixel.getRow(), pixel.getCol())) {
                sequenceAction.addAction(Actions.color(transColor, 0f));
            }
            sequenceAction.addAction(Actions.delay(0.2f * mult));
            if (level.isFilled(pixel.getRow(), pixel.getCol())) {
                sequenceAction.addAction(Actions.color(Color.WHITE, 0.2f + (0.4f * mult)));
            }
            sequenceAction.addAction(Actions.delay(0.2f * mult));
            sequenceAction.addAction(Actions.color(pixelColor, 0.2f + (0.4f * mult)));
            pixel.addAction(sequenceAction);
            return PixelLogicUIBaseLevelAnimation.getDuration(sequenceAction);
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            PixelLogicLevel level = board.getLevel();
            float pixelWidth = this.getWidth() / level.getColumns();
            float pixelHeight = this.getHeight() / level.getRows();
            for (int row = 0; row < level.getRows(); row++) {
                for (int col = 0; col < level.getColumns(); col++) {
                    this.pixels[row][col].setSize(pixelWidth, pixelHeight);
                    this.pixels[row][col].setPosition(pixelWidth * col, pixelHeight * row);
                }
            }
        }
    }


}
