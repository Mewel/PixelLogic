package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

public abstract class PixelLogicListLevelMode extends PixelLogicLevelMode {

    private List<PixelLogicLevel> levels;

    private Preferences preferences;

    @Override
    public void setup(PixelLogicGlobal global) {
        super.setup(global);
        if (!this.setupDone) {
            this.levels = this.loadLevels();
            this.preferences = Gdx.app.getPreferences(getName());
            this.setupDone = true;
        }
    }

    public void run() {
        String levelName = this.preferences.getString(getLastPlayedLevelProperty());
        if (levelName != null) {
            PixelLogicLevel level = findLevel(levelName);
            if (level != null) {
                run(level);
                return;
            }
        }
        loadNextLevel();
    }

    public void run(PixelLogicLevel level) {
        String pixels = this.preferences.getString(getPixelsProperty(level));
        level.ofPixelString(pixels);
        runLevel(level);
    }

    protected void loadNextLevel() {
        PixelLogicLevel level = next();
        if (level == null) {
            onFinished();
            return;
        }
        storeLastPlayedLevel(level);
        runLevel(level);
    }

    protected void storeLastPlayedLevel(PixelLogicLevel level) {
        this.preferences.putString(getLastPlayedLevelProperty(), level.getName());
        this.preferences.remove(getPixelsProperty(level));
        this.preferences.flush();
    }

    protected void onFinished() {
        this.preferences.putBoolean("finished", true);
        this.preferences.putBoolean("replay", false);
        this.preferences.flush();
        this.reset();
    }

    protected PixelLogicLevel next() {
        int currentLevelIndex = this.levels.indexOf(this.level);
        if (currentLevelIndex == -1) {
            return this.levels.get(0);
        }
        int nextLevelIndex = currentLevelIndex + 1;
        if (nextLevelIndex >= this.levels.size()) {
            for (PixelLogicLevel level : this.levels) {
                level.reset();
            }
            return null;
        }
        return this.levels.get(nextLevelIndex);
    }

    public void reset() {
        this.preferences.remove(getLastPlayedLevelProperty());
        for (String key : this.preferences.get().keySet()) {
            if (key.startsWith("pixels_")) {
                this.preferences.remove(key);
            }
        }
        this.preferences.flush();
    }

    public PixelLogicLevel findLevel(String name) {
        for (PixelLogicLevel level : this.levels) {
            if (level.getName().equals(name)) {
                return level;
            }
        }
        return null;
    }

    public List<PixelLogicLevel> getLevels() {
        return levels;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    protected String getLastPlayedLevelProperty() {
        return "lastPlayedLevel";
    }

    protected String getPixelsProperty(PixelLogicLevel level) {
        return "pixels_" + level.toSimpleName();
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.destroyed.equals(changeEvent.getStatus())) {
                loadNextLevel();
            } else if (PixelLogicLevelStatus.solved.equals(changeEvent.getStatus())) {
                this.preferences.remove(getPixelsProperty(level));
                this.preferences.flush();
            }
        } else if (event instanceof PixelLogicBoardChangedEvent) {
            PixelLogicBoardChangedEvent changedBoardEvent = (PixelLogicBoardChangedEvent) event;
            this.preferences.putString(getPixelsProperty(level), changedBoardEvent.getLevel().toPixelString());
            this.preferences.flush();
        } else if (event instanceof PixelLogicUIPageChangeEvent) {
            PixelLogicUIPageChangeEvent screenChangeEvent = (PixelLogicUIPageChangeEvent) event;
            if (!screenChangeEvent.getPageId().equals(PixelLogicUIPageId.level)) {
                this.deactivate();
            }
        }
    }

    public abstract String getName();

    protected abstract List<PixelLogicLevel> loadLevels();

}
