package de.mewel.pixellogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUISplashScreen;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyleController;

public class PixelLogicGame extends Game implements PixelLogicGlobal {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUISplashScreen splashScreen;

    private PixelLogicUIAppScreen appScreen;

    private PixelLogicAchievements achievements;

    private PixelLogicAudio audio;

    private PixelLogicUIStyleController styleController;

    @Override
    public void create() {
        // libgdx init
        Gdx.input.setCatchBackKey(true);

        this.splashScreen = new PixelLogicUISplashScreen();
        this.setScreen(this.splashScreen);

        initAndRun();
    }

    private void initAndRun() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        // load assets
                        PixelLogicGame.this.init();
                        // fade out splash screen and run game
                        splashScreen.close(1f, new Runnable() {
                            @Override
                            public void run() {
                                PixelLogicGame.this.run();
                            }
                        });
                    }
                });
            }
        }).start();
    }

    private void init() {
        // load assets
        assets = new PixelLogicAssets();
        assets.load();
        // pixel logic init
        styleController = new PixelLogicUIStyleController();
        styleController.init();
        eventManager = new PixelLogicEventManager();
        achievements = new PixelLogicAchievements(assets, eventManager);
        audio = new PixelLogicAudio(assets, eventManager);
        appScreen = new PixelLogicUIAppScreen(PixelLogicGame.this);
    }

    private void run() {
        // set screen and show main menu
        setScreen(appScreen);
        appScreen.setPage(PixelLogicUIPageId.mainMenu, new PixelLogicUIPageProperties());
    }

    @Override
    public void dispose() {
        if (this.audio != null) {
            this.audio.dispose();
        }
        if (this.splashScreen != null) {
            this.splashScreen.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
        }
        if (this.appScreen != null) {
            this.appScreen.dispose();
        }
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.achievements != null) {
            this.achievements.dispose();
        }
        super.dispose();
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    @Override
    public PixelLogicUIAppScreen getAppScreen() {
        return appScreen;
    }

    @Override
    public PixelLogicAchievements getAchievements() {
        return achievements;
    }

    @Override
    public PixelLogicAudio getAudio() {
        return audio;
    }

    @Override
    public PixelLogicUIStyle getStyle() {
        return styleController.get();
    }

    @Override
    public PixelLogicUIStyleController getStyleController() {
        return styleController;
    }

}
