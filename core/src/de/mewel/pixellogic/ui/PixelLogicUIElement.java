package de.mewel.pixellogic.ui;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;

public interface PixelLogicUIElement {

    PixelLogicAssets getAssets();

    PixelLogicEventManager getEventManager();

}
