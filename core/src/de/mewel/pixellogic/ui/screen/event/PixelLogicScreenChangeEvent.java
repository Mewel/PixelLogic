package de.mewel.pixellogic.ui.screen.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenData;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialFinishedScreen;

public class PixelLogicScreenChangeEvent extends PixelLogicEvent {

    private PixelLogicUIScreenData data;

    public PixelLogicScreenChangeEvent(Object source, PixelLogicUIScreenData data) {
        super(source);
        this.data = data;
    }

    public PixelLogicUIScreenData getData() {
        return this.data;
    }

    public String getScreenId() {
        return this.data.getString("screenId");
    }

}
