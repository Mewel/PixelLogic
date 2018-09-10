package de.mewel.pixellogic.model;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.List;

public class PixelLogicLevelCollection {

    private String id;
    private String name;
    private Integer pixmapWidth;
    private Integer pixmapHeight;
    private Boolean preserveSize = false;
    private Boolean preserveOrder = false;
    private List<PixelLogicLevelData> level;

    private Pixmap pixmap;
    private Pixmap levelmap;

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

    public PixelLogicLevelData getLevel(String name) {
        Integer index = getLevelIndexByName(name);
        return index != null ? level.get(index) : null;
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

    public void setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
    }

    public void setLevelmap(Pixmap levelmap) {
        this.levelmap = levelmap;
    }

    public Pixmap getPixmap() {
        return pixmap;
    }

    public Pixmap getLevelmap() {
        return levelmap;
    }

    public Boolean getPreserveOrder() {
        return preserveOrder;
    }

    public void setPreserveOrder(Boolean preserveOrder) {
        this.preserveOrder = preserveOrder;
    }

    public Boolean getPreserveSize() {
        return preserveSize;
    }

    public void setPreserveSize(Boolean preserveSize) {
        this.preserveSize = preserveSize;
    }

    public Sprite getSprite(String name, Texture levelTexture) {
        return getSprite(name, levelTexture, pixmapWidth, pixmapHeight);
    }

    public Sprite getSprite(String name, Texture levelTexture, int fixedWidth, int fixedHeight) {
        PixelLogicLevelData data = getLevel(name);
        Integer x = data.getX();
        Integer y = data.getY();
        if (x == null || y == null) {
            Integer index = getLevelIndexByName(name);
            int columns = levelTexture.getWidth() / pixmapWidth;
            y = index / columns;
            x = index % columns;
        }
        return new Sprite(levelTexture, x * pixmapWidth, y * pixmapHeight, fixedWidth, fixedHeight);
    }

}
