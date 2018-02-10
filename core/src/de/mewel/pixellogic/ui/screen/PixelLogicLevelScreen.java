package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevelMenu;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevelResolutionManager;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevelToolbar;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.BASE_SIZE;

public class PixelLogicLevelScreen implements Screen {

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private Stage stage;

    private PixelLogicGUILevel levelUI;

    private PixelLogicGUILevelToolbar toolbar;

    private PixelLogicGUILevelMenu menu;

    private Texture backgroundTexture;

    private Image backgroundImage;

    private PixelLogicLevelStatus levelStatus;

    private ScreenListener screenListener;

    public PixelLogicLevelScreen() {
        this.levelStatus = null;
        this.stage = new Stage();

        // BACKGROUND
        this.backgroundTexture = new Texture(Gdx.files.internal("background/level_1.jpg"));
        this.backgroundImage = null;
        this.updateBackgroundImage();

        // LEVEL
        this.levelUI = null;
        this.toolbar = new PixelLogicGUILevelToolbar();
        this.stage.addActor(this.toolbar);

        // MENU
        this.menu = new PixelLogicGUILevelMenu(this);

        // STAGE
        this.screenListener = new ScreenListener(this);
        this.stage.addListener(this.screenListener);
        PixelLogicEventManager.instance().listen(this.screenListener);
        Gdx.input.setInputProcessor(this.stage);
    }

    private void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(true);
        stage.setViewport(new ExtendViewport(width, height, camera));
    }

    @Override
    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.stage.getViewport().update(width, height);
        this.updateBounds();
        this.updateBackgroundImage();
    }

    public void loadLevel(PixelLogicLevel level) {
        updateViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.levelUI = new PixelLogicGUILevel();
        this.levelUI.getColor().a = .0f;
        this.levelUI.load(level);
        this.stage.addActor(this.levelUI);
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

    private FPSLogger fpsLogger = new FPSLogger();

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        if (PixelLogicLevelStatus.playable.equals(this.levelStatus) && this.levelUI.isSolved()) {
            this.levelStatus = PixelLogicLevelStatus.finished;
            Action delay = Actions.sequence(Actions.delay(.1f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    onSolved();
                }
            }));
            stage.addAction(delay);
        }

        fpsLogger.log();

        stage.act(delta);
        stage.draw();
    }

    private void onSolved() {
        changeLevelStatus(PixelLogicLevelStatus.solved);
        this.stage.addAction(Actions.sequence(
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
        PixelLogicEventManager.instance().fire(new PixelLogicLevelChangeEvent(this, getLevel(), getLevelStatus()));
    }

    public PixelLogicLevel getLevel() {
        return this.levelUI.getLevel();
    }

    public PixelLogicLevelStatus getLevelStatus() {
        return this.levelStatus;
    }

    public Stage getStage() {
        return stage;
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
        PixelLogicEventManager.instance().remove(this.screenListener);
        this.stage.dispose();
        this.backgroundTexture.dispose();
    }

    private void updateBounds() {
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        // root
        this.stage.getRoot().setSize(screenWidth, screenHeight);

        // toolbar
        int baseHeight = PixelLogicGUILevelResolutionManager.instance().getIconBaseHeight() * 2;
        int toolbarHeight = Math.max(baseHeight, BASE_SIZE * 2);
        int toolbarPaddingTop = toolbarHeight / 10;
        this.toolbar.setBounds(0, screenHeight - toolbarHeight, screenWidth, toolbarHeight);

        // level
        if (this.levelUI != null) {
            int levelMaxHeight = screenHeight - (int) this.toolbar.getHeight() - toolbarPaddingTop;
            this.levelUI.resize(screenWidth, levelMaxHeight);
            float x = screenWidth / 2f - this.levelUI.getWidth() / 2f;
            float y = levelMaxHeight / 2f - this.levelUI.getHeight() / 2f;
            this.levelUI.setPosition(x, y);
        }

        // menu
        this.menu.setBounds(0, 0, screenWidth, screenHeight);
    }

    private void updateBackgroundImage() {
        Gdx.app.log("screen", "" + this.stage.getRoot().getWidth());
        if(this.backgroundImage != null) {
            this.backgroundImage.remove();
        }
        this.backgroundImage = new Image(backgroundTexture);
        this.backgroundImage.setFillParent(true);
        this.backgroundImage.setScaling(Scaling.fill);
        this.backgroundImage.setPosition(this.backgroundImage.getImageWidth(), 0);
        this.stage.getRoot().addActorAt(0, this.backgroundImage);
    }

    private static class ScreenListener extends InputListener implements PixelLogicListener {

        private PixelLogicLevelScreen screen;

        ScreenListener(PixelLogicLevelScreen screen) {
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
        }

    }

}
