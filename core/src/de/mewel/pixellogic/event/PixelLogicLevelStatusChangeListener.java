package de.mewel.pixellogic.event;

public interface PixelLogicLevelStatusChangeListener {

    void onLevelChange(PixelLogicLevelStatusChangeEvent event);

    void onLevelLoad(PixelLogicLevelStatusChangeEvent event);

    void onLevelSolved(PixelLogicLevelStatusChangeEvent event);

    void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event);

    void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event);

}
