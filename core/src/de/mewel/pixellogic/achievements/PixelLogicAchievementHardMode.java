package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementHardMode extends PixelLogicAchievement {

    public PixelLogicAchievementHardMode(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "hardMode";
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
