package de.mewel.pixellogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicStartSecretLevelEvent;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUISecretLevelStartAnimation;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.screen.PixelLogicUIAppScreen;

public class PixelLogicGameController implements PixelLogicListener {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private PixelLogicUIAppScreen screen;

    public PixelLogicGameController(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUIAppScreen screen) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.screen = screen;
        this.eventManager.listen(this);
    }

    public void run() {
        this.screen.setPage(PixelLogicUIPageId.timeTrial, new PixelLogicUIPageProperties());

        /*PixelLogicCampaignMode cm = new PixelLogicCampaignMode();
        cm.setup(assets, eventManager);
        cm.run();*/
    }

    public void dispose() {
        this.eventManager.remove(this);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicStartSecretLevelEvent) {
            if (screen.isActive(PixelLogicUIPageId.level)) {
                Gdx.input.setInputProcessor(null);
                PixelLogicUILevelPage page = (PixelLogicUILevelPage) screen.getPage(PixelLogicUIPageId.level);
                new PixelLogicUISecretLevelStartAnimation(page).execute();
                page.getToolbar().addAction(Actions.fadeOut(.2f));
            }
        }
    }

}
