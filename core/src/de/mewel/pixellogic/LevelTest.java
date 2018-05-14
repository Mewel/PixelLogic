package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class LevelTest extends Game implements PixelLogicGlobal {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen appScreen;

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        final PixelLogicTimeTrialMode mode = new PixelLogicTimeTrialMode(new TestOptions());
        mode.setup(this);
        mode.run();
    }

    public static final class TestOptions extends PixelLogicTimeTrialModeOptions {

        public TestOptions() {
            this.id = Modes.time_trial_hardcore.name();
            this.name = "HARD MODE";
            this.levelSize = new int[]{8};
            this.levelSizeOffset = new int[]{3};
            this.levelMinDifficulty = new float[]{2.5f};
            this.levelMaxDifficulty = new float[]{3.5f};
        }

    }

    @Override
    public void dispose() {
        if (this.assets != null) {
            this.assets.dispose();
        }
        if (this.eventManager != null) {
            this.eventManager.dispose();
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

}
