package de.mewel.pixellogic.event;

import de.mewel.pixellogic.mode.PixelLogicLevelMode;

public class PixelLogicLoadNextLevelEvent extends PixelLogicEvent {

    public PixelLogicLoadNextLevelEvent(PixelLogicLevelMode source) {
        super(source);
    }

}
