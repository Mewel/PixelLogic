package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCharactersMode extends PixelLogicListLevelMode {

    @Override
    protected String getName() {
        return "characters";
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> characters = PixelLogicLevelLoader.load(getCollection());
        return new ArrayList<PixelLogicLevel>(characters);
    }

    public PixelLogicLevelCollection getCollection() {
        return getAssets().getLevelCollection("characters");
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);

    }
}
