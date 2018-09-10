package de.mewel.pixellogic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelData;

public abstract class PixelLogicLevelLoader {

    public static List<PixelLogicLevel> load(PixelLogicLevelCollection collection) {
        if (collection == null) {
            return new ArrayList<PixelLogicLevel>();
        }
        List<PixelLogicLevel> levelList = loadOrdered(collection);
        // preserve order
        if (collection.getPreserveOrder()) {
            return levelList;
        }
        // return by complexity: easiest level first
        Map<PixelLogicLevel, Float> complexityMap = new HashMap<PixelLogicLevel, Float>();
        for (PixelLogicLevel level : levelList) {
            PixelLogicComplexityAnalyzerResult result = PixelLogicComplexityAnalyzer.analyze(level);
            complexityMap.put(level, result.getComplexity());
        }
        return PixelLogicUtil.sortByValue(complexityMap);
    }

    private static List<PixelLogicLevel> loadOrdered(PixelLogicLevelCollection collection) {
        List<PixelLogicLevel> levelList = new ArrayList<PixelLogicLevel>();
        for (int i = 0; i < collection.getLevelList().size(); i++) {
            PixelLogicLevel level = load(collection, i);
            /*boolean solvable = PixelLogicUtil.isSolvable(level.getLevelData());
            Gdx.app.log("level loader", level.getName() + " is " + (solvable ? "valid" : "invalid--------------------------------------"));
            PixelLogicComplexityAnalyzerResult result = PixelLogicComplexityAnalyzer.analyze(level);
            Gdx.app.log("level loader", level.getName() + " complexity " + result.getComplexity());*/
            levelList.add(level);
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
            return new PixelLogicLevel(levelData.getName(), level, pixmap, collection.getPreserveSize());
        } catch (Exception exc) {
            throw new RuntimeException("Unable to load level '" + levelData.getName() + "'", exc);
        }
    }

    private static Integer[][] getImageData(PixelLogicLevelCollection collection, PixelLogicLevelData levelData, Pixmap pixmap) throws IOException {
        if (pixmap == null) {
            return null;
        }
        int srcX, srcY;
        if (levelData.getX() == null || levelData.getY() == null) {
            int width = pixmap.getWidth();
            Integer index = collection.indexOf(levelData);
            srcX = (index * collection.getPixmapWidth()) % width;
            srcY = ((index * collection.getPixmapWidth()) / width) * collection.getPixmapHeight();
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
                imageData[height - 1 - y][x] = pixel;
            }
        }
        return imageData;
    }

}
