package de.mewel.pixellogic.spike;

import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.achievements.PixelLogicAchievements;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class TimeTrialLevelTest extends AbstractSpikeGame {

    @Override
    public void create() {
        this.eventManager = new PixelLogicEventManager();

        this.assets = new PixelLogicAssets();
        this.assets.load();

        this.achievements = new PixelLogicAchievements(assets, eventManager);
        this.audio = new PixelLogicAudio(assets, eventManager);

        this.appScreen = new PixelLogicUIAppScreen(this);
        this.setScreen(this.appScreen);

        final PixelLogicTimeTrialMode mode = new PixelLogicTimeTrialMode(new TestOptions());
        mode.setup(this);
        mode.activate();
        mode.run();

        eventManager.listen(new PixelLogicListener() {
            @Override
            public void handle(PixelLogicEvent event) {
                if (event instanceof PixelLogicNextLevelEvent) {
                    Gdx.app.log("lvl", ((PixelLogicNextLevelEvent) event).getNextLevel().toString());
                }
            }
        });
    }

    public static final class TestOptions extends PixelLogicTimeTrialModeOptions {

        public TestOptions() {
            this.id = Mode.time_trial_hardcore;
            this.name = "HARD MODE";
            this.levelSize = new int[]{10};
            this.levelSizeOffset = new int[]{0};
            this.levelMinDifficulty = new float[]{5.9f};
            this.levelMaxDifficulty = new float[]{15f};
        }

    }

}
