package de.mewel.pixellogic.mode;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.PixelLogicUIBoard;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.misc.PixelLogicUIPicture;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicPictureMode extends PixelLogicListLevelMode {

    private PixelLogicLevelCollection collection;

    private Group pageRoot;

    private PixelLogicUIPicture picture;

    public PixelLogicPictureMode(PixelLogicLevelCollection collection) {
        super();
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
        data.put("pageId", PixelLogicUIPageId.picture);
        this.getEventManager().fire(new PixelLogicUIPageChangeEvent(this, data));
    }

    @Override
    protected List<PixelLogicLevel> loadLevels() {
        List<PixelLogicLevel> levels = PixelLogicLevelLoader.load(getCollection());
        return new ArrayList<PixelLogicLevel>(levels);
    }

    @Override
    public void deactivate() {
        super.deactivate();
        destroyPicture();
    }

    public PixelLogicLevelCollection getCollection() {
        return this.collection;
    }

    public boolean isSolved() {
        return getPreferences().getBoolean("finished", false);
    }

    /**
     * Returns a number between -1 and the amount of sprites for this picture to solve. Minus one
     * means that nothing is solved yet.
     *
     * @return a number between -1 and the amount of sprite for this picture
     */
    public int getSolveProgress() {
        return getPreferences().getInteger("solvedIndex", -1);
    }

    public int incrementAndGetSolveProgress() {
        int solvedIndex = getSolveProgress() + 1;
        getPreferences().putInteger("solvedIndex", solvedIndex);
        getPreferences().flush();
        return solvedIndex;
    }

    public int getLevelIndex() {
        //String levelName = getPreferences().getString(getLastPlayedLevelProperty(), null);
        //Integer levelIndex = levelName != null ? Integer.valueOf(levelName.substring(1)) - 1 : null;
        //return levelIndex != null ? collection.getOrder().get(levelIndex) : -1;
        int solveProgress = getSolveProgress();
        return solveProgress == -1 ? -1 : collection.getOrder().get(solveProgress);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.solved.equals(changeEvent.getStatus())) {
                handleLevelSolved();
            } else if (PixelLogicLevelStatus.beforeDestroyed.equals(changeEvent.getStatus())) {
                destroyPicture();
            }
        }
    }

    private void handleLevelSolved() {
        PixelLogicUILevelPage page = (PixelLogicUILevelPage) getAppScreen().getCurrentPage();
        PixelLogicUILevel level = page.getLevelUI();
        final PixelLogicUIBoard board = level.getBoard();

        PixelLogicLevelCollection collection = page.getProperties().get("pictureCollection");

        this.picture = new PixelLogicUIPicture(page.getAssets(), page.getEventManager(), collection);
        this.pageRoot = page.getRoot();

        int toolbarHeight = PixelLogicUIUtil.getToolbarHeight();
        float size = Math.min(page.getWidth(), page.getHeight() - toolbarHeight);
        this.picture.setSize(size, size);
        this.picture.setPosition(page.getWidth() / 2 - size / 2, page.getHeight() / 2 - size / 2 + toolbarHeight / 2);
        this.picture.getColor().a = 0f;
        this.pageRoot.addActorBefore(level, picture);

        //this.solvedIndex = getSolveProgress();
        //int levelIndex = solvedIndex == -1 ? -1 : collection.getOrder().get(solvedIndex);
        //this.background.update(levelIndex);

        int solveProgress = incrementAndGetSolveProgress();
        final int oldLevelIndex = solveProgress == 0 ? -1 : collection.getOrder().get(solveProgress - 1);
        final int levelIndex = getLevelIndex();

        this.picture.update(oldLevelIndex);
        final float delay = .5f;
        picture.addAction(new SequenceAction(
                Actions.delay(delay),
                Actions.fadeIn(.5f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        picture.update(board, levelIndex, oldLevelIndex, delay + .2f);
                    }
                })));
    }

    private void destroyPicture() {
        if (pageRoot != null && picture != null) {
            picture.addAction(new SequenceAction(Actions.fadeOut(.2f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    pageRoot.removeActor(picture);
                }
            })));
        }
    }

}