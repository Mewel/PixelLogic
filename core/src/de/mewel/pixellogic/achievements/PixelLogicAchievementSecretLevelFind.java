package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicSecretLevelEvent;

public class PixelLogicAchievementSecretLevelFind extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Secret Level";
    }

    @Override
    public String getDescription() {
        return "it's hard to find\neverything is blocked\nbut ease your mind\nand it will be unlocked";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicSecretLevelEvent && ((PixelLogicSecretLevelEvent) event).isFind();
    }

}
