package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.Scaling;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicLoadNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIMessageModal;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelMenu;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelResolution;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelToolbar;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIBoardSolvedAnimation;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUILevelAnimation;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIPictureBoardSolvedAnimation;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;
import de.mewel.pixellogic.util.PixelLogicMusic;

import static de.mewel.pixellogic.PixelLogicConstants.PUZZLE_SOLVED_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.PUZZLE_SOLVED_SOUND_VOLUME;

public class PixelLogicUILevelPage extends PixelLogicUIPage {

    protected static final Map<String, Class<? extends PixelLogicUILevelAnimation>> SOLVED_ANIMATION_MAP;

    protected PixelLogicUILevel levelUI;

    protected PixelLogicUILevelToolbar toolbar;

    protected PixelLogicUILevelMenu menu;

    protected PixelLogicLevelStatus levelStatus;

    protected ScreenListener screenListener;

    protected PixelLogicUIMessageModal loadingModal;

    protected PixelLogicUILevelAnimation solvedAnimation;

    protected Image backgroundImage;

    static {
        SOLVED_ANIMATION_MAP = new HashMap<>();
        SOLVED_ANIMATION_MAP.put("default", PixelLogicUIBoardSolvedAnimation.class);
        SOLVED_ANIMATION_MAP.put("picture", PixelLogicUIPictureBoardSolvedAnimation.class);
    }

    public PixelLogicUILevelPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.level);

        // LEVEL
        this.levelUI = null;
        this.toolbar = new PixelLogicUILevelToolbar(global);

        // MODAL's
        this.menu = createMenu();
        this.loadingModal = new PixelLogicUIMessageModal("loading next level...", getGlobal(), getStage());

        // STAGE
        this.screenListener = createScreenListener();
    }

    protected PixelLogicUILevelMenu createMenu() {
        return new PixelLogicUILevelMenu(getGlobal(), this);
    }

    protected ScreenListener createScreenListener() {
        return new ScreenListener(this);
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        Gdx.app.log("lvl screen", "activate");
        super.activate(properties);
        getEventManager().listen(this.screenListener);
        getStage().addListener(this.screenListener);
        this.levelStatus = null;
        this.updateBackgroundImage();
        this.menu.activate(properties);
        getStage().addAction(Actions.fadeIn(.5f));
    }

    @Override
    public void deactivate(final Runnable after) {
        getEventManager().remove(this.screenListener);
        getStage().removeListener(this.screenListener);
        if (!PixelLogicLevelStatus.DESTROYED.equals(this.levelStatus)) {
            this.destroyLevel();
        }
        this.menu.deactivate();
        super.deactivate(() -> {
            Action action = Actions.sequence(Actions.fadeOut(.5f), Actions.run(after));
            getStage().addAction(action);
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.updateBounds(width, height);
        this.updateBackgroundImage();
    }

    public void loadLevel(PixelLogicLevel level) {
        // toolbar
        getStage().addActor(this.toolbar);
        this.toolbar.getColor().a = .0f;
        this.toolbar.addAction(Actions.fadeIn(.4f));

        // level
        this.levelUI = new PixelLogicUILevel(getGlobal());
        this.levelUI.getColor().a = .0f;
        this.levelUI.load(level);
        this.getStage().addActor(this.levelUI);
        changeLevelStatus(PixelLogicLevelStatus.LOADED, () -> updateBounds((int) getWidth(), (int) getHeight()));
        Action fadeInAction = Actions.sequence(Actions.fadeIn(.4f), Actions.run(() -> changeLevelStatus(PixelLogicLevelStatus.PLAYABLE)));
        this.levelUI.addAction(fadeInAction);
    }

    public void resetLevel() {
        this.levelUI.resetLevel();
    }

    public void destroyLevel() {
        changeLevelStatus(PixelLogicLevelStatus.BEFORE_DESTROYED);

        // destroy animation
        if (this.solvedAnimation != null) {
            this.solvedAnimation.destroy();
            this.solvedAnimation = null;
        }

        // toolbar
        Action fadeOutToolbarAction = Actions.sequence(Actions.fadeOut(.4f), Actions.run(() -> {
            if (toolbar == null) {
                return;
            }
            toolbar.remove();
        }));
        this.toolbar.addAction(fadeOutToolbarAction);

        // level
        Action fadeOutLevelAction = Actions.sequence(Actions.fadeOut(.4f), Actions.run(() -> {
            if (levelUI != null) {
                levelUI.clear();
                levelUI.remove();
                changeLevelStatus(PixelLogicLevelStatus.DESTROYED);
            }
        }));
        this.levelUI.addAction(fadeOutLevelAction);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        if (PixelLogicLevelStatus.PLAYABLE.equals(this.levelStatus) && this.levelUI.isSolved()) {
            this.levelStatus = PixelLogicLevelStatus.FINISHED;
            Action delay = Actions.sequence(Actions.delay(.1f), Actions.run(this::onSolved));
            getStage().addAction(delay);
        }
    }

    protected void onSolved() {
        changeLevelStatus(PixelLogicLevelStatus.SOLVED);
        this.solvedAnimation = showSolvedAnimation();
        final PixelLogicMusic levelMusic = getAudio().getLevelMusic();
        levelMusic.fadeTo(.5f, .05f, () -> getAudio().playSound(PUZZLE_SOLVED_SOUND, PUZZLE_SOLVED_SOUND_VOLUME));
        this.getStage().addAction(Actions.sequence(
                Actions.delay(.3f),
                Actions.run(() -> changeLevelStatus(PixelLogicLevelStatus.FINISHED)
                ),
                Actions.delay(4f),
                Actions.run(() -> levelMusic.fadeIn(.5f)
                )));
    }

    public PixelLogicUILevelAnimation showSolvedAnimation() {
        String animationKey = getProperties().getString("solvedAnimation", "default");
        Class<? extends PixelLogicUILevelAnimation> animationClass = SOLVED_ANIMATION_MAP.get(animationKey);
        PixelLogicUILevelAnimation animation;
        try {
            Constructor<? extends PixelLogicUILevelAnimation> constructor = animationClass.getConstructor(PixelLogicGlobal.class);
            animation = constructor.newInstance(getGlobal());
        } catch (Exception e) {
            throw new GdxRuntimeException("Unable to instantiate animation class " + animationClass.getName(), e);
        }
        animation.setPage(this);
        animation.execute();
        return animation;
    }

    protected void changeLevelStatus(PixelLogicLevelStatus status) {
        this.changeLevelStatus(status, null);
    }

    protected void changeLevelStatus(PixelLogicLevelStatus status, Runnable after) {
        this.levelStatus = status;
        getEventManager().fire(new PixelLogicLevelStatusChangeEvent(this, getLevel(), getLevelStatus()), after);
    }

    public PixelLogicLevel getLevel() {
        return this.levelUI.getLevel();
    }

    public PixelLogicUILevel getLevelUI() {
        return this.levelUI;
    }

    public PixelLogicUILevelMenu getMenu() {
        return menu;
    }

    public PixelLogicUILevelToolbar getToolbar() {
        return toolbar;
    }

    public PixelLogicLevelStatus getLevelStatus() {
        return this.levelStatus;
    }

    public ScreenListener getScreenListener() {
        return screenListener;
    }

    @Override
    public void dispose() {
        super.dispose();
        getEventManager().remove(this.screenListener);
    }

    protected void updateBounds(int width, int height) {
        // root
        this.getRoot().setSize(width, height);

        // toolbar
        if (toolbar != null) {
            updateToolbarBounds(width);
        }

        // level
        if (this.levelUI != null) {
            updateLevelBounds(width, height);
        }
        // modal's
        if (menu != null && loadingModal != null) {
            updateModalBounds(width, height);
        }
    }

    protected void updateModalBounds(int width, int height) {
        this.menu.setBounds(0, 0, width, height);
        this.loadingModal.setBounds(0, 0, width, height);
    }

    protected void updateLevelBounds(int width, int height) {
        int levelMaxHeight = Math.min(PixelLogicUIUtil.getLevelMaxHeight(), height);
        PixelLogicUILevelResolution resolution = new PixelLogicUILevelResolution(getLevel(), width, levelMaxHeight);

        Vector2 levelSize = resolution.getLevelSize();
        this.levelUI.setSize(levelSize.x, levelSize.y);
        this.levelUI.updateLevelResolution(resolution);
        int toolbarHeightAndPadding = PixelLogicUIUtil.getToolbarHeight() + PixelLogicUIUtil.getToolbarPaddingTop();
        float x = width / 2f - this.levelUI.getWidth() / 2f;
        float y = levelMaxHeight / 2f - this.levelUI.getHeight() / 2f + toolbarHeightAndPadding;
        this.levelUI.setPosition((int) x, (int) y);
    }

    protected void updateToolbarBounds(int width) {
        int toolbarHeight = PixelLogicUIUtil.getToolbarHeight();
        this.toolbar.setBounds(0, 0, width, toolbarHeight);
    }

    private void updateBackgroundImage() {
        Gdx.app.log("screen", "" + this.getRoot().getWidth());
        if (this.backgroundImage != null) {
            this.backgroundImage.remove();
        }
        this.backgroundImage = new Image(getGlobal().getStyle().getLevelBackgroundTexture(getAssets()));
        this.backgroundImage.setFillParent(true);
        this.backgroundImage.setScaling(Scaling.fill);
        this.backgroundImage.setPosition(this.backgroundImage.getImageWidth(), 0);
        this.getRoot().addActorAt(0, this.backgroundImage);
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        updateBackgroundImage();
    }

    private boolean isAutoBlockEnabled() {
        return Gdx.app.getPreferences("pixellogic_settings").getBoolean("autoBlock", false);
    }

    protected static class ScreenListener extends InputListener implements PixelLogicListener {

        private final PixelLogicUILevelPage page;

        ScreenListener(PixelLogicUILevelPage page) {
            this.page = page;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (PixelLogicLevelStatus.FINISHED.equals(this.page.getLevelStatus())) {
                this.page.destroyLevel();
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicUserEvent) {
                PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
                if (PixelLogicUserEvent.Type.LEVEL_MENU_CLICKED.equals(userEvent.getType()) &&
                        PixelLogicLevelStatus.PLAYABLE.equals(page.getLevelStatus())) {
                    page.menu.show();
                } else if (PixelLogicUserEvent.Type.BACK_BUTTON_CLICKED.equals(userEvent.getType())) {
                    if (page.menu.isShown()) {
                        page.menu.close();
                    } else if (PixelLogicLevelStatus.PLAYABLE.equals(page.getLevelStatus())) {
                        page.menu.show();
                    }
                }
            } else if (event instanceof PixelLogicLoadNextLevelEvent) {
                Gdx.app.log("screen", "next level");
                page.loadingModal.show();
            } else if (event instanceof PixelLogicNextLevelEvent) {
                page.loadingModal.close();
                PixelLogicNextLevelEvent nextLevelEvent = (PixelLogicNextLevelEvent) event;
                PixelLogicLevel nextLevel = nextLevelEvent.getNextLevel();
                page.loadLevel(nextLevel);
            } else if (event instanceof PixelLogicBoardChangedEvent) {
                handleAutoBlock((PixelLogicBoardChangedEvent) event);
            }
        }

        protected void handleAutoBlock(PixelLogicBoardChangedEvent event) {
            if (page.isAutoBlockEnabled() && event.getValue() != null && event.getValue()) {
                PixelLogicLevel level = event.getLevel();
                int row = event.getRow();
                int column = event.getCol();
                if (level.isRowComplete(row)) {
                    for (int colIndex = 0; colIndex < level.getColumns(); colIndex++) {
                        if (level.isEmpty(row, colIndex)) {
                            page.levelUI.setPixel(row, colIndex, false);
                        }
                    }
                }
                if (level.isColumnComplete(column)) {
                    for (int rowIndex = 0; rowIndex < level.getRows(); rowIndex++) {
                        if (level.isEmpty(rowIndex, column)) {
                            page.levelUI.setPixel(rowIndex, column, false);
                        }
                    }
                }
            }
        }
    }

}
