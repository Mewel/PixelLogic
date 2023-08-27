package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.graphics.FPSLogger;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicStyleChangedEvent;
import de.mewel.pixellogic.ui.layer.PixelLogicUIAchievementLayer;
import de.mewel.pixellogic.ui.layer.PixelLogicUILayer;
import de.mewel.pixellogic.ui.layer.PixelLogicUIPageLayer;
import de.mewel.pixellogic.ui.page.PixelLogicUIAboutPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIAchievementsPage;
import de.mewel.pixellogic.ui.page.PixelLogicUICharactersPage;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIMainPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.PixelLogicUIPicturePage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPlayPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialFinishedPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialPage;
import de.mewel.pixellogic.ui.page.PixelLogicUITutorialLevelPage;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUIAppScreen extends PixelLogicUILayeredScreen implements PixelLogicListener {

    private final PixelLogicUIPageLayer pageLayer;

    private final PixelLogicUIAchievementLayer achievementLayer;

    protected FPSLogger fpsLogger = new FPSLogger();

    public PixelLogicUIAppScreen(PixelLogicGlobal global) {
        super(global);

        // achievements
        this.achievementLayer = new PixelLogicUIAchievementLayer(global);

        // page
        this.pageLayer = new PixelLogicUIPageLayer(global);
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

        // listen
        getEventManager().listen(this);
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

    @Override
    public void render(float delta) {
        super.render(delta);
        // fpsLogger.log();
        getAudio().act(delta);
    }

    @Override
    public void dispose() {
        getEventManager().remove(this);
        super.dispose();
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if(event instanceof PixelLogicStyleChangedEvent) {
            PixelLogicUIStyle newStyle = ((PixelLogicStyleChangedEvent) event).getStyle();
            for (PixelLogicUILayer layer : getLayers()) {
                layer.styleChanged(newStyle);
            }
        }
    }

}
