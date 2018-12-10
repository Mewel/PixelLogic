package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import java.util.HashMap;
import java.util.Map;

import de.mewel.pixellogic.PixelLogicConstants;
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
import de.mewel.pixellogic.ui.level.PixelLogicUILevelToolbar;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIBoardSolvedAnimation;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUILevelAnimation;
import de.mewel.pixellogic.ui.level.animation.PixelLogicUIPictureBoardSolvedAnimation;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUILevelPage extends PixelLogicUIPage {

    private static Map<String, Class<? extends PixelLogicUILevelAnimation>> SOLVED_ANIMATION_MAP;

    private PixelLogicUILevel levelUI;

    private PixelLogicUILevelToolbar toolbar;

    private PixelLogicUILevelMenu menu;

    private Texture backgroundTexture;

    private Image backgroundImage;

    private PixelLogicLevelStatus levelStatus;

    private ScreenListener screenListener;

    private PixelLogicUIMessageModal loadingModal;

    private PixelLogicUILevelAnimation solvedAnimation;

    private FPSLogger fpsLogger = new FPSLogger();

    static {
        SOLVED_ANIMATION_MAP = new HashMap<String, Class<? extends PixelLogicUILevelAnimation>>();
        SOLVED_ANIMATION_MAP.put("default", PixelLogicUIBoardSolvedAnimation.class);
        SOLVED_ANIMATION_MAP.put("picture", PixelLogicUIPictureBoardSolvedAnimation.class);
    }

    public PixelLogicUILevelPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.level);

        // BACKGROUND
        this.backgroundTexture = new Texture(Gdx.files.internal("background/level_1.jpg"));
        this.backgroundImage = null;

        // LEVEL
        this.levelUI = null;
        this.toolbar = new PixelLogicUILevelToolbar(getAssets(), getEventManager());

        // MODAL's
        this.menu = new PixelLogicUILevelMenu(getAssets(), getEventManager(), this);
        this.loadingModal = new PixelLogicUIMessageModal("loading next level...", getAssets(), getEventManager(), getStage());

        // STAGE
        this.screenListener = new ScreenListener(this);
        getStage().addListener(this.screenListener);
        getEventManager().listen(this.screenListener);
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        Gdx.app.log("lvl screen", "activate");
        super.activate(properties);
        this.levelStatus = null;
        this.updateBackgroundImage();
        this.menu.activate(properties);
        getStage().addAction(Actions.fadeIn(.5f));
    }

    @Override
    public void deactivate(final Runnable after) {
        if (!PixelLogicLevelStatus.destroyed.equals(this.levelStatus)) {
            this.destroyLevel();
        }
        this.menu.deactivate();
        super.deactivate(new Runnable() {
            @Override
            public void run() {
                Action action = Actions.sequence(Actions.fadeOut(.5f), Actions.run(after));
                getStage().addAction(action);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.updateBounds();
        this.updateBackgroundImage();
    }

    public void loadLevel(PixelLogicLevel level) {
        // toolbar
        getStage().addActor(this.toolbar);
        this.toolbar.getColor().a = .0f;
        this.toolbar.addAction(Actions.fadeIn(.4f));

        // level
        this.levelUI = new PixelLogicUILevel(getAssets(), getEventManager());
        this.levelUI.getColor().a = .0f;
        this.levelUI.load(level);
        this.getStage().addActor(this.levelUI);
        this.updateBounds();
        changeLevelStatus(PixelLogicLevelStatus.loaded);
        Action fadeInAction = Actions.sequence(Actions.fadeIn(.4f), Actions.run(new Runnable() {
            @Override
            public void run() {
                changeLevelStatus(PixelLogicLevelStatus.playable);
            }
        }));
        this.levelUI.addAction(fadeInAction);
    }

    public void resetLevel() {
        this.levelUI.resetLevel();
    }

    public void destroyLevel() {
        changeLevelStatus(PixelLogicLevelStatus.beforeDestroyed);

        // destroy animation
        if (this.solvedAnimation != null) {
            this.solvedAnimation.destroy();
            this.solvedAnimation = null;
        }

        // toolbar
        Action fadeOutToolbarAction = Actions.sequence(Actions.fadeOut(.4f), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (toolbar == null) {
                    return;
                }
                toolbar.remove();
            }
        }));
        this.toolbar.addAction(fadeOutToolbarAction);

        // level
        Action fadeOutLevelAction = Actions.sequence(Actions.fadeOut(.4f), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (levelUI != null) {
                    levelUI.clear();
                    levelUI.remove();
                    changeLevelStatus(PixelLogicLevelStatus.destroyed);
                }
            }
        }));
        this.levelUI.addAction(fadeOutLevelAction);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        if (PixelLogicLevelStatus.playable.equals(this.levelStatus) && this.levelUI.isSolved()) {
            this.levelStatus = PixelLogicLevelStatus.finished;
            Action delay = Actions.sequence(Actions.delay(.1f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    onSolved();
                }
            }));
            getStage().addAction(delay);
        }

        // fpsLogger.log();
    }

    private void onSolved() {
        changeLevelStatus(PixelLogicLevelStatus.solved);
        this.solvedAnimation = showSolvedAnimation();
        getAssets().get().get(PixelLogicConstants.PUZZLE_SOLVED_SOUND, Sound.class).play(.2f);
        this.getStage().addAction(Actions.sequence(
                Actions.delay(.3f),
                Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    changeLevelStatus(PixelLogicLevelStatus.finished);
                                }
                            }
                )));
    }

    private PixelLogicUILevelAnimation showSolvedAnimation() {
        String animationKey = getProperties().getString("solvedAnimation", "default");
        Class<? extends PixelLogicUILevelAnimation> animationClass = SOLVED_ANIMATION_MAP.get(animationKey);
        PixelLogicUILevelAnimation animation;
        try {
            animation = animationClass.newInstance();
        } catch (Exception e) {
            animation = new PixelLogicUIBoardSolvedAnimation();
        }
        animation.setPage(this);
        animation.execute();
        return animation;
    }

    private void changeLevelStatus(PixelLogicLevelStatus status) {
        this.levelStatus = status;
        getEventManager().fire(new PixelLogicLevelStatusChangeEvent(this, getLevel(), getLevelStatus()));
    }

    public PixelLogicLevel getLevel() {
        return this.levelUI.getLevel();
    }

    public PixelLogicUILevel getLevelUI() {
        return this.levelUI;
    }

    public PixelLogicUILevelToolbar getToolbar() {
        return toolbar;
    }

    public PixelLogicLevelStatus getLevelStatus() {
        return this.levelStatus;
    }

    @Override
    public void dispose() {
        super.dispose();
        getEventManager().remove(this.screenListener);
        this.backgroundTexture.dispose();
    }

    private void updateBounds() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // root
        this.getRoot().setSize(screenWidth, screenHeight);

        // toolbar
        int toolbarHeight = PixelLogicUIUtil.getToolbarHeight();
        int toolbarHeightAndPadding = toolbarHeight + PixelLogicUIUtil.getToolbarPaddingTop();
        this.toolbar.setBounds(0, 0, screenWidth, toolbarHeight);

        // level
        if (this.levelUI != null) {
            int levelMaxHeight = PixelLogicUIUtil.getLevelMaxHeight();
            this.levelUI.resize();
            float x = screenWidth / 2f - this.levelUI.getWidth() / 2f;
            float y = levelMaxHeight / 2f - this.levelUI.getHeight() / 2f + toolbarHeightAndPadding;
            this.levelUI.setPosition((int) x, (int) y);
        }

        // modal's
        this.menu.setBounds(0, 0, screenWidth, screenHeight);
        this.loadingModal.setBounds(0, 0, screenWidth, screenHeight);
    }

    private void updateBackgroundImage() {
        Gdx.app.log("screen", "" + this.getRoot().getWidth());
        if (this.backgroundImage != null) {
            this.backgroundImage.remove();
        }
        this.backgroundImage = new Image(backgroundTexture);
        this.backgroundImage.setFillParent(true);
        this.backgroundImage.setScaling(Scaling.fill);
        this.backgroundImage.setPosition(this.backgroundImage.getImageWidth(), 0);
        //this.getRoot().addActorAt(0, this.backgroundImage);
    }

    private static class ScreenListener extends InputListener implements PixelLogicListener {

        private PixelLogicUILevelPage page;

        ScreenListener(PixelLogicUILevelPage page) {
            this.page = page;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (PixelLogicLevelStatus.finished.equals(this.page.getLevelStatus())) {
                this.page.destroyLevel();
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicUserEvent) {
                PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
                if (PixelLogicUserEvent.Type.LEVEL_MENU_CLICKED.equals(userEvent.getType()) ||
                        PixelLogicUserEvent.Type.BACK_BUTTON_CLICKED.equals(userEvent.getType())) {
                    page.menu.show();
                }
            }
            if (event instanceof PixelLogicLoadNextLevelEvent) {
                Gdx.app.log("screen", "next level");
                page.loadingModal.show();
            }
            if (event instanceof PixelLogicNextLevelEvent) {
                page.loadingModal.close();
                PixelLogicNextLevelEvent nextLevelEvent = (PixelLogicNextLevelEvent) event;
                PixelLogicLevel nextLevel = nextLevelEvent.getNextLevel();
                page.loadLevel(nextLevel);
            }
        }

    }

}
