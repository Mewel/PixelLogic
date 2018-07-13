package de.mewel.pixellogic.achievements;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.mewel.pixellogic.event.PixelLogicEvent;

public abstract class PixelLogicAchievement {

    private boolean done;

    public PixelLogicAchievement() {
        this.done = false;
    }

    public abstract String getName();

    public abstract String getDescription();

    abstract boolean check(PixelLogicEvent event);

    public String getId() {
        try {
            return Arrays.toString(getName().getBytes("UTF-8")).replaceAll("\\D+", "_");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Achievements: Couldn't get id of " + getName());
        }
    }

    public void achieved() {
        this.done = true;
    }

    public boolean isDone() {
        return this.done;
    }

}
