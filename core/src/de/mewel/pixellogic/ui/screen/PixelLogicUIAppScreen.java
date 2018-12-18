package de.mewel.pixellogic.ui.screen;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.layer.PixelLogicUIAchievementLayer;
import de.mewel.pixellogic.ui.layer.PixelLogicUIPageLayer;
import de.mewel.pixellogic.ui.page.PixelLogicUIAboutPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIAchievementsPage;
import de.mewel.pixellogic.ui.page.PixelLogicUICharactersPage;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIMainPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPlayPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.PixelLogicUIPicturePage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialFinishedPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITutorialLevelPage;

public class PixelLogicUIAppScreen extends PixelLogicUILayeredScreen {

    private PixelLogicUIPageLayer pageLayer;

    private PixelLogicUIAchievementLayer achievementLayer;

    public PixelLogicUIAppScreen(PixelLogicGlobal global) {
        super(global.getAssets(), global.getEventManager());

        // achievements
        this.achievementLayer = new PixelLogicUIAchievementLayer(getAssets(), getEventManager());

        // page
        this.pageLayer = new PixelLogicUIPageLayer(getAssets(), getEventManager());
        this.pageLayer.add(PixelLogicUIPageId.mainMenu, new PixelLogicUIMainPage(global));
        this.pageLayer.add(PixelLogicUIPageId.level, new PixelLogicUILevelPage(global));
        this.pageLayer.add(PixelLogicUIPageId.play, new PixelLogicUIPlayPage(global));
        this.pageLayer.add(PixelLogicUIPageId.timeTrial, new PixelLogicUITimeTrialPage(global));
        this.pageLayer.add(PixelLogicUIPageId.timeTrialFinished, new PixelLogicUITimeTrialFinishedPage(global));
        this.pageLayer.add(PixelLogicUIPageId.characters, new PixelLogicUICharactersPage(global));
        this.pageLayer.add(PixelLogicUIPageId.achievements, new PixelLogicUIAchievementsPage(global));
        this.pageLayer.add(PixelLogicUIPageId.picture, new PixelLogicUIPicturePage(global));
        this.pageLayer.add(PixelLogicUIPageId.about, new PixelLogicUIAboutPage(global));
        this.pageLayer.add(PixelLogicUIPageId.tutorialLevel, new PixelLogicUITutorialLevelPage(global));

        // add
        this.add(this.pageLayer);
        this.add(this.achievementLayer);
    }

    public void setPage(PixelLogicUIPageId pageId) {
        this.setPage(pageId, new PixelLogicUIPageProperties());
    }

    public void setPage(PixelLogicUIPageId pageId, PixelLogicUIPageProperties properties) {
        PixelLogicUIPage page = this.pageLayer.get(pageId);
        this.pageLayer.activate(page, properties, null);
    }

    public void setPage(PixelLogicUIPageId pageId, PixelLogicUIPageProperties properties, Runnable after) {
        PixelLogicUIPage page = this.pageLayer.get(pageId);
        this.pageLayer.activate(page, properties, after);
    }

    public PixelLogicUIPage getPage(PixelLogicUIPageId pageId) {
        return this.pageLayer.get(pageId);
    }

    public boolean isActive(PixelLogicUIPageId pageId) {
        return this.pageLayer.isActive(pageId);
    }

    public PixelLogicUIPage getCurrentPage() {
        return this.pageLayer.getActivePage();
    }

}
