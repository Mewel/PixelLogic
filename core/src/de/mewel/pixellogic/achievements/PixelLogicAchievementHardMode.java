package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementHardMode extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Die Hard";
    }

    @Override
    public String getDescription() {
        return "Beat the Hard Time Trial Mode in under 4 minutes.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicUIPageChangeEvent)) {
            return false;
        }
        PixelLogicUIPageChangeEvent pageChangeEvent = (PixelLogicUIPageChangeEvent) event;
        if (!(PixelLogicUIPageId.timeTrialFinished.equals(pageChangeEvent.getPageId()))) {
            return false;
        }
        PixelLogicUIPageProperties properties = pageChangeEvent.getData();
        final PixelLogicTimeTrialModeOptions.Mode mode = properties.get("mode");
        if (!PixelLogicTimeTrialModeOptions.Mode.time_trial_hardcore.equals(mode)) {
            return false;
        }
        final Long time = properties.getLong("time");
        return time <= 240000;
    }

}
