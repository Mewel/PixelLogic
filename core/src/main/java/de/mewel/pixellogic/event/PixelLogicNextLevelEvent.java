package de.mewel.pixellogic.event;

import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicNextLevelEvent extends PixelLogicEvent {

    private final PixelLogicLevel nextLevel;

    public PixelLogicNextLevelEvent(PixelLogicLevelMode source, PixelLogicLevel nextLevel) {
        super(source);
        this.nextLevel = nextLevel;
    }

    public PixelLogicLevel getNextLevel() {
        return nextLevel;
    }

}
