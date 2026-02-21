package de.mewel.pixellogic.ui.level.event;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicLevelSwitcherChangedEvent extends PixelLogicEvent {

    private final boolean fillPixel;

    public PixelLogicLevelSwitcherChangedEvent(Actor source, boolean fillPixel) {
        super(source);
        this.fillPixel = fillPixel;
    }

    public boolean isFillPixel() {
        return fillPixel;
    }

}
