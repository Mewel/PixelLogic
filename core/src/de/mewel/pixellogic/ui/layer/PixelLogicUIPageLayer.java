package de.mewel.pixellogic.ui.layer;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.page.PixelLogicUIPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUIPageLayer implements PixelLogicUILayer {

    private final Map<PixelLogicUIPageId, PixelLogicUIPage> pageMap;

    private PixelLogicUIPage activePage;

    private final PixelLogicGlobal global;

    public PixelLogicUIPageLayer(PixelLogicGlobal global) {
        this.global = global;
        this.pageMap = new HashMap<>();
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

    public void activate(final PixelLogicUIPage page, final PixelLogicUIPageProperties data, final Runnable after) {
        Gdx.app.log("layer activate", "current page " + this.activePage);

        // fire before activation, not sure if thats good but otherwise the back button does not
        // work cause it goes to the next lvl because the destroy event is fired before the
        // change page event
        data.put("pageId", page.getPageId());
        if (activePage != null) {
            data.put("oldPageId", activePage.getPageId());
        }
        getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));

        // activate page
        if (this.activePage != null) {
            this.activePage.deactivate(() -> activate(page, activePage, data, after));
            return;
        }
        activate(page, null, data, after);
    }

    protected void activate(final PixelLogicUIPage newPage, final PixelLogicUIPage oldPage,
                            final PixelLogicUIPageProperties data, final Runnable after) {
        activePage = newPage;
        activePage.activate(data);
        activePage.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (after != null) {
            after.run();
        }
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        for (PixelLogicUIPage page : pageMap.values()) {
            page.styleChanged(style);
        }
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
        return global.getAssets();
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return global.getEventManager();
    }

    @Override
    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

    public PixelLogicUIPage getActivePage() {
        return activePage;
    }
}
