package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCampaignMode implements PixelLogicLevelMode, PixelLogicListener {

    private List<PixelLogicLevel> levels;

    private PixelLogicLevel level;

    private Preferences preferences;

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    @Override
    public void setup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.loadLevels();
        this.eventManager.listen(this);
        this.preferences = Gdx.app.getPreferences("campaign_preferences");
    }

    public void run() {
        String levelName = this.preferences.getString("levelName");
        if (levelName != null) {
            PixelLogicLevel level = findLevel(levelName);
            if (level != null) {
                String pixels = this.preferences.getString("pixels");
                if (pixels != null) {
                    level.ofPixelString(pixels);
                }
                runLevel(level);
                return;
            }
        }
        runLevel(this.levels.get(0));
    }

    private void loadNextLevel() {
        PixelLogicLevel level = next();
        if (level == null) {
            // TODO handle no more level's
            return;
        }
        this.level = level;
        this.preferences.putString("levelName", this.level.getName());
        this.preferences.remove("pixels");
        this.preferences.flush();
        runLevel(level);
    }

    private void runLevel(PixelLogicLevel level) {
        this.level = level;
        this.eventManager.fire(new PixelLogicNextLevelEvent(this, level));
    }

    private PixelLogicLevel findLevel(String name) {
        for (PixelLogicLevel level : this.levels) {
            if (level.getName().equals(name)) {
                return level;
            }
        }
        return null;
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                loadNextLevel();
            }
        } else if (event instanceof PixelLogicUserEvent) {
            PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
            if (PixelLogicUserEvent.Type.BOARD_CHANGED.equals(userEvent.getType())) {
                this.preferences.putString("pixels", this.level.toPixelString());
                this.preferences.flush();
            }
        }
    }

    @Override
    public PixelLogicLevel next() {
        int currentLevelIndex = this.levels.indexOf(this.level);
        if (currentLevelIndex == -1) {
            return this.levels.get(0);
        }
        int nextLevelIndex = currentLevelIndex + 1;
        if (nextLevelIndex >= this.levels.size()) {
            for (PixelLogicLevel level : this.levels) {
                level.reset();
            }
            return this.levels.get(0);
        }
        return this.levels.get(nextLevelIndex);
    }

    private void loadLevels() {
        levels = new ArrayList<PixelLogicLevel>();

        // load tutorial level's
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("3x3")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("4x4")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("5x5")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("6x6")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("7x7")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("8x8")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("9x9")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("10x10")));
        // levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("11x11")));
        levels.addAll(PixelLogicLevelLoader.load(assets.getLevelCollection("12x12")));
    }

}
