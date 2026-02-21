package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicSecretLevelEvent;

public class PixelLogicAchievementSecretLevelBeat extends PixelLogicAchievement {

    public PixelLogicAchievementSecretLevelBeat(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "secretLevelBeat";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicSecretLevelEvent && ((PixelLogicSecretLevelEvent) event).isBeat();
    }

}
