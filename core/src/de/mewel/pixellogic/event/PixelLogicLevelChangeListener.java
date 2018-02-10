package de.mewel.pixellogic.event;

public interface PixelLogicLevelChangeListener {

    void onLevelChange(PixelLogicLevelChangeEvent event);

    void onLevelLoad(PixelLogicLevelChangeEvent event);

    void onLevelSolved(PixelLogicLevelChangeEvent event);

    void onLevelBeforeDestroyed(PixelLogicLevelChangeEvent event);

    void onLevelDestroyed(PixelLogicLevelChangeEvent event);

}
