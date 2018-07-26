package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicSecretLevelEvent;

public class PixelLogicAchievementSecretLevelBeat extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Master of all secrets";
    }

    @Override
    public String getDescription() {
        return "beat the secret level";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicSecretLevelEvent && ((PixelLogicSecretLevelEvent) event).isBeat();
    }

}
