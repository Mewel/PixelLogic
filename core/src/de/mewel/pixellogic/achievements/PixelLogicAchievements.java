package de.mewel.pixellogic.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;

public class PixelLogicAchievements implements PixelLogicListener {

    private PixelLogicEventManager eventManager;

    private List<PixelLogicAchievement> achievements;

    public PixelLogicAchievements(PixelLogicEventManager eventManager) {
        this.eventManager = eventManager;
        this.achievements = new ArrayList<PixelLogicAchievement>();
        eventManager.listen(this);

        clearAll();
        register();
    }

    protected void register() {
        Preferences preferences = Gdx.app.getPreferences("achievements");

        // add
        this.achievements.add(new PixelLogicAchievementEasyMode());
        this.achievements.add(new PixelLogicAchievementHardMode());
        this.achievements.add(new PixelLogicAchievementInsaneMode());
        this.achievements.add(new PixelLogicAchievementEpicMode());
        this.achievements.add(new PixelLogicAchievementNoBlock());
        this.achievements.add(new PixelLogicAchievementSecretLevelFind());
        this.achievements.add(new PixelLogicAchievementSecretLevelBeat());

        // set if done
        for (PixelLogicAchievement achievement : this.achievements) {
            boolean done = preferences.getBoolean(getDoneKey(achievement));
            if (done) {
                achievement.achieved();
            }
        }
    }

    private String getDoneKey(PixelLogicAchievement achievement) {
        return achievement.getId() + "_done";
    }

    @Override
    public void handle(PixelLogicEvent event) {
        for (PixelLogicAchievement achievement : this.achievements) {
            if (achievement.isDone()) {
                continue;
            }
            if (achievement.check(event)) {
                fireAchieved(achievement);
            }
        }
    }

    public void fireAchieved(PixelLogicAchievement achievement) {
        Preferences preferences = Gdx.app.getPreferences("achievements");
        achievement.achieved();
        preferences.putBoolean(getDoneKey(achievement), true);
        preferences.flush();
        eventManager.fire(new PixelLogicAchievementEvent(this, achievement));
    }

    public void dispose() {
        this.eventManager.remove(this);
    }

    public void clearAll() {
        Preferences preferences = Gdx.app.getPreferences("achievements");
        preferences.clear();
        preferences.flush();
    }

    public List<PixelLogicAchievement> list() {
        return this.achievements;
    }

}
