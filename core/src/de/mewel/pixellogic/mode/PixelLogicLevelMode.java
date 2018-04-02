package de.mewel.pixellogic.mode;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public interface PixelLogicLevelMode {

    void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager);

    void dispose();

    void run();

}
