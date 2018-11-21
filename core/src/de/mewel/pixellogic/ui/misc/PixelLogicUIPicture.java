package de.mewel.pixellogic.ui.misc;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicUIActor;


public class PixelLogicUIPicture extends PixelLogicUIActor {

    private PixelLogicLevelCollection collection;

    private Sprite pixmap;

    private Sprite[][] unsolved;

    private int solvedIndex = 10;

    public PixelLogicUIPicture(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicLevelCollection collection) {
        super(assets, eventManager);
        this.collection = collection;
        buildSprites(collection);
    }

    private void buildSprites(PixelLogicLevelCollection collection) {
        Pixmap pixmap = collection.getPixmap();
        this.pixmap = new Sprite(new Texture(pixmap));

        int pixmapWidth = collection.getPixmapWidth();
        int pixmapHeight = collection.getPixmapHeight();
        int rows = pixmap.getHeight() / pixmapHeight;
        int cols = pixmap.getWidth() / pixmapWidth;
        this.unsolved = new Sprite[rows][cols];
        Texture unsolvedTexture = new Texture(collection.getUnsolved());
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                this.unsolved[row][col] = new Sprite(unsolvedTexture,
                        col * pixmapWidth,
                        row * pixmapHeight,
                        pixmapWidth,
                        pixmapHeight);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);
        float alpha = parentAlpha * color.a;
        batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
        if (pixmap != null) {
            batch.draw(pixmap, getX(), getY(), getWidth(), getHeight());
        }
        float cols = (pixmap.getWidth() / collection.getPixmapWidth());
        float rows = (pixmap.getHeight() / collection.getPixmapHeight());
        float unsolvedWidth = (getWidth() / cols);
        float unsolvedHeight = (getHeight() / rows);

        int solvedCounter = -1;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (solvedCounter++ >= solvedIndex) {
                    int x = (int) (getX() + col * unsolvedWidth);
                    int y = (int) ((getY() + getHeight() - unsolvedHeight) - row * unsolvedHeight);
                    batch.draw(this.unsolved[row][col], x, y, unsolvedWidth, unsolvedHeight);
                }
            }
        }
        batch.setColor(color);
    }

}
