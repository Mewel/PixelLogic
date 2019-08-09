package de.mewel.pixellogic.mode;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicCharactersFinishedEvent;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicCharactersMode extends PixelLogicListLevelMode {

    @Override
    public String getName() {
        return "characters";
    }

    @Override
    protected void onFinished() {
        super.onFinished();
        // back to main menu
        this.getAppScreen().setPage(PixelLogicUIPageId.characters);
    }

    @Override
    protected void onSetupDone() {
        super.onSetupDone();
        checkAllSolvedAchievement();
    }

    private void checkAllSolvedAchievement() {
        boolean allSolved = true;
        for (PixelLogicLevel level : getLevels()) {
            if (!isLevelSolved(level)) {
                allSolved = false;
                break;
            }
        }
        if (allSolved) {
            getEventManager().fire(new PixelLogicCharactersFinishedEvent(this));
        }
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> characters = PixelLogicLevelLoader.load(getCollection());
        return new ArrayList<PixelLogicLevel>(characters);
    }

    public PixelLogicLevelCollection getCollection() {
        return getAssets().getLevelCollection("characters");
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.solved.equals(changeEvent.getStatus())) {
                getPreferences().putBoolean(getSolvedProperty(getLevel()), true).flush();
                checkAllSolvedAchievement();
            }
        }
    }

    protected String getSolvedProperty(PixelLogicLevel level) {
        return "solved_" + level.toSimpleName();
    }

    public boolean isLevelSolved(PixelLogicLevel level) {
        return getPreferences().getBoolean(getSolvedProperty(level));
    }

}
