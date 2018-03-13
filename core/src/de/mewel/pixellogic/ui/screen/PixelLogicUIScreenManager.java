package de.mewel.pixellogic.ui.screen;

import java.util.Map;

import de.mewel.pixellogic.PixelLogicGame;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.ui.screen.event.PixelLogicStartTimeTrialModeEvent;
import de.mewel.pixellogic.ui.screen.event.PixelLogicTimeTrialFinishedEvent;

public class PixelLogicUIScreenManager implements PixelLogicListener {

    private Map<String, PixelLogicUIScreen> screenMap;

    private PixelLogicUIScreen activeScreen;

    private PixelLogicGame game;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIScreenManager(PixelLogicGame game, PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.game = game;
        this.assets = assets;
        this.eventManager = eventManager;
    }

    public void add(String id, PixelLogicUIScreen screen) {
        this.screenMap.put(id, screen);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicStartTimeTrialModeEvent) {
            PixelLogicStartTimeTrialModeEvent ttEvent = (PixelLogicStartTimeTrialModeEvent) event;
            activate(this.levelScreen, new PixelLogicUIScreenData());
            PixelLogicLevelMode mode = new PixelLogicTimeTrialMode(ttEvent.getOptions());
            mode.setup(assets, eventManager);
            mode.run();
        }
        if (event instanceof PixelLogicTimeTrialFinishedEvent) {
            PixelLogicTimeTrialFinishedEvent finishedEvent = (PixelLogicTimeTrialFinishedEvent) event;
            PixelLogicUIScreenData data = new PixelLogicUIScreenData();
            data.put("time", finishedEvent.getTime());
            activate(this.timeTrialFinishedScreen, data);
        }
    }

    public void activate(final PixelLogicUIScreen screen, final PixelLogicUIScreenData data) {
        if (this.activeScreen != null) {
            this.activeScreen.deactivate(new Runnable() {
                @Override
                public void run() {
                    activeScreen = screen;
                    activeScreen.activate(data);
                    game.setScreen(activeScreen);
                }
            });
            return;
        }
        this.activeScreen = screen;
        this.activeScreen.activate(data);
        game.setScreen(this.activeScreen);
    }

}
