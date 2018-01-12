package de.mewel.pixellogic.event;

public class PixelLogicUserEvent extends PixelLogicEvent {

    public enum Type {
        TOOLBAR_MENU_CLICKED
    }

    private Type type;

    public PixelLogicUserEvent(Object source, Type type) {
        super(source);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

}
