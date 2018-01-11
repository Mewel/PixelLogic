package de.mewel.pixellogic.mode;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;

public interface PixelLogicLevelMode {

    void run(PixelLogicLevelCollection collection);

    PixelLogicLevel next();

}
