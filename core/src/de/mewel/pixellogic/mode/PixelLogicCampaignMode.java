package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicCampaignFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCampaignMode extends PixelLogicListLevelMode {

    @Override
    public String getName() {
        return "campaign";
    }

    @Override
    protected void runLevel(PixelLogicLevel level) {
        PixelLogicUILevelPage levelPage = getLevelPage();
        int levelIndex = getLevels().indexOf(level) + 1;
        levelPage.getMenu().setStatusText("Level: " + levelIndex + "/" + getLevels().size());
        super.runLevel(level);
    }

    private PixelLogicUILevelPage getLevelPage() {
        return (PixelLogicUILevelPage) getAppScreen().getPage(PixelLogicUIPageId.level);
    }

    @Override
    protected void onFinished() {
        super.onFinished();

        // finished event -> adds achievement
        this.getEventManager().fire(new PixelLogicCampaignFinishedEvent(this));

        // back to play page
        this.getAppScreen().setPage(PixelLogicUIPageId.play);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                getLevelPage().getMenu().clearStatusText();
            }
        }
        if (event instanceof PixelLogicUIPageChangeEvent) {
            getLevelPage().getMenu().clearStatusText();
        }
        super.handle(event);
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> levels = new ArrayList<PixelLogicLevel>();
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/easy")));
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/medium")));
        levels.addAll(PixelLogicLevelLoader.load(getAssets().getLevelCollection("campaign/hard")));
        return levels;
    }

}
