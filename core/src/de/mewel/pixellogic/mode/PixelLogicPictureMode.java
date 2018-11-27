package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicPictureMode extends PixelLogicListLevelMode {

    private PixelLogicLevelCollection collection;

    public PixelLogicPictureMode(PixelLogicLevelCollection collection) {
        this.collection = collection;
    }

    @Override
    public String getName() {
        return "picture_" + collection.getId();
    }

    @Override
    protected void onFinished() {
        super.onFinished();
        // back to main menu
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", PixelLogicUIPageId.mainMenu);
        this.getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> levels = PixelLogicLevelLoader.load(getCollection());
        return new ArrayList<PixelLogicLevel>(levels);
    }

    @Override
    public Preferences getPreferences() {
        return super.getPreferences();
    }

    @Override
    public void reset() {
        getPreferences().remove("solvedIndex");
        super.reset();
    }

    public PixelLogicLevelCollection getCollection() {
        return this.collection;
    }

}
