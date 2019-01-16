package de.mewel.pixellogic.spike;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicPictureMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzer;
import de.mewel.pixellogic.util.PixelLogicComplexityAnalyzerResult;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PicturesSpike extends Game implements PixelLogicGlobal {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen appScreen;

    private PixelLogicAchievements achievements;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(assets, eventManager);

        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        PixelLogicLevelCollection collection = getAssets().getLevelCollection("pictures/starrynight");
        final PixelLogicPictureMode mode = new PixelLogicPictureMode(collection);
        mode.setup(this);
        mode.reset();

        final List<PixelLogicLevel> levels = mode.getLevels();
        for (PixelLogicLevel level : levels) {
            boolean solvable = PixelLogicUtil.isSolvable(level.getLevelData());
            Gdx.app.log("level loader", level.getName() + " is " + (solvable ? "valid" : "invalid--------------------------------------"));
            PixelLogicComplexityAnalyzerResult result = PixelLogicComplexityAnalyzer.analyze(level);
            Gdx.app.log("level loader", level.getName() + " complexity " + result.getComplexity());
        }

        PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
        pageProperties.put("solvedAnimation", "picture");
        pageProperties.put("pictureCollection", mode.getCollection());
        pageProperties.put("menuBackId", PixelLogicUIPageId.mainMenu);
        getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties, new Runnable() {
            @Override
            public void run() {
                mode.activate();
                mode.run(levels.get(7 - 1));
            }
        });
    }

    @Override
    public void dispose() {
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
        }
        if (this.achievements != null) {
            this.achievements.dispose();
        }
        super.dispose();
    }

    public PixelLogicAssets getAssets() {
        return assets;
    }

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


}
