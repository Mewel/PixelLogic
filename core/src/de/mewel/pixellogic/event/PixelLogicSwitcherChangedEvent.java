package de.mewel.pixellogic.event;

import com.badlogic.gdx.scenes.scene2d.Actor;

public class PixelLogicSwitcherChangedEvent extends PixelLogicEvent {

    private boolean fillPixel;

    public PixelLogicSwitcherChangedEvent(Actor source, boolean fillPixel) {
        super(source);
        this.fillPixel = fillPixel;
    }

    public boolean isFillPixel() {
        return fillPixel;
    }

}
