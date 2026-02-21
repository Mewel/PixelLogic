package de.mewel.pixellogic.model;

import com.badlogic.gdx.utils.OrderedMap;

public class PixelLogicLevelData {

    private String name;
    private OrderedMap<String, String> languages;
    private Integer x;
    private Integer y;

    public String getName() {
        return name;
    }

    public OrderedMap<String, String> getLanguages() {
        return languages;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public void setY(Integer y) {
        this.y = y;
    }

}
