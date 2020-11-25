package de.mewel.pixellogic.event;

public class PixelLogicUserEvent extends PixelLogicEvent {

    public enum Type {
        BACK_BUTTON_CLICKED, LEVEL_MENU_CLICKED, LEVEL_MENU_CLOSED, LEVEL_UNDO_CLICKED
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
