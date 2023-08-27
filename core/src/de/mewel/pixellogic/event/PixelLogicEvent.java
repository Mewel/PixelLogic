package de.mewel.pixellogic.event;

public abstract class PixelLogicEvent {

    private final Object source;

    public PixelLogicEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

}
