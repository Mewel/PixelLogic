package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangedEvent;

public class PixelLogicAchievementEpicMode extends PixelLogicAchievement {

    @Override
    public String getName() {
        return "just... EPIC";
    }

    @Override
    public String getDescription() {
        return "Beat the Epic Time Trial Mode in under 9 minutes.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicUIPageChangedEvent)) {
            return false;
        }
        PixelLogicUIPageChangedEvent pageChangedEvent = (PixelLogicUIPageChangedEvent) event;
        if (!(PixelLogicUIPageId.timeTrialFinished.equals(pageChangedEvent.getPageId()))) {
            return false;
        }
        PixelLogicUIPageProperties properties = pageChangedEvent.getData();
        final PixelLogicTimeTrialModeOptions.Mode mode = properties.get("mode");
        if (!PixelLogicTimeTrialModeOptions.Mode.time_trial_epic.equals(mode)) {
            return false;
        }
        final Long time = properties.getLong("time");
        return time <= 540000;
    }

}
