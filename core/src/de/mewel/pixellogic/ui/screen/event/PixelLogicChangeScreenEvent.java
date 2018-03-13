package de.mewel.pixellogic.ui.screen.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenData;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialFinishedScreen;

public class PixelLogicChangeScreenEvent extends PixelLogicEvent {

    private PixelLogicUIScreenData data;

    public PixelLogicChangeScreenEvent(PixelLogicUITimeTrialFinishedScreen source, PixelLogicUIScreenData data) {
        super(source);
    }

    public PixelLogicUIScreenData getData() {
        return data;
    }

}
