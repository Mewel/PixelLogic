package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicAchievementDieHard extends PixelLogicAchievement {

    @Override
    String getName() {
        return "Die Hard";
    }

    @Override
    String getDescription() {
        return "Beat the Hard Mode in Time Trials in under 4 minutes.";
    }

    @Override
    void check(PixelLogicEvent event) {

    }

}
