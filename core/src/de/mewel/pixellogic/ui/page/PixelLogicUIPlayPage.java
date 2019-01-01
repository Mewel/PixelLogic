package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicCampaignMode;
import de.mewel.pixellogic.mode.PixelLogicTutorialMode;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_COLOR;

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

        this.modes = new ArrayList<LevelModeUI>();
        this.modes.add(new LevelModeUI(getCampaignLabel(), getAssets().translate("play.campaign.description"), this, new Runnable() {
            @Override
            public void run() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                pageProperties.put("menuBackId", PixelLogicUIPageId.play);
                getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties, new Runnable() {
                    @Override
                    public void run() {
                        campaignMode.activate();
                        campaignMode.run();
                    }
                });
            }
        }));
        this.modes.add(new LevelModeUI(getAssets().translate("play.characters.title"), getAssets().translate("play.characters.description"), this, new Runnable() {
            @Override
            public void run() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.characters, pageProperties);
            }
        }));
        this.modes.add(new LevelModeUI(getAssets().translate("play.timeTrial.title"), getAssets().translate("play.timeTrial.description"), this, new Runnable() {
            @Override
            public void run() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.timeTrial, pageProperties);
            }
        }));
        this.modes.add(new LevelModeUI(getAssets().translate("play.art.title"), getAssets().translate("play.art.description"),
                this, new Runnable() {
            @Override
            public void run() {
                PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                getAppScreen().setPage(PixelLogicUIPageId.picture, pageProperties);
            }
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
        return new PlayHeader(getAssets(), getEventManager(), headerText, backPageId);
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

    private class LevelModeUI extends Container<VerticalGroup> {

        private Container<Label> labelContainer;

        private PixelLogicUIButton button;

        public LevelModeUI(final String buttonText, final String description,
                           PixelLogicUIPlayPage page, final Runnable buttonAction) {
            super(new VerticalGroup());

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            Label descriptionLabel = this.getLabel(description);
            descriptionLabel.setWrap(true);
            this.labelContainer = new Container<Label>(descriptionLabel);
            getActor().addActor(this.labelContainer);

            this.button = new PixelLogicUIButton(page.getAssets(), page.getEventManager(), buttonText) {
                @Override
                public void handleClick() {
                    buttonAction.run();
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            getActor().pack();

            Texture whiteTexture = PixelLogicUIUtil.getTexture(new Color(BLOCK_COLOR));
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2;
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

        private HeaderButton tutorialButton;

        public PlayHeader(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text, PixelLogicUIPageId backPageId) {
            super(assets, eventManager, text, backPageId);
            this.tutorialButton = new HeaderButton(assets.getIcon(6));
            this.tutorialButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    PixelLogicUIUtil.playButtonSound(getAssets());
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    pageProperties.put("menuBackId", PixelLogicUIPageId.play);
                    getAppScreen().setPage(PixelLogicUIPageId.tutorialLevel, pageProperties, new Runnable() {
                        @Override
                        public void run() {
                            tutorialMode.activate();
                            tutorialMode.run();
                        }
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
            int y = (int) (this.getHeight() / 2f - size / 2f);
            int x = (int) this.getWidth() - getPadding() - size;
            this.tutorialButton.setBounds(x, y, size, size);
        }
    }

}
