package de.mewel.pixellogic.event;

import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class PixelLogicEvent {

    private Object source;

    public PixelLogicEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

}
