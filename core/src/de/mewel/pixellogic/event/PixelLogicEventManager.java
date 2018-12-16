package de.mewel.pixellogic.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PixelLogicEventManager {

    private Queue<Tuple> eventQueue;

    private List<PixelLogicListener> listeners;

    public PixelLogicEventManager() {
        this.eventQueue = new LinkedBlockingQueue<Tuple>();
        this.listeners = new ArrayList<PixelLogicListener>();
    }

    public void fire(PixelLogicEvent event) {
        this.fire(event, null);
    }

    public void fire(PixelLogicEvent event, Runnable after) {
        this.eventQueue.offer(new Tuple(event, after));
        if (this.eventQueue.size() > 1) {
            return;
        }
        Tuple tuple;
        while ((tuple = this.eventQueue.peek()) != null) {
            for (PixelLogicListener listener : new ArrayList<PixelLogicListener>(listeners)) {
                listener.handle(tuple.event);
            }
            if (tuple.after != null) {
                tuple.after.run();
            }
            this.eventQueue.remove();
        }
    }

    public void listen(PixelLogicListener listener) {
        this.listeners.add(listener);
    }

    public void remove(PixelLogicListener listener) {
        this.listeners.remove(listener);
    }

    public void dispose() {
        this.eventQueue.clear();
        this.listeners.clear();
    }

    private static class Tuple {

        Tuple(PixelLogicEvent event, Runnable after) {
            this.event = event;
            this.after = after;
        }

        public PixelLogicEvent event;

        public Runnable after;

    }

}
