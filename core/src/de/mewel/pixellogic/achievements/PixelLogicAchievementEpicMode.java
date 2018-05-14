package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementEpicMode extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "just... EPIC";
    }

    @Override
    public String getDescription() {
        return "Beat the Time Trial Epic Mode in under 9 minutes.";
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
        String mode = properties.getString("mode");
        if (!PixelLogicTimeTrialModeOptions.Modes.time_trial_epic.name().equals(mode)) {
            return false;
        }
        final Long time = properties.getLong("time");
        return time <= 540000;
    }

}
