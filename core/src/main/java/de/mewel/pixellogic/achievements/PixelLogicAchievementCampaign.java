package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicCampaignFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicAchievementCampaign extends PixelLogicAchievement {

    public PixelLogicAchievementCampaign(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "campaign";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicCampaignFinishedEvent;
    }

}
