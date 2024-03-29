package de.mewel.pixellogic.achievements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;

public class PixelLogicAchievements implements PixelLogicListener {

    private final PixelLogicAssets assets;

    private final PixelLogicEventManager eventManager;

    private final List<PixelLogicAchievement> achievements;

    public PixelLogicAchievements(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.achievements = new ArrayList<>();
        eventManager.listen(this);

        // clearAll();
        register();
    }

    protected void register() {
        Preferences preferences = Gdx.app.getPreferences("pixellogic_achievements");

        // add
        this.achievements.add(new PixelLogicAchievementCampaign(getAssets()));
        this.achievements.add(new PixelLogicAchievementEasyMode(getAssets()));
        this.achievements.add(new PixelLogicAchievementHardMode(getAssets()));
        this.achievements.add(new PixelLogicAchievementInsaneMode(getAssets()));
        this.achievements.add(new PixelLogicAchievementEpicMode(getAssets()));
        this.achievements.add(new PixelLogicAchievementNoBlock(getAssets()));
        this.achievements.add(new PixelLogicAchievementSecretLevelFind(getAssets()));
        this.achievements.add(new PixelLogicAchievementSecretLevelBeat(getAssets()));
        this.achievements.add(new PixelLogicAchievementPicture(getAssets()));
        this.achievements.add(new PixelLogicAchievementCharacters(getAssets()));

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
        Preferences preferences = Gdx.app.getPreferences("pixellogic_achievements");
        achievement.achieved();
        preferences.putBoolean(getDoneKey(achievement), true);
        preferences.flush();
        eventManager.fire(new PixelLogicAchievementEvent(this, achievement));
    }

    public void dispose() {
        this.eventManager.remove(this);
    }

    public PixelLogicAssets getAssets() {
        return assets;
    }

    public void clearAll() {
        Preferences preferences = Gdx.app.getPreferences("pixellogic_achievements");
        preferences.clear();
        preferences.flush();
    }

    public List<PixelLogicAchievement> list() {
        return this.achievements;
    }

}
