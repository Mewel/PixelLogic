package de.mewel.pixellogic;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public interface PixelLogicGlobal {

    PixelLogicAssets getAssets();

    PixelLogicEventManager getEventManager();

    PixelLogicUIAppScreen getAppScreen();

    PixelLogicAchievements getAchievements();

    PixelLogicAudio getAudio();

}
