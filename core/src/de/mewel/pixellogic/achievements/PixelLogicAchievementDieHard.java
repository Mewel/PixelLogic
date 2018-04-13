package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenId;
import de.mewel.pixellogic.ui.screen.event.PixelLogicScreenChangeEvent;

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
    boolean check(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicScreenChangeEvent)) {
            return false;
        }
        PixelLogicScreenChangeEvent screenChangeEvent = (PixelLogicScreenChangeEvent) event;
        if (PixelLogicScreenId.timeTrialFinished.equals(screenChangeEvent.getScreenId())) {
            final Long time = screenChangeEvent.getData().getLong("time");
            if(time < 240000) {
                return true;
            }
        }
        return false;
    }

}
