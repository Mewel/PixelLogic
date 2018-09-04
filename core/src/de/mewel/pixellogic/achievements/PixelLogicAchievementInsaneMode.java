package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementInsaneMode extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "Insane In The Membrane";
    }

    @Override
    public String getDescription() {
        return "Beat the Insane Time Trial Mode in under 8 minutes.";
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
        if (!PixelLogicTimeTrialModeOptions.Mode.time_trial_insane.equals(mode)) {
            return false;
        }
        final Long time = properties.getLong("time");
        return time <= 480000;
    }

}
