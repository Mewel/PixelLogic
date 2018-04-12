package de.mewel.pixellogic.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import de.mewel.pixellogic.event.PixelLogicEvent;

public abstract class PixelLogicAchievement {

    private boolean done;

    public PixelLogicAchievement() {
        this.done = false;
    }

    abstract String getName();

    abstract String getDescription();

    abstract boolean check(PixelLogicEvent event);

    public void fireAchieved() {

    }

    public void load() {
        Preferences achievements = Gdx.app.getPreferences("achievements");
        this.done = achievements.getBoolean(getId() + "_done");
    }

    public String getId() {
        try {
            return Arrays.toString(getName().getBytes("UTF-8")).replaceAll("\\D+", "_");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Achievements: Couldn't get id of " + getName());
        }
    }

    boolean isDone() {
        return this.done;
    }

}
