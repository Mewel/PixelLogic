package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.layer.PixelLogicUIAchievementLayer;
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
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicMusic;

import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.LEVEL_MUSIC_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.MENU_MUSIC;
import static de.mewel.pixellogic.PixelLogicConstants.MENU_MUSIC_VOLUME;

public class PixelLogicUIAppScreen extends PixelLogicUILayeredScreen implements PixelLogicListener {

    private PixelLogicUIPageLayer pageLayer;

    private PixelLogicUIAchievementLayer achievementLayer;

    private PixelLogicMusic menuMusic, levelMusic;

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

        // events
        getEventManager().listen(this);

        // music
        Music baseMenuMusic = getAssets().get().get(MENU_MUSIC);
        baseMenuMusic.setLooping(true);
        baseMenuMusic.setVolume(MENU_MUSIC_VOLUME);
        Music baseLevelMusic = getAssets().get().get(LEVEL_MUSIC);
        baseLevelMusic.setLooping(true);
        baseLevelMusic.setVolume(LEVEL_MUSIC_VOLUME);
        this.menuMusic = new PixelLogicMusic(baseMenuMusic);
        this.levelMusic = new PixelLogicMusic(baseLevelMusic);

        this.menuMusic.get().play();
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
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicUIPageChangeEvent) {
            PixelLogicUIPageChangeEvent pageChangeEvent = (PixelLogicUIPageChangeEvent) event;
            if (pageChangeEvent.getPageId().equals(PixelLogicUIPageId.level)) {
                menuMusic.fadeOut(1f, new Runnable() {
                    @Override
                    public void run() {
                        levelMusic.fadeIn(1f);
                    }
                });
            } else if (pageChangeEvent.oldPageId() != null && pageChangeEvent.oldPageId().equals(PixelLogicUIPageId.level)) {
                levelMusic.fadeOut(1f, new Runnable() {
                    @Override
                    public void run() {
                        menuMusic.fadeIn(1f);
                    }
                });
            }
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (this.menuMusic != null) {
            this.menuMusic.act(delta);
        }
        if (this.levelMusic != null) {
            Gdx.app.log("level music volume", this.levelMusic.get().getVolume() + "");
            Gdx.app.log("level music playing", this.levelMusic.get().isPlaying() + "");
            this.levelMusic.act(delta);
        }
    }

    public PixelLogicMusic getMenuMusic() {
        return menuMusic;
    }

    public PixelLogicMusic getLevelMusic() {
        return levelMusic;
    }

    @Override
    public void dispose() {
        getEventManager().remove(this);
        super.dispose();
    }

}
