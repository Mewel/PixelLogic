package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.gui.PixelLogicGUILevelResolutionManager;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.mode.PixelLogicRandomLevelMode;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.gui.screen.PixelLogicScreenManager;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicCollectionManager.instance().setup();
        PixelLogicScreenManager.instance().setup(this);

        PixelLogicLevelCollection levelCollection = PixelLogicCollectionManager.instance().getCollection("characters2");
        PixelLogicLevelMode mode = new PixelLogicRandomLevelMode();
        mode.run(levelCollection);
    }

    @Override
    public void dispose() {
        PixelLogicGUILevelResolutionManager.instance().dispose();
        super.dispose();
    }

}
