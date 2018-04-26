package de.mewel.pixellogic.ui.screen;

import de.mewel.pixellogic.achievements.PixelLogicAchievementDieHard;
import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.layer.PixelLogicUIAchievementLayer;
import de.mewel.pixellogic.ui.layer.PixelLogicUIPageLayer;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialFinishedPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialPage;

public class PixelLogicUIAppScreen extends PixelLogicUILayeredScreen {

    private PixelLogicUIPageLayer pageLayer;

    private PixelLogicUIAchievementLayer achievementLayer;

    private PixelLogicAchievements achievements;

    public PixelLogicUIAppScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        // achievements
        this.achievements = new PixelLogicAchievements(eventManager);
        this.achievementLayer = new PixelLogicUIAchievementLayer(assets, eventManager);

        // page
        this.pageLayer = new PixelLogicUIPageLayer(assets, eventManager);
        PixelLogicUILevelPage levelScreen = new PixelLogicUILevelPage(assets, eventManager);
        PixelLogicUITimeTrialPage timeTrialScreen = new PixelLogicUITimeTrialPage(assets, eventManager);
        PixelLogicUITimeTrialFinishedPage timeTrialFinishedScreen = new PixelLogicUITimeTrialFinishedPage(assets, eventManager);
        this.pageLayer.add(PixelLogicUIPageId.level, levelScreen);
        this.pageLayer.add(PixelLogicUIPageId.timeTrial, timeTrialScreen);
        this.pageLayer.add(PixelLogicUIPageId.timeTrialFinished, timeTrialFinishedScreen);
        this.pageLayer.activate(timeTrialScreen, new PixelLogicUIPageProperties());

        // add
        this.add(this.pageLayer);
        this.add(this.achievementLayer);

        achievements.fireAchieved(new PixelLogicAchievementDieHard());
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.pageLayer != null) {
            this.pageLayer.dispose();
        }
        if (this.achievementLayer != null) {
            this.achievementLayer.dispose();
        }
        if (this.achievements != null) {
            this.achievements.dispose();
        }
    }
}
