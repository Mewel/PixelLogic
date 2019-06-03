package de.mewel.pixellogic.spike;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicCharactersMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class CharactersLevelTest extends AbstractSpikeGame {

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(assets, eventManager);
        this.audio = new PixelLogicAudio(assets, eventManager);

        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        final PixelLogicCharactersMode mode = new PixelLogicCharactersMode();
        mode.setup(this);
        mode.reset();
        PixelLogicLevel level = mode.findLevel("Sideshow Mel");
        mode.activate();
        mode.run(level);

        PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
        pageProperties.put("menuBackId", PixelLogicUIPageId.mainMenu);
        getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties);
    }

}
