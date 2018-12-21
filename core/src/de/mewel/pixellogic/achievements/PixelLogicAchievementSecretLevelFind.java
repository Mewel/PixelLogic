package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicSecretLevelEvent;

public class PixelLogicAchievementSecretLevelFind extends PixelLogicAchievement {

    public PixelLogicAchievementSecretLevelFind(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "secretLevelFind";
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
