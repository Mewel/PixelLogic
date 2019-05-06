package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.mode.PixelLogicTutorialMode;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.background.PixelLogicUIRotatingBoardBackground;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.component.PixelLogicUIExitDialog;
import de.mewel.pixellogic.ui.component.PixelLogicUISettings;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;
import de.mewel.pixellogic.ui.component.PixelLogicUIVerticalGroup;

import static de.mewel.pixellogic.PixelLogicConstants.TEXT_COLOR;

public class PixelLogicUIMainPage extends PixelLogicUIBasePage {

    private PixelLogicUIRotatingBoardBackground background;

    private PixelLogicUIColoredSurface backgroundOverlay;

    private Image logoImage;

    private PixelLogicUIVerticalGroup buttonGroup;

    private PixelLogicUIButton playButton;

    private PixelLogicUIButton achievementButton;

    private PixelLogicUIButton aboutButton;

    private PixelLogicUISprite settingsButton;

    private PixelLogicUISettings settings;

    private PixelLogicUIExitDialog exitDialog;

    public PixelLogicUIMainPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void buildGui(String headerText, PixelLogicUIPageId backPageId) {
        this.background = new PixelLogicUIRotatingBoardBackground(getGlobal());
        getStage().addActor(this.background);

        this.backgroundOverlay = new PixelLogicUIColoredSurface(getGlobal());
        this.backgroundOverlay.setColor(new Color(255f, 255f, 255f, .5f));
        this.backgroundOverlay.setInheritParentAlpha(false);
        getStage().addActor(this.backgroundOverlay);

        super.buildGui(headerText, backPageId);
    }

    @Override
    protected void build() {
        Texture logo = getAssets().getLogo();
        logoImage = new Image(logo);
        logoImage.setOrigin(Align.center);

        this.playButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("play")) {
            @Override
            public void handleClick() {
                Preferences preferences = getPreferences();
                boolean firstTimeOpenApp = preferences.getBoolean("firstTimeOpenApp", true);
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                if (firstTimeOpenApp) {
                    preferences.putBoolean("firstTimeOpenApp", false);
                    preferences.flush();
                    PixelLogicUIPlayPage page = (PixelLogicUIPlayPage) getAppScreen().getPage(PixelLogicUIPageId.play);
                    final PixelLogicTutorialMode tutorialMode = page.getTutorialMode();
                    pageProperties.put("menuBackId", PixelLogicUIPageId.play);
                    getAppScreen().setPage(PixelLogicUIPageId.tutorialLevel, pageProperties, new Runnable() {
                        @Override
                        public void run() {
                            tutorialMode.activate();
                            tutorialMode.run();
                        }
                    });
                    return;
                }
                getAppScreen().setPage(PixelLogicUIPageId.play, pageProperties);
            }
        };

        this.achievementButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("achievements")) {
            @Override
            public void handleClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.achievements, pageProperties);
            }
        };

        this.aboutButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("about")) {
            @Override
            public void handleClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.about, pageProperties);
            }
        };

        buttonGroup = new PixelLogicUIVerticalGroup();
        PixelLogicUIColoredSurface buttonGroupBackground = new PixelLogicUIColoredSurface(getGlobal());
        buttonGroupBackground.setColor(new Color(255f, 255f, 255f, .5f));
        buttonGroup.setBackground(buttonGroupBackground);
        buttonGroup.center();

        buttonGroup.addActor(this.playButton);
        buttonGroup.addActor(this.achievementButton);
        buttonGroup.addActor(this.aboutButton);

        getPageRoot().center();
        getPageRoot().addActor(logoImage);
        getPageRoot().addActor(buttonGroup);

        // settings
        this.settingsButton = new PixelLogicUISprite(getGlobal(), 10);
        this.settingsButton.setColor(TEXT_COLOR);
        this.settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                settings.show(getStage().getRoot());
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        getStage().getRoot().addActor(settingsButton);

        // settings
        this.settings = new PixelLogicUISettings(getGlobal());

        // exit dialog
        this.exitDialog = new PixelLogicUIExitDialog(getGlobal(), getStage().getRoot());
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        if (this.playButton != null) {
            this.playButton.unblock();
        }
        if (this.achievementButton != null) {
            this.achievementButton.unblock();
        }
        if (this.aboutButton != null) {
            this.aboutButton.unblock();
        }
        fadeIn(null);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        if (event instanceof PixelLogicUserEvent) {
            PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
            if (PixelLogicUserEvent.Type.BACK_BUTTON_CLICKED.equals(userEvent.getType())) {
                if (settings.isShown()) {
                    settings.close();
                } else if (exitDialog.isShown()) {
                    exitDialog.close();
                } else {
                    exitDialog.show();
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        this.background.setBounds(0, 0, width, height);
        this.backgroundOverlay.setBounds(0, 0, width, height);
        this.playButton.setSize(getButtonWidth(), getButtonHeight());
        this.achievementButton.setSize(getButtonWidth(), getButtonHeight());
        this.aboutButton.setSize(getButtonWidth(), getButtonHeight());

        this.buttonGroup.pad(getPadding());
        this.buttonGroup.space(getSpace());
        this.buttonGroup.invalidate();

        this.logoImage.setScale((int) (height / (this.logoImage.getHeight() * 6f)));

        int iconSize = PixelLogicUIUtil.getIconBaseHeight();
        this.settingsButton.setSize(iconSize, iconSize);
        this.settingsButton.pad(iconSize);
        this.settingsButton.setPosition(width - this.settingsButton.getWidth(), 0);

        this.settings.setWidth(width);

        this.exitDialog.setBounds(0, 0, width, height);

        getPageRoot().padTop(getPadding() * 3);
        getPageRoot().space(getSpace() * 3);
    }

    public void pause() {
        Gdx.app.log("main", "pause");
    }

    public void resume() {
        Gdx.app.log("main", "resume");
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences("pixellogic_settings");
    }

}
