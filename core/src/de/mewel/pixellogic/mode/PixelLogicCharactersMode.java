package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCharactersMode extends PixelLogicListLevelMode {

    @Override
    protected String getName() {
        return "characters";
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        return new ArrayList<PixelLogicLevel>(PixelLogicLevelLoader.load(getAssets().getLevelCollection("characters")));
    }

}
