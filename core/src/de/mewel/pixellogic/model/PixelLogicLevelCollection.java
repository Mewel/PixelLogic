package de.mewel.pixellogic.model;

import java.util.List;

public class PixelLogicLevelCollection {

    private String id;
    private String name;
    private Integer pixmapWidth;
    private Integer pixmapHeight;
    private List<PixelLogicLevelData> level;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPixmapWidth() {
        return pixmapWidth;
    }

    public void setPixmapWidth(Integer pixmapWidth) {
        this.pixmapWidth = pixmapWidth;
    }

    public Integer getPixmapHeight() {
        return pixmapHeight;
    }

    public void setPixmapHeight(Integer pixmapHeight) {
        this.pixmapHeight = pixmapHeight;
    }

    public List<PixelLogicLevelData> getLevelList() {
        return level;
    }

    public void setLevelList(List<PixelLogicLevelData> level) {
        this.level = level;
    }

    public Integer getLevelIndexByName(String name) {
        for (int i = 0; i < this.level.size(); i++) {
            PixelLogicLevelData levelData = level.get(i);
            if (levelData.getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

    public Integer indexOf(PixelLogicLevelData levelData) {
        return this.level.indexOf(levelData);
    }

}
