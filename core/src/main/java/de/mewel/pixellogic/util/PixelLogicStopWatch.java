package de.mewel.pixellogic.util;

public class PixelLogicStopWatch {

    private boolean running;

    private boolean paused;

    private long start;

    private long pausedStart;

    private long end;

    public PixelLogicStopWatch() {
        reset();
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isPaused() {
        return paused;
    }

    public PixelLogicStopWatch reset() {
        running = false;
        paused = false;
        start = -1;
        pausedStart = -1;
        end = -1;
        return this;
    }

    public long start() {
        start = System.currentTimeMillis();
        running = true;
        paused = false;
        pausedStart = -1;
        return elapsed();
    }

    public long startOrResume() {
        if(isRunning()) {
            resume();
        } else {
            start();
        }
        return elapsed();
    }

    public long stop() {
        if (!isRunning()) {
            return -1;
        }
        if (isPaused()) {
            running = false;
            paused = false;
            end = pausedStart;
            return pausedStart - start;
        }
        end = System.currentTimeMillis();
        running = false;
        return end - start;
    }

    public long pause() {
        if (!isRunning()) {
            return -1;
        }
        if (isPaused()) {
            return pausedStart - start;
        }
        pausedStart = System.currentTimeMillis();
        paused = true;
        return pausedStart - start;
    }

    public long resume() {
        if (isPaused() && isRunning()) {
            start = System.currentTimeMillis() - (pausedStart - start);
            paused = false;
        }
        return elapsed();
    }

    public long elapsed() {
        if (isRunning()) {
            return isPaused() ? pausedStart - start : System.currentTimeMillis() - start;
        }
        return end - start;
    }

}
