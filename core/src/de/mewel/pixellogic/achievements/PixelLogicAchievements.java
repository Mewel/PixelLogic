package de.mewel.pixellogic.achievements;

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
        register();
    }

    protected void register() {
        this.achievements.add(new PixelLogicAchievementDieHard());
        for(PixelLogicAchievement achievement : this.achievements) {
            achievement.load();
        }
    }

    @Override
    public void handle(PixelLogicEvent event) {
        for(PixelLogicAchievement achievement : this.achievements) {
            if(achievement.isDone()) {
                continue;
            }
            achievement.check(event);
        }
    }

    public void dispose() {
        this.eventManager.remove(this);
    }

}
