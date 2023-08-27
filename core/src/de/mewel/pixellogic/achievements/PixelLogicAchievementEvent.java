package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicAchievementEvent extends PixelLogicEvent {

    private final PixelLogicAchievement achievement;

    public PixelLogicAchievementEvent(PixelLogicAchievements source, PixelLogicAchievement achievement) {
        super(source);
        this.achievement = achievement;
    }

    public PixelLogicAchievement getAchievement() {
        return achievement;
    }

}
