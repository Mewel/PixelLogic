package de.mewel.pixellogic.event;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.screen.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.screen.PixelLogicLevelScreen;

public class PixelLogicLevelChangeEvent extends PixelLogicEvent {

    private PixelLogicLevel level;

    private PixelLogicLevelStatus status;

    public PixelLogicLevelChangeEvent(PixelLogicLevelScreen screen, PixelLogicLevel level, PixelLogicLevelStatus status) {
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
    public PixelLogicLevelScreen getSource() {
        return (PixelLogicLevelScreen) super.getSource();
    }

}
