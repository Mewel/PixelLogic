package de.mewel.pixellogic;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public interface PixelLogicGlobal {

    PixelLogicAssets getAssets();

    PixelLogicEventManager getEventManager();

    PixelLogicUIAppScreen getAppScreen();

}
