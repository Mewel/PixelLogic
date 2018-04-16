package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementDieHard extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Die Hard";
    }

    @Override
    public String getDescription() {
        return "Beat the Hard Mode in Time Trials in under 4 minutes.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicUIPageChangeEvent)) {
            return false;
        }
        PixelLogicUIPageChangeEvent screenChangeEvent = (PixelLogicUIPageChangeEvent) event;
        if (PixelLogicUIPageId.timeTrialFinished.equals(screenChangeEvent.getPageId())) {
            final Long time = screenChangeEvent.getData().getLong("time");
            if (time < 240000) {
                return true;
            }
        }
        return false;
    }

}
