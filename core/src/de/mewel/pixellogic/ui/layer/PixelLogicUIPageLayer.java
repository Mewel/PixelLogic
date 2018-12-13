package de.mewel.pixellogic.ui.layer;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangedEvent;

public class PixelLogicUIPageLayer implements PixelLogicUILayer {

    private Map<PixelLogicUIPageId, PixelLogicUIPage> pageMap;

    private PixelLogicUIPage activePage;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    public PixelLogicUIPageLayer(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.pageMap = new HashMap<PixelLogicUIPageId, PixelLogicUIPage>();
        this.activePage = null;
    }

    public void add(PixelLogicUIPageId id, PixelLogicUIPage screen) {
        this.pageMap.put(id, screen);
    }

    public PixelLogicUIPage get(PixelLogicUIPageId id) {
        return this.pageMap.get(id);
    }

    public boolean isActive(PixelLogicUIPageId id) {
        return this.activePage != null && activePage.getPageId().equals(id);
    }

    public void activate(final PixelLogicUIPage page, final PixelLogicUIPageProperties data) {
        Gdx.app.log("layer activate", "current page " + this.activePage);
        if (this.activePage != null) {
            this.activePage.deactivate(new Runnable() {
                @Override
                public void run() {
                    activate(page, activePage, data);
                }
            });
            return;
        }
        activate(page, null, data);
    }

    protected void activate(final PixelLogicUIPage newPage, final PixelLogicUIPage oldPage, final PixelLogicUIPageProperties data) {
        data.put("pageId", newPage.getPageId());
        if (oldPage != null) {
            data.put("oldPageId", oldPage.getPageId());
        }
        getEventManager().fire(new PixelLogicUIPageChangedEvent(this, data));

        activePage = newPage;
        activePage.activate(data);
        activePage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }

    @Override
    public void render(float delta) {
        if (this.activePage != null) {
            this.activePage.render(delta);
        }
    }

    @Override
    public void resize(int width, int height) {
        if (this.activePage != null) {
            this.activePage.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        for (PixelLogicUIPage page : pageMap.values()) {
            page.dispose();
        }
    }

    @Override
    public void pause() {
        if (this.activePage != null) {
            this.activePage.pause();
        }
    }

    @Override
    public void resume() {
        if (this.activePage != null) {
            this.activePage.resume();
        }
    }

    @Override
    public PixelLogicAssets getAssets() {
        return this.assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    public PixelLogicUIPage getActivePage() {
        return activePage;
    }

}
