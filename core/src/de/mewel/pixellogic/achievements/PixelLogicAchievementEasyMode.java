package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementEasyMode extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Easy Peasy Lemon Squeezy";
    }

    @Override
    public String getDescription() {
        return "Beat the Time Trial Easy Mode in under 2 minutes.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicUIPageChangeEvent)) {
            return false;
        }
        PixelLogicUIPageChangeEvent screenChangeEvent = (PixelLogicUIPageChangeEvent) event;
        if (!(PixelLogicUIPageId.timeTrialFinished.equals(screenChangeEvent.getPageId()))) {
            return false;
        }
        PixelLogicUIPageProperties properties = screenChangeEvent.getData();
        final PixelLogicTimeTrialModeOptions.Mode mode = properties.get("mode");
        if (!PixelLogicTimeTrialModeOptions.Mode.time_trial_easy.equals(mode)) {
            return false;
        }
        final Long time = properties.getLong("time");
        return time <= 120000;
    }

}
