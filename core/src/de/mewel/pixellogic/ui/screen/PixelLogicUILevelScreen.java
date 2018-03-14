package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicNextLevelEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelMenu;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelToolbar;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUILevelScreen extends PixelLogicUIScreen {

    private PixelLogicUILevel levelUI;

    private PixelLogicUILevelToolbar toolbar;

    private PixelLogicUILevelMenu menu;

    private Texture backgroundTexture;

    private Image backgroundImage;

    private PixelLogicLevelStatus levelStatus;

    private ScreenListener screenListener;

    private FPSLogger fpsLogger = new FPSLogger();

    public PixelLogicUILevelScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        // BACKGROUND
        this.backgroundTexture = new Texture(Gdx.files.internal("background/level_1.jpg"));
        this.backgroundImage = null;

        // LEVEL
        this.levelUI = null;
        this.toolbar = new PixelLogicUILevelToolbar(getAssets(), getEventManager());
        getStage().addActor(this.toolbar);
        // MENU

        this.menu = new PixelLogicUILevelMenu(getAssets(), getEventManager(), this);

        // STAGE
        this.screenListener = new ScreenListener(this);
        getStage().addListener(this.screenListener);
        getEventManager().listen(this.screenListener);
    }

    @Override
    public void activate(PixelLogicUIScreenData data) {
        super.activate(data);
        this.levelStatus = null;
        this.updateBackgroundImage();
        getStage().addAction(Actions.fadeIn(.5f));
    }

    @Override
    public void deactivate(Runnable after) {
        Action action = Actions.sequence(Actions.fadeOut(.5f), Actions.run(after));
        this.getStage().addAction(action);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.updateBounds();
        this.updateBackgroundImage();
    }

    public void loadLevel(PixelLogicLevel level) {
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
        Action fadeOutAction = Actions.sequence(Actions.fadeOut(.4f), Actions.run(new Runnable() {
            @Override
            public void run() {
                if (levelUI != null) {
                    levelUI.clear();
                    levelUI.remove();
                    changeLevelStatus(PixelLogicLevelStatus.destroyed);
                }
            }
        }));
        this.levelUI.addAction(fadeOutAction);
    }

    @Override
    public void show() {

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

    private void changeLevelStatus(PixelLogicLevelStatus status) {
        this.levelStatus = status;
        getEventManager().fire(new PixelLogicLevelStatusChangeEvent(this, getLevel(), getLevelStatus()));
    }

    public PixelLogicLevel getLevel() {
        return this.levelUI.getLevel();
    }

    public PixelLogicLevelStatus getLevelStatus() {
        return this.levelStatus;
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {

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
        this.toolbar.setBounds(0, screenHeight - toolbarHeight, screenWidth, toolbarHeight);

        // level
        if (this.levelUI != null) {
            int levelMaxHeight = PixelLogicUIUtil.getLevelMaxHeight();
            this.levelUI.resize();
            float x = screenWidth / 2f - this.levelUI.getWidth() / 2f;
            float y = levelMaxHeight / 2f - this.levelUI.getHeight() / 2f;
            this.levelUI.setPosition((int) x, (int) y);
        }

        // menu
        this.menu.setBounds(0, 0, screenWidth, screenHeight);
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

        private PixelLogicUILevelScreen screen;

        ScreenListener(PixelLogicUILevelScreen screen) {
            this.screen = screen;
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            if (PixelLogicLevelStatus.finished.equals(this.screen.getLevelStatus())) {
                this.screen.destroyLevel();
            }
            return super.touchDown(event, x, y, pointer, button);
        }

        @Override
        public void handle(PixelLogicEvent event) {
            if (event instanceof PixelLogicUserEvent) {
                PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
                if (PixelLogicUserEvent.Type.TOOLBAR_MENU_CLICKED.equals(userEvent.getType())) {
                    screen.menu.show();
                }
            }
            if (event instanceof PixelLogicNextLevelEvent) {
                PixelLogicNextLevelEvent nextLevelEvent = (PixelLogicNextLevelEvent) event;
                PixelLogicLevel nextLevel = nextLevelEvent.getNextLevel();
                screen.loadLevel(nextLevel);
            }
        }

    }

}
