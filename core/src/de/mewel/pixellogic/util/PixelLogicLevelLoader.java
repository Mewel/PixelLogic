package de.mewel.pixellogic.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelData;

public abstract class PixelLogicLevelLoader {

    public static List<PixelLogicLevel> load(PixelLogicLevelCollection collection) {
        if (collection == null) {
            return new ArrayList<PixelLogicLevel>();
        }
        if (collection.getLevelList() == null) {
            return loadByAutoDetect(collection);
        }
        return loadByList(collection);
    }

    private static List<PixelLogicLevel> loadByAutoDetect(PixelLogicLevelCollection collection) {
        int levelWidth = collection.getPixmapWidth();
        int levelHeight = collection.getPixmapHeight();
        int pixmapWidth = collection.getLevelmap().getWidth();
        int pixmapHeight = collection.getLevelmap().getHeight();
        int cols = pixmapWidth / levelWidth;
        int rows = pixmapHeight / levelHeight;
        List<Integer> orderList = collection.getOrder();

        List<PixelLogicLevel> levels = new ArrayList<PixelLogicLevel>();
        int levelCounter = 0;
        int orderCounter = 0;
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                if (orderList != null && !orderList.isEmpty() && !orderList.contains(orderCounter++)) {
                    continue;
                }
                PixelLogicLevelData levelData = new PixelLogicLevelData();
                levelData.setX(x);
                levelData.setY(y);
                levelData.setName("#" + ++levelCounter);
                PixelLogicLevel level = createLevel(collection, levelData);
                levels.add(level);
            }
        }
        return levels;
    }

    private static List<PixelLogicLevel> loadByList(PixelLogicLevelCollection collection) {
        List<PixelLogicLevel> levelList = loadOrdered(collection);
        // preserve order
        if (collection.getPreserveOrder()) {
            return levelList;
        }
        // order is pre calculated -> cause calculating the complexity takes to much time
        List<Integer> order = collection.getOrder();
        if (order != null && order.size() == levelList.size()) {
            List<PixelLogicLevel> orderedLevelList = new ArrayList<PixelLogicLevel>(levelList.size());
            for (int i = 0; i < levelList.size(); i++) {
                orderedLevelList.add(levelList.get(order.get(i)));
            }
            return orderedLevelList;
        }
        // not pre calculated -> we analyze the complexity and print it -> to hard to put it back ;)
        Map<PixelLogicLevel, Float> complexityMap = new HashMap<PixelLogicLevel, Float>();
        for (PixelLogicLevel level : levelList) {
            PixelLogicComplexityAnalyzerResult result = PixelLogicComplexityAnalyzer.analyze(level);
            complexityMap.put(level, result.getComplexity());
        }
        List<PixelLogicLevel> pixelLogicLevels = PixelLogicUtil.sortByValue(complexityMap);
        // print the order to console -> need to manually put it back to the collection
        StringBuilder orderString = new StringBuilder("\"order\": [");
        String prefix = "";
        for (PixelLogicLevel level : pixelLogicLevels) {
            orderString.append(prefix).append(levelList.indexOf(level));
            prefix = ",";
        }
        orderString.append("]");
        Gdx.app.log("level loader order for " + collection.getName(), orderString.toString());
        // return by complexity: easiest level first
        return pixelLogicLevels;
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
        return createLevel(collection, levelData);
    }

    private static PixelLogicLevel createLevel(PixelLogicLevelCollection collection, PixelLogicLevelData levelData) {
        try {
            Integer[][] pixmap = getImageData(collection, levelData, collection.getPixmap());
            Integer[][] levelmap = getImageData(collection, levelData, collection.getLevelmap());
            if (levelmap == null) {
                levelmap = pixmap;
            }
            Boolean[][] level = PixelLogicUtil.toLevelData(levelmap);
            PixelLogicLevel pixelLogicLevel = new PixelLogicLevel(levelData.getName(), level, pixmap, collection.getPreserveSize());
            if (levelData.getLanguages() != null) {
                String language = Locale.getDefault().getLanguage();
                pixelLogicLevel.setDisplayName(levelData.getLanguages().get(language));
            }
            return pixelLogicLevel;
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
