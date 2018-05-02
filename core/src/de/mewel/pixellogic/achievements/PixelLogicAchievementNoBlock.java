package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicUserChangedBoardEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public class PixelLogicAchievementNoBlock extends PixelLogicAchievement {

    public Boolean isThePuzzelBigEnough;

    public Boolean blocked;

    public PixelLogicAchievementNoBlock() {
        this.isThePuzzelBigEnough = null;
        this.blocked = null;
    }

    @Override
    public String getName() {
        return "!Bloc Party";
    }

    @Override
    public String getDescription() {
        return "Solve a 6x6 or bigger puzzle without blocking a single cell.";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        if(event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent statusChangeEvent = (PixelLogicLevelStatusChangeEvent) event;
            PixelLogicLevel level = statusChangeEvent.getLevel();
            if(PixelLogicLevelStatus.loaded.equals(statusChangeEvent.getStatus())) {
                // 6x6, 5*7, 4*8 are all ok
                this.isThePuzzelBigEnough = level.getRows() * level.getColumns() >= 32;
                this.blocked = false;
                return false;
            }
        }
        if(this.isThePuzzelBigEnough == null || this.blocked == null || !this.isThePuzzelBigEnough || this.blocked) {
            return false;
        }
        if(event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent statusChangeEvent = (PixelLogicLevelStatusChangeEvent) event;
            PixelLogicLevelStatus status = statusChangeEvent.getStatus();
            if(PixelLogicLevelStatus.solved.equals(status)) {
                // achievement reached
                return !this.blocked;
            }
            if(PixelLogicLevelStatus.destroyed.equals(status)) {
                this.isThePuzzelBigEnough = null;
                this.blocked = null;
                return false;
            }
        }
        if(event instanceof PixelLogicUserChangedBoardEvent) {
            PixelLogicUserChangedBoardEvent changedBoardEvent = (PixelLogicUserChangedBoardEvent) event;
            Boolean value = changedBoardEvent.getValue();
            if(changedBoardEvent.isPixelChanged()) {
                if(value != null && !value) {
                    this.blocked = true;
                }
                return false;
            }
            //if(changedBoardEvent.getLevel().isRowComplete())
        }

        return false;
    }

}
