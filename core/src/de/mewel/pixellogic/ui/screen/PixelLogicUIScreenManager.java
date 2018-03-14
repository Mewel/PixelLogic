package de.mewel.pixellogic.ui.screen;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicGame;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.screen.event.PixelLogicScreenChangeEvent;

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
        this.screenMap = new HashMap<String, PixelLogicUIScreen>();
        this.activeScreen = null;
        this.eventManager.listen(this);
    }

    public void add(String id, PixelLogicUIScreen screen) {
        this.screenMap.put(id, screen);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicScreenChangeEvent) {
            PixelLogicScreenChangeEvent screenChangeEvent = (PixelLogicScreenChangeEvent) event;
            String screenId = screenChangeEvent.getScreenId();
            PixelLogicUIScreen screen = this.screenMap.get(screenId);
            if(screen == null) {
                throw new RuntimeException("Unable to find screen " + screenId);
            }
            activate(screen, screenChangeEvent.getData());
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

    public void dispose() {
        this.eventManager.remove(this);
    }

}
