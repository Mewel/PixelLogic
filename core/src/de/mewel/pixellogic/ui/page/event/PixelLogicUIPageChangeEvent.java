package de.mewel.pixellogic.ui.page.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;

public class PixelLogicUIPageChangeEvent extends PixelLogicEvent {

    private final PixelLogicUIPageProperties data;

    public PixelLogicUIPageChangeEvent(Object source, PixelLogicUIPageProperties data) {
        super(source);
        this.data = data;
    }

    public PixelLogicUIPageProperties getData() {
        return this.data;
    }

    public PixelLogicUIPageId getPageId() {
        return this.data.get("pageId");
    }

    public PixelLogicUIPageId oldPageId() {
        return this.data.get("oldPageId");
    }

}
