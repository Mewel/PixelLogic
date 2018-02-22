package de.mewel.pixellogic.event;

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
