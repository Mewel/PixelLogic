package de.mewel.pixellogic.ui.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIActor;

public class PixelLogicUIRotatingBoardBackground extends PixelLogicUIActor {

    private final int pixel = 25;

    private float rotate = 0;

    private float fadeTicker = 0;

    private final Pixel[][] board;

    private final Random random;

    public PixelLogicUIRotatingBoardBackground(PixelLogicGlobal global) {
        super(global);
        random = new Random();
        board = new Pixel[pixel][pixel];
        for (int row = 0; row < pixel; row++) {
            for (int col = 0; col < pixel; col++) {
                Pixel pixel = new Pixel(row, col);
                pixel.alpha = random.nextBoolean() ? 1f : 0f;
                pixel.color = random.nextFloat() > .4f ?
                        getGlobal().getStyle().getPixelFilledColor() :
                        getGlobal().getStyle().getPixelBlockedColor();
                board[row][col] = pixel;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // background dimensions
        int boardWidth = (int) getWidth();
        int boardHeight = (int) getHeight();

        // init
        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        ShapeRenderer renderer = this.getAssets().getShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate(boardWidth / 2f, boardHeight / 2f, 0);
        renderer.rotate(0, 0, 1, rotate);

        // render board
        int pixelWidth = boardWidth / pixel;
        int pixelHeight = boardHeight / pixel;
        int size = (int) (Math.max(pixelWidth, pixelHeight) * 1.5f);
        int space = size / 12;
        size = size - space;
        int combined = size + space;
        int start = (-combined * pixel) / 2;
        int maxRenderHeight = (int) (boardHeight / 1.6f);

        // render
        Color emptyColor = getGlobal().getStyle().getPixelEmptyColor();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                int x = start + row * combined;
                int y = start + col * combined;
                if (x > boardWidth || x < -boardWidth || y > maxRenderHeight || y < -maxRenderHeight) {
                    continue;
                }
                Pixel pixel = board[row][col];
                float r = pixel.alpha * pixel.color.r + (1 - pixel.alpha) * emptyColor.r;
                float g = pixel.alpha * pixel.color.g + (1 - pixel.alpha) * emptyColor.g;
                float b = pixel.alpha * pixel.color.b + (1 - pixel.alpha) * emptyColor.b;
                renderer.setColor(r, g, b, parentAlpha);
                renderer.box(x, y, 0, size, size, 0f);
            }
        }
        renderer.end();
        Gdx.gl.glDisable(GL30.GL_BLEND);
        batch.begin();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // rotate
        this.rotate += delta * 2;
        if (this.rotate > 360) {
            this.rotate = 0;
        }

        // fade
        final int changes = 2;
        this.fadeTicker += delta;
        if (this.fadeTicker > .01f) {
            for (int i = 0; i < changes; i++) {
                int pixelIndex = random.nextInt(pixel * pixel);
                int row = pixelIndex / pixel;
                int col = pixelIndex % pixel;
                this.board[row][col].fade = this.board[row][col].alpha == 0f;
                this.fadeTicker = 0;
            }
        }

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Pixel pixel = this.board[row][col];
                if (pixel.fade != null) {
                    pixel.alpha += pixel.fade ? .8f * delta : -.8f * delta;
                    if (pixel.fade && pixel.alpha > 1f) {
                        pixel.alpha = 1f;
                        pixel.fade = null;
                    } else if (!pixel.fade && pixel.alpha < 0f) {
                        pixel.alpha = 0f;
                        pixel.fade = null;
                        pixel.color = random.nextFloat() > .4f ?
                                getGlobal().getStyle().getPixelFilledColor() :
                                getGlobal().getStyle().getPixelBlockedColor();
                    }
                }
            }
        }
    }

    private static class Pixel {

        public final int row;
        public final int col;

        public Color color;

        public float alpha;

        Boolean fade;

        public Pixel(int row, int col) {
            this.row = row;
            this.col = col;
            this.alpha = 1f;
            this.fade = null;
        }

    }

}
