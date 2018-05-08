package de.mewel.pixellogic.ui.level.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;

public class PixelLogicLevelStatusChangeEvent extends PixelLogicEvent {

    private PixelLogicLevel level;

    private PixelLogicLevelStatus status;

    public PixelLogicLevelStatusChangeEvent(PixelLogicUILevelPage source, PixelLogicLevel level, PixelLogicLevelStatus status) {
        super(source);
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
    public PixelLogicUILevelPage getSource() {
        return (PixelLogicUILevelPage) super.getSource();
    }

}
