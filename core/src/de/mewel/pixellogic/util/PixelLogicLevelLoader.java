package de.mewel.pixellogic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelData;

public abstract class PixelLogicLevelLoader {

    public static PixelLogicLevel load(PixelLogicLevelCollection collection, int levelIndex) {
        FileHandle pixmapHandle = Gdx.files.internal("level").child(collection.getId()).child("pixmap.png");
        PixelLogicLevelData levelData = collection.getLevelList().get(levelIndex);
        int srcX = levelData.getX() * collection.getPixmapWidth();
        int srcY = levelData.getY() * collection.getPixmapHeight();
        try {
            Boolean[][] level = buildLevel(pixmapHandle, srcX, srcY, collection.getPixmapWidth(), collection.getPixmapHeight());
            return new PixelLogicLevel(level);
        } catch (IOException ioExc) {
            throw new RuntimeException("Unable to load pixmap.png of " + collection.getId(), ioExc);
        }
    }

    public static Boolean[][] buildLevel(FileHandle imageHandle, int srcX, int srcY, int width, int height) throws IOException {
        Boolean level[][] = new Boolean[height][width];
        Pixmap pixmap = new Pixmap(imageHandle);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int value = pixmap.getPixel(x + srcX, y + srcY);
                int R = ((value & 0xff000000) >>> 24);
                int G = ((value & 0x00ff0000) >>> 16);
                int B = ((value & 0x0000ff00) >>> 8);
                int A = ((value & 0x000000ff));
                level[y][x] = A == 255;
            }
        }
        return level;
    }

}
