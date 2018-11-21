package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicPictureMode extends PixelLogicListLevelMode {

    @Override
    public String getName() {
        return "picture";
    }

    @Override
    protected void onFinished() {
        super.onFinished();
        // back to main menu
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", PixelLogicUIPageId.mainMenu);
        this.getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> levels = PixelLogicLevelLoader.load(getCollection());
        return new ArrayList<PixelLogicLevel>(levels);
    }

    public PixelLogicLevelCollection getCollection() {
        return getAssets().getLevelCollection("pictures/davinci");
    }

}
