package de.mewel.pixellogic.ui.level.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;

public class PixelLogicLevelStatusChangeEvent extends PixelLogicEvent {

    private PixelLogicLevel level;

    private PixelLogicLevelStatus status;

    public PixelLogicLevelStatusChangeEvent(PixelLogicUILevelScreen screen, PixelLogicLevel level, PixelLogicLevelStatus status) {
        super(screen);
        this.level = level;
        this.status = status;
    }

    public PixelLogicLevelStatus getStatus() {
        return this.status;
    }

    public PixelLogicLevel getLevel() {
        return this.level;
    }

    @Override
    public PixelLogicUILevelScreen getSource() {
        return (PixelLogicUILevelScreen) super.getSource();
    }

}
