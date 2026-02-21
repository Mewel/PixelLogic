package de.mewel.pixellogic.ui;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyleable;

public interface PixelLogicUIElement extends PixelLogicUIStyleable {

    PixelLogicAssets getAssets();

    PixelLogicEventManager getEventManager();

    PixelLogicAudio getAudio();

}
