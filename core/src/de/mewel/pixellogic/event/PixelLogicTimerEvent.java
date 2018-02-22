package de.mewel.pixellogic.event;

public class PixelLogicTimerEvent extends PixelLogicEvent {

    public enum Status {
        start, stop, reset
    }

    private Status status;

    private long time;

    public PixelLogicTimerEvent(Object source, Status status, long time) {
        super(source);
        this.status = status;
        this.time = time;
    }

    public long getTime() {
        return time;
    }

    public Status getStatus() {
        return status;
    }

}
