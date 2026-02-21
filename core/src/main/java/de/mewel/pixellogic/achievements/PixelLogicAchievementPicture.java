package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicPictureFinishedEvent;

public class PixelLogicAchievementPicture extends PixelLogicAchievement {

    public PixelLogicAchievementPicture(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "picture";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicPictureFinishedEvent;
    }

}
