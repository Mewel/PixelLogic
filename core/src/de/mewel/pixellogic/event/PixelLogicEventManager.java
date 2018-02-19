package de.mewel.pixellogic.event;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class PixelLogicEventManager {

    private Queue<PixelLogicEvent> eventQueue;

    private List<PixelLogicListener> listeners;

    public PixelLogicEventManager() {
        this.eventQueue = new LinkedBlockingQueue<PixelLogicEvent>();
        this.listeners = new ArrayList<PixelLogicListener>();
    }

    public void fire(PixelLogicEvent event) {
        this.eventQueue.offer(event);
        if (this.eventQueue.size() > 1) {
            return;
        }
        while ((event = this.eventQueue.peek()) != null) {
            for (PixelLogicListener listener : new ArrayList<PixelLogicListener>(listeners)) {
                listener.handle(event);
            }
            this.eventQueue.remove();
        }
    }

    public void listen(PixelLogicListener listener) {
        this.listeners.add(listener);
        Gdx.app.log("listeners: ", this.listeners.toString());
    }

    public void remove(PixelLogicListener listener) {
        this.listeners.remove(listener);
    }

    public void dispose() {
        this.eventQueue.clear();
        this.listeners.clear();
    }

}
