package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicCampaignFinishedEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCampaignMode extends PixelLogicListLevelMode {

    @Override
    protected String getName() {
        return "campaign";
    }

    @Override
    protected void onFinished() {
        super.onFinished();

        // finished event -> adds achievement
        this.getEventManager().fire(new PixelLogicCampaignFinishedEvent(this));

        // back to main menu
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", PixelLogicUIPageId.mainMenu);
        this.getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> levels = new ArrayList<PixelLogicLevel>();
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/intro")));
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/easy")));
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/medium")));
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/hard")));
        return levels;
    }

}
