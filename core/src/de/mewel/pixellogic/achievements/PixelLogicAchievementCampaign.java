package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicCampaignFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicAchievementCampaign extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Yes We Can!";
    }

    @Override
    public String getDescription() {
        return "Finish the campaign.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicCampaignFinishedEvent;
    }

}
