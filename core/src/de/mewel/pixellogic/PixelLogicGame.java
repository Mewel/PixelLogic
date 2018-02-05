package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevelResolutionManager;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.mode.PixelLogicRandomLevelMode;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.screen.PixelLogicScreenManager;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicCollectionManager.instance().setup();
        PixelLogicScreenManager.instance().setup(this);

        /*PixelLogicLevelCollection levelCollection = PixelLogicCollectionManager.instance().getCollection("characters2");
        PixelLogicLevelMode mode = new PixelLogicRandomLevelMode(levelCollection);
        mode.run();*/

        PixelLogicLevelMode mode = new PixelLogicCampaignMode();
        mode.run();
    }

    @Override
    public void dispose() {
        PixelLogicGUILevelResolutionManager.instance().dispose();
        super.dispose();
    }

}
