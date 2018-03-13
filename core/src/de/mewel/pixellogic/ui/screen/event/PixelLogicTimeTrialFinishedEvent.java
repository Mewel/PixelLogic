package de.mewel.pixellogic.ui.screen.event;

import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicTimeTrialFinishedEvent extends PixelLogicEvent {

    private long time;

    public PixelLogicTimeTrialFinishedEvent(Object source, long time) {
        super(source);
        this.time = time;
    }

    public long getTime() {
        return time;
    }

}
