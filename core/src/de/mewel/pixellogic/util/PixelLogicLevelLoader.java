package de.mewel.pixellogic.util;

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
        if(collection != null) {
            for (int i = 0; i < collection.getLevelList().size(); i++) {
                PixelLogicLevel level = load(collection, i);
                PixelLogicUtil.validateLevel(level);
                levelList.add(level);
            }
        }
        return levelList;
    }

    public static PixelLogicLevel load(PixelLogicLevelCollection collection, String name) {
        Integer levelIndex = collection.getLevelIndexByName(name);
        return load(collection, levelIndex);
    }

    public static PixelLogicLevel load(PixelLogicLevelCollection collection, int levelIndex) {
        PixelLogicLevelData levelData = collection.getLevelList().get(levelIndex);
        return getPixelLogicImageLevel(collection, levelData);
    }

    private static PixelLogicLevel getPixelLogicImageLevel(PixelLogicLevelCollection collection, PixelLogicLevelData levelData) {
        try {
            Integer[][] pixmap = getImageData(collection, levelData, collection.getPixmap());
            Integer[][] levelmap = getImageData(collection, levelData, collection.getLevelmap());
            if (levelmap == null) {
                levelmap = pixmap;
            }
            Boolean[][] level = PixelLogicUtil.toLevelData(levelmap);
            return new PixelLogicLevel(levelData.getName(), level, pixmap);
        } catch (Exception exc) {
            throw new RuntimeException("Unable to load level '" + levelData.getName() + "'", exc);
        }
    }

    private static Integer[][] getImageData(PixelLogicLevelCollection collection, PixelLogicLevelData levelData, Pixmap pixmap) throws IOException {
        if (pixmap == null) {
            return null;
        }
        int srcX, srcY;
        if (levelData.getX() == null) {
            srcX = collection.indexOf(levelData) * collection.getPixmapWidth();
            srcY = 0;
        } else {
            srcX = levelData.getX() * collection.getPixmapWidth();
            srcY = levelData.getY() * collection.getPixmapHeight();
        }
        return parseImage(pixmap, srcX, srcY, collection.getPixmapWidth(), collection.getPixmapHeight());
    }

    private static Integer[][] parseImage(Pixmap pixmap, int srcX, int srcY, int width, int height) throws IOException {
        Integer imageData[][] = new Integer[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = pixmap.getPixel(x + srcX, y + srcY);
                imageData[y][x] = pixel;
            }
        }
        return imageData;
    }

}
