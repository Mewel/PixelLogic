package de.mewel.pixellogic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelData;

public abstract class PixelLogicLevelLoader {

    public static List<PixelLogicLevel> load(PixelLogicLevelCollection collection) {
        List<PixelLogicLevel> levelList = new ArrayList<PixelLogicLevel>();
        for(int i = 0; i < collection.getLevelList().size(); i++) {
            levelList.add(load(collection, i));
        }
        return levelList;
    }

    public static PixelLogicLevel load(PixelLogicLevelCollection collection, String name) {
        Integer levelIndex = collection.getLevelIndexByName(name);
        return load(collection, levelIndex);
    }

    public static PixelLogicLevel load(PixelLogicLevelCollection collection, int levelIndex) {
        FileHandle pixmapHandle = Gdx.files.internal("level").child(collection.getId()).child("pixmap.png");
        PixelLogicLevelData levelData = collection.getLevelList().get(levelIndex);
        int srcX = levelData.getX() * collection.getPixmapWidth();
        int srcY = levelData.getY() * collection.getPixmapHeight();
        try {
            Integer[][] imageData = buildImageData(pixmapHandle, srcX, srcY, collection.getPixmapWidth(), collection.getPixmapHeight());
            return new PixelLogicLevel(levelData.getName(), imageData);
        } catch (IOException ioExc) {
            throw new RuntimeException("Unable to load pixmap.png of " + collection.getId(), ioExc);
        }
    }

    private static Integer[][] buildImageData(FileHandle imageHandle, int srcX, int srcY, int width, int height) throws IOException {
        Integer imageData[][] = new Integer[height][width];
        Pixmap pixmap = new Pixmap(imageHandle);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixmap.getPixel(x + srcX, y + srcY);
                imageData[y][x] = pixel;
            }
        }
        return imageData;
    }

}
