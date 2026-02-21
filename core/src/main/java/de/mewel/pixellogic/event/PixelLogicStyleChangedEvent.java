package de.mewel.pixellogic.event;

import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicStyleChangedEvent extends PixelLogicEvent {

    private final PixelLogicUIStyle style;

    public PixelLogicStyleChangedEvent(Object source, PixelLogicUIStyle style) {
        super(source);
        this.style = style;
    }

    public PixelLogicUIStyle getStyle() {
        return style;
    }

}
