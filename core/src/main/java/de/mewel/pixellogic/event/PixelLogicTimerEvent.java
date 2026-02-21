package de.mewel.pixellogic.event;

public class PixelLogicTimerEvent extends PixelLogicEvent {

    public enum Status {
        start, stop, pause, resume, reset
    }

    private final Status status;

    private final long time;

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
