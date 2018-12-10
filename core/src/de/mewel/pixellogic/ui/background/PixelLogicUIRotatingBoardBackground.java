package de.mewel.pixellogic.ui.background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIActor;
import de.mewel.pixellogic.PixelLogicConstants;

public class PixelLogicUIRotatingBoardBackground extends PixelLogicUIActor {

    private int pixel = 25;

    private float rotate = 0;

    private float fadeTicker = 0;

    private Pixel board[][];

    private Random random;

    public PixelLogicUIRotatingBoardBackground(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        random = new Random();
        board = new Pixel[pixel][pixel];
        for (int row = 0; row < pixel; row++) {
            for (int col = 0; col < pixel; col++) {
                Pixel pixel = new Pixel(row, col);
                pixel.alpha = random.nextBoolean() ? 1f : 0f;
                pixel.color = random.nextFloat() > .4f ? PixelLogicConstants.PIXEL_FILLED_COLOR :
                        PixelLogicConstants.PIXEL_BLOCKED_COLOR;
                board[row][col] = pixel;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.end();
        Gdx.gl.glEnable(GL30.GL_BLEND);
        ShapeRenderer renderer = this.getAssets().getShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.setTransformMatrix(batch.getTransformMatrix());
        renderer.translate((int) (getWidth() / 2), (int) (getHeight() / 2), 0);
        renderer.rotate(0, 0, 1, rotate);

        // render board
        int width = (int) getWidth() / pixel;
        int height = (int) getHeight() / pixel;
        int size = (int) (Math.max(width, height) * 1.5f);
        int space = size / 12;
        size = size - space;
        int combined = size + space;
        int start = (-combined * pixel) / 2;

        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                Pixel pixel = board[row][col];
                Color c1 = new Color(pixel.color);
                Color c2 = new Color(PixelLogicConstants.PIXEL_EMPTY_COLOR);
                float r = pixel.alpha * c1.r + (1 - pixel.alpha) * c2.r;
                float g = pixel.alpha * c1.g + (1 - pixel.alpha) * c2.g;
                float b = pixel.alpha * c1.b + (1 - pixel.alpha) * c2.b;
                Color c = new Color(r, g, b, parentAlpha);
                renderer.setColor(c);
                renderer.begin(ShapeRenderer.ShapeType.Filled);
                renderer.box(start + row * combined, start + col * combined,
                        0, size, size, 0f);
                renderer.end();
            }
        }

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
                                PixelLogicConstants.PIXEL_FILLED_COLOR :
                                PixelLogicConstants.PIXEL_BLOCKED_COLOR;
                    }
                }
            }
        }
    }

    private static class Pixel {

        public int row, col;

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
