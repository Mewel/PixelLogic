package de.mewel.pixellogic.model;

public class PixelLogicLevelData {

    private String name;
    private Integer x;
    private Integer y;
    private Integer difficulty;
    private Boolean[][] pixels;

    public String getName() {
        return name;
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public Boolean[][] getPixels() {
        return pixels;
    }

}
