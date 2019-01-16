package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicAchievementNoBlock extends PixelLogicAchievement {

    private Boolean isThePuzzelBigEnough;

    private Boolean blocked;

    PixelLogicAchievementNoBlock(PixelLogicAssets assets) {
        super(assets);
        this.isThePuzzelBigEnough = null;
        this.blocked = null;
    }

    @Override
    public String getId() {
        return "noBlock";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        // reset on level start
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent statusChangeEvent = (PixelLogicLevelStatusChangeEvent) event;
            PixelLogicLevel level = statusChangeEvent.getLevel();
            // 6x6, 5*7, 4*8 are all ok
            if (PixelLogicLevelStatus.loaded.equals(statusChangeEvent.getStatus())) {
                this.isThePuzzelBigEnough = level.getRows() * level.getColumns() >= 32;
                this.blocked = false;
                return false;
            }
        }
        // reset on board is empty
        if (event instanceof PixelLogicBoardChangedEvent) {
            PixelLogicBoardChangedEvent changedBoardEvent = (PixelLogicBoardChangedEvent) event;
            if (changedBoardEvent.getLevel().isEmpty()) {
                this.blocked = false;
            }
        }
        if (this.isThePuzzelBigEnough == null || this.blocked == null || !this.isThePuzzelBigEnough || this.blocked) {
            return false;
        }
        // check if level is solved or destroyed
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent statusChangeEvent = (PixelLogicLevelStatusChangeEvent) event;
            PixelLogicLevelStatus status = statusChangeEvent.getStatus();
            if (PixelLogicLevelStatus.solved.equals(status)) {
                // ignore certain level's
                PixelLogicLevel level = statusChangeEvent.getLevel();
                if ("Heart".equals(level.getName()) || "Windows".equals(level.getName())) {
                    return false;
                }
                // achievement reached
                return !this.blocked;
            }
            if (PixelLogicLevelStatus.destroyed.equals(status)) {
                this.isThePuzzelBigEnough = null;
                this.blocked = null;
                return false;
            }
        }
        // check if something is blocked by the user
        if (event instanceof PixelLogicBoardChangedEvent) {
            PixelLogicBoardChangedEvent changedBoardEvent = (PixelLogicBoardChangedEvent) event;
            Boolean value = changedBoardEvent.getValue();
            if (changedBoardEvent.isPixelChanged()) {
                if (value != null && !value) {
                    this.blocked = true;
                }
                return false;
            }
        }
        return false;
    }

}
