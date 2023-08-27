package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.mode.PixelLogicTutorialMode;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.component.PixelLogicUIContainer;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public class PixelLogicUIPlayPage extends PixelLogicUIBasePage {

    private List<LevelModeUI> modes;

    private PixelLogicCampaignMode campaignMode;

    private PixelLogicTutorialMode tutorialMode;

    public PixelLogicUIPlayPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.play, global.getAssets().translate("play"), PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.tutorialMode = new PixelLogicTutorialMode();
        this.tutorialMode.setup(getGlobal());

        this.campaignMode = new PixelLogicCampaignMode();
        this.campaignMode.setup(getGlobal());

        this.modes = new ArrayList<>();
        this.modes.add(new LevelModeUI(getGlobal(), getCampaignLabel(), getAssets().translate("play.campaign.description"), () -> {
            PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
            pageProperties.put("menuBackId", PixelLogicUIPageId.play);
            getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties, () -> {
                campaignMode.activate();
                campaignMode.run();
            });
        }));
        this.modes.add(new LevelModeUI(getGlobal(), getAssets().translate("play.characters.title"), getAssets().translate("play.characters.description"), () -> {
            PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
            getAppScreen().setPage(PixelLogicUIPageId.characters, pageProperties);
        }));
        this.modes.add(new LevelModeUI(getGlobal(), getAssets().translate("play.timeTrial.title"), getAssets().translate("play.timeTrial.description"), () -> {
            PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
            getAppScreen().setPage(PixelLogicUIPageId.timeTrial, pageProperties);
        }));
        this.modes.add(new LevelModeUI(getGlobal(), getAssets().translate("play.art.title"), getAssets().translate("play.art.description"),
                () -> {
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    getAppScreen().setPage(PixelLogicUIPageId.picture, pageProperties);
                }));
        this.buildModes();
    }

    protected void buildModes() {
        for (LevelModeUI mode : this.modes) {
            getPageRoot().addActor(mode);
        }
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        for (LevelModeUI mode : this.modes) {
            mode.activate();
        }
        this.modes.get(0).button.setText(getCampaignLabel());
        fadeIn(null);
    }

    @Override
    protected Header buildHeader(String headerText, PixelLogicUIPageId backPageId) {
        return new PlayHeader(getGlobal(), headerText, backPageId);
    }

    private Preferences getCampaignPreferences() {
        return Gdx.app.getPreferences("pixellogic_" + this.campaignMode.getName());
    }

    private String getCampaignLabel() {
        boolean campaignStarted = getCampaignPreferences().getBoolean("started");
        return getAssets().translate("play.campaign." + (campaignStarted ? "continue" : "start"));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (LevelModeUI mode : this.modes) {
            mode.resize();
        }
    }

    public PixelLogicTutorialMode getTutorialMode() {
        return this.tutorialMode;
    }

    private class LevelModeUI extends PixelLogicUIContainer<VerticalGroup> {

        private final Container<Label> labelContainer;

        private final PixelLogicUIButton button;

        public LevelModeUI(PixelLogicGlobal global, final String buttonText, final String description,
                           final Runnable buttonAction) {
            super(global, new VerticalGroup());

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            Label descriptionLabel = this.getLabel(description);
            descriptionLabel.setWrap(true);
            this.labelContainer = new Container<>(descriptionLabel);
            getActor().addActor(this.labelContainer);

            this.button = new PixelLogicUIButton(getGlobal(), buttonText) {
                @Override
                public void handleClick() {
                    buttonAction.run();
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            getActor().pack();
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2f;
            this.pad(padding, 0, padding, padding * 2);
            this.button.setSize(getButtonWidth(), getButtonHeight());

            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle());
        }

        public void activate() {
            this.button.unblock();
        }

        public Label getLabel(String text) {
            Label.LabelStyle style = getLabelStyle();
            return new Label(text, style);
        }

        private Label.LabelStyle getLabelStyle() {
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 0);
            return new Label.LabelStyle(font, Color.WHITE);
        }

    }

    protected class PlayHeader extends Header {

        private final PixelLogicUISprite tutorialButton;

        public PlayHeader(PixelLogicGlobal global, String text, PixelLogicUIPageId backPageId) {
            super(global, text, backPageId);
            this.tutorialButton = new PixelLogicUISprite(global, 6);
            this.tutorialButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    pageProperties.put("menuBackId", PixelLogicUIPageId.play);
                    getAppScreen().setPage(PixelLogicUIPageId.tutorialLevel, pageProperties, () -> {
                        tutorialMode.activate();
                        tutorialMode.run();
                    });
                }
            });
            this.addActor(this.tutorialButton);
        }

        @Override
        protected void positionChanged() {
            super.positionChanged();

            // tutorial button
            int size = PixelLogicUIUtil.getIconBaseHeight();
            int iconPadding = size / 2;
            int y = (int) (this.getHeight() / 2f - size / 2f) - iconPadding;
            int x = (int) this.getWidth() - getPadding() - size - iconPadding;
            this.tutorialButton.setBounds(x, y, size, size);
            this.tutorialButton.pad(iconPadding);
        }
    }

}
