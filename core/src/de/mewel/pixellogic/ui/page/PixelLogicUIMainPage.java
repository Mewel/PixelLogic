package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.ui.background.PixelLogicUIRotatingBoardBackground;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.component.PixelLogicUIVerticalGroup;

public class PixelLogicUIMainPage extends PixelLogicUIBasePage {

    private PixelLogicUIRotatingBoardBackground background;

    private PixelLogicUIColoredSurface backgroundOverlay;

    private Image logoImage;

    private PixelLogicUIVerticalGroup buttonGroup;

    private PixelLogicUIButton campaignButton;

    private PixelLogicUIButton moreLevelsButton;

    private PixelLogicUIButton achievementButton;

    private PixelLogicUIButton aboutButton;

    private PixelLogicCampaignMode campaignMode;

    public PixelLogicUIMainPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.mainMenu);
        this.campaignMode = new PixelLogicCampaignMode();
    }

    @Override
    protected void buildGui(String headerText, PixelLogicUIPageId backPageId) {
        this.campaignMode = new PixelLogicCampaignMode();

        this.background = new PixelLogicUIRotatingBoardBackground(getAssets(), getEventManager());
        getStage().addActor(this.background);

        this.backgroundOverlay = new PixelLogicUIColoredSurface(getAssets());
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

        this.campaignButton = new PixelLogicUIButton(getAssets(), getEventManager(), getCampaignLabel()) {

            @Override
            public void handleClick() {
                campaignButton.setText(getCampaignLabel());
                Preferences campaignPreferences = getCampaignPreferences();
                campaignPreferences.putBoolean("started", true);
                campaignPreferences.flush();

                campaignMode.setup(getGlobal());
                campaignMode.activate();
                campaignMode.run();

                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                pageProperties.put("menuBackId", PixelLogicUIPageId.mainMenu);
                getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties);
            }
        };

        this.moreLevelsButton = new PixelLogicUIButton(getAssets(), getEventManager(), "more levels") {
            @Override
            public void handleClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.moreLevels, pageProperties);
            }
        };

        this.achievementButton = new PixelLogicUIButton(getAssets(), getEventManager(), "achievements") {
            @Override
            public void handleClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.achievements, pageProperties);
            }
        };

        this.aboutButton = new PixelLogicUIButton(getAssets(), getEventManager(), "about") {
            @Override
            public void handleClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.about, pageProperties);
            }
        };

        buttonGroup = new PixelLogicUIVerticalGroup();
        PixelLogicUIColoredSurface buttonGroupBackground = new PixelLogicUIColoredSurface(getAssets());
        buttonGroupBackground.setColor(new Color(255f, 255f, 255f, .5f));
        buttonGroup.setBackground(buttonGroupBackground);
        buttonGroup.center();

        buttonGroup.addActor(this.campaignButton);
        buttonGroup.addActor(this.moreLevelsButton);
        buttonGroup.addActor(this.achievementButton);
        buttonGroup.addActor(this.aboutButton);

        getPageRoot().center();
        getPageRoot().addActor(logoImage);
        getPageRoot().addActor(buttonGroup);
    }

    private String getCampaignLabel() {
        boolean campaignStarted = getCampaignPreferences().getBoolean("started");
        Gdx.app.log("main", "campaign started " + campaignStarted);
        return campaignStarted ? "continue campaign" : "start campaign";
    }

    private Preferences getCampaignPreferences() {
        return Gdx.app.getPreferences(this.campaignMode.getName());
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        if (this.campaignButton != null) {
            this.campaignButton.setText(getCampaignLabel());
            this.campaignButton.unblock();
        }
        if (this.moreLevelsButton != null) {
            this.moreLevelsButton.unblock();
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
    public void resize(int width, int height) {
        super.resize(width, height);

        this.background.setBounds(0, 0, width, height);
        this.backgroundOverlay.setBounds(0, 0, width, height);
        this.campaignButton.setSize(getButtonWidth(), getButtonHeight());
        this.moreLevelsButton.setSize(getButtonWidth(), getButtonHeight());
        this.achievementButton.setSize(getButtonWidth(), getButtonHeight());
        this.aboutButton.setSize(getButtonWidth(), getButtonHeight());

        this.buttonGroup.pad(getPadding());
        this.buttonGroup.space(getSpace());
        this.buttonGroup.invalidate();

        this.logoImage.setScale((int) (height / (this.logoImage.getHeight() * 6f)));

        getPageRoot().padTop(getPadding() * 3);
        getPageRoot().space(getSpace() * 3);
    }

    public void pause() {
        Gdx.app.log("main", "pause");
    }

    public void resume() {
        Gdx.app.log("main", "resume");
    }


}
