package de.mewel.pixellogic.mode;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;

public interface PixelLogicLevelMode {

    void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager);

    void run();

}
