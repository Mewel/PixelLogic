package de.mewel.pixellogic.event;

public class PixelLogicSecretLevelEvent extends PixelLogicEvent {

    public enum Type {find, beat}

    private final Type type;

    public PixelLogicSecretLevelEvent(Object source, Type type) {
        super(source);
        this.type = type;
    }

    public boolean isFind() {
        return Type.find.equals(type);
    }

    public boolean isBeat() {
        return Type.beat.equals(type);
    }

}
