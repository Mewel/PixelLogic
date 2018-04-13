package de.mewel.pixellogic.ui.screen.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenId;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenProperties;

public class PixelLogicScreenChangeEvent extends PixelLogicEvent {

    private PixelLogicUIScreenProperties data;

    public PixelLogicScreenChangeEvent(Object source, PixelLogicUIScreenProperties data) {
        super(source);
        this.data = data;
    }

    public PixelLogicUIScreenProperties getData() {
        return this.data;
    }

    public PixelLogicScreenId getScreenId() {
        return this.data.get("screenId", PixelLogicScreenId.class);
    }

}
