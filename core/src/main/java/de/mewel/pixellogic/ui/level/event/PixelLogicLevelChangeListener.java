package de.mewel.pixellogic.ui.level.event;

public interface PixelLogicLevelChangeListener {

    void onLevelChange(PixelLogicLevelStatusChangeEvent event);

    void onLevelLoad(PixelLogicLevelStatusChangeEvent event);

    void onLevelSolved(PixelLogicLevelStatusChangeEvent event);

    void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event);

    void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event);

    void onBoardChange(PixelLogicBoardChangedEvent event);

}
