package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.level.event.PixelLogicUserChangedBoardEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementSecretLevelFind extends PixelLogicAchievement {

    private boolean isHardCoreMode = false;

    @Override
    public String getName() {
        return "Team Secret Level";
    }

    @Override
    public String getDescription() {
        return "Find the secret level. I know its 'hard', but maybe, you get a Puppy as a reward?";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        checkHardCoreMode(event);
        if (!this.isHardCoreMode) {
            return false;
        }
        if (event instanceof PixelLogicUserChangedBoardEvent) {
            PixelLogicUserChangedBoardEvent changedBoardEvent = (PixelLogicUserChangedBoardEvent) event;
            if (changedBoardEvent.getLevel().isFilled()) {
                return true;
            }
        }
        return false;
    }

    private void checkHardCoreMode(PixelLogicEvent event) {
        if (!(event instanceof PixelLogicUIPageChangeEvent)) {
            return;
        }
        PixelLogicUIPageChangeEvent pageChangeEvent = (PixelLogicUIPageChangeEvent) event;
        PixelLogicUIPageProperties properties = pageChangeEvent.getData();
        final PixelLogicTimeTrialModeOptions.Mode mode = properties.get("mode");
        this.isHardCoreMode = PixelLogicTimeTrialModeOptions.Mode.time_trial_hardcore.equals(mode);
    }

}
