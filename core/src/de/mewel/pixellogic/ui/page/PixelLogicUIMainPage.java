package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.ui.background.PixelLogicUIRotatingBoardBackground;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

public class PixelLogicUIMainPage extends PixelLogicUIBasePage {

    private PixelLogicUIRotatingBoardBackground background;

    private PixelLogicUIColoredSurface menuBackground;

    private PixelLogicUIButton campaignButton;

    private PixelLogicUIButton timeTrialButton;

    private PixelLogicUIButton achievementButton;

    public PixelLogicUIMainPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void buildGui(String headerText, PixelLogicUIPageId backPageId) {
        this.background = new PixelLogicUIRotatingBoardBackground(getAssets(), getEventManager());
        getStage().addActor(this.background);

        this.menuBackground = new PixelLogicUIColoredSurface(getAssets());
        this.menuBackground.setColor(new Color(255f, 255f, 255f, .6f));
        getStage().addActor(this.menuBackground);

        super.buildGui(headerText, backPageId);
    }

    @Override
    protected void build() {
        this.campaignButton = new PixelLogicUIButton(getAssets(), getEventManager(), "replay campaign") {
            @Override
            public void onClick() {
                PixelLogicCampaignMode campaignMode = new PixelLogicCampaignMode();
                campaignMode.setup(getGlobal());
                campaignMode.run();

                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                pageProperties.put("menuBackId", PixelLogicUIPageId.mainMenu);
                getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties);
            }
        };

        this.timeTrialButton = new PixelLogicUIButton(getAssets(), getEventManager(), "time trial") {
            @Override
            public void onClick() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.timeTrial, pageProperties);
            }
        };

        this.achievementButton = new PixelLogicUIButton(getAssets(), getEventManager(), "achievements") {
            @Override
            public void onClick() {

            }
        };

        getPageRoot().center();
        getPageRoot().addActor(this.campaignButton);
        getPageRoot().addActor(this.timeTrialButton);
        getPageRoot().addActor(this.achievementButton);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.background.setBounds(0, 0, width, height);
        this.campaignButton.setSize(getButtonWidth(), getButtonHeight());
        this.timeTrialButton.setSize(getButtonWidth(), getButtonHeight());
        this.achievementButton.setSize(getButtonWidth(), getButtonHeight());

        /*this.menuBackground.setBounds(getPadding(), getPadding(),
                width - getPadding() * 2,
                height - getPadding() * 2);*/
        this.menuBackground.setBounds(0, 0, width, height);
    }


}
