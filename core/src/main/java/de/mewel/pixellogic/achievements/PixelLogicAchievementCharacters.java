package de.mewel.pixellogic.achievements;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicCharactersFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicEvent;

public class PixelLogicAchievementCharacters extends PixelLogicAchievement {

    public PixelLogicAchievementCharacters(PixelLogicAssets assets) {
        super(assets);
    }

    @Override
    public String getId() {
        return "characters";
    }

    @Override
    boolean check(PixelLogicEvent event) {
        return event instanceof PixelLogicCharactersFinishedEvent;
    }

}
