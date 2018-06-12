package de.mewel.pixellogic.ui.screen;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.ui.layer.PixelLogicUIAchievementLayer;
import de.mewel.pixellogic.ui.layer.PixelLogicUIPageLayer;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIMainPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialFinishedPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialPage;

public class PixelLogicUIAppScreen extends PixelLogicUILayeredScreen {

    private PixelLogicUIPageLayer pageLayer;

    private PixelLogicUIAchievementLayer achievementLayer;

    private PixelLogicAchievements achievements;

    public PixelLogicUIAppScreen(PixelLogicGlobal global) {
        super(global.getAssets(), global.getEventManager());

        // achievements
        this.achievements = new PixelLogicAchievements(getEventManager());
        this.achievementLayer = new PixelLogicUIAchievementLayer(getAssets(), getEventManager());

        // page
        this.pageLayer = new PixelLogicUIPageLayer(getAssets(), getEventManager());
        this.pageLayer.add(PixelLogicUIPageId.mainMenu, new PixelLogicUIMainPage(global));
        this.pageLayer.add(PixelLogicUIPageId.level, new PixelLogicUILevelPage(global));
        this.pageLayer.add(PixelLogicUIPageId.timeTrial, new PixelLogicUITimeTrialPage(global));
        this.pageLayer.add(PixelLogicUIPageId.timeTrialFinished, new PixelLogicUITimeTrialFinishedPage(global));

        // add
        this.add(this.pageLayer);
        this.add(this.achievementLayer);
    }

    public void setPage(PixelLogicUIPageId pageId, PixelLogicUIPageProperties properties) {
        PixelLogicUIPage page = this.pageLayer.get(pageId);
        this.pageLayer.activate(page, properties);
    }

    public PixelLogicUIPage getPage(PixelLogicUIPageId pageId) {
        return this.pageLayer.get(pageId);
    }

    public boolean isActive(PixelLogicUIPageId pageId) {
        return this.pageLayer.isActive(pageId);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.achievements != null) {
            this.achievements.dispose();
        }
    }

}
