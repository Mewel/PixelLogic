package de.mewel.pixellogic.spike;

import com.badlogic.gdx.Gdx;

import java.util.List;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
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

public class PicturesSpike extends AbstractSpikeGame {

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(assets, eventManager);
        this.audio = new PixelLogicAudio(assets, eventManager);

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
        getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties, () -> {
            mode.activate();
            mode.run(levels.get(7 - 1));
        });
    }

}
