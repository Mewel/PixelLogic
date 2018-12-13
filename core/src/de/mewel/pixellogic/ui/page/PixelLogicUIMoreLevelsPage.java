package de.mewel.pixellogic.ui.page;

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
import de.mewel.pixellogic.mode.PixelLogicTutorialMode;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_COLOR;

public class PixelLogicUIMoreLevelsPage extends PixelLogicUIBasePage {

    private List<LevelModeUI> modes;

    public PixelLogicUIMoreLevelsPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.moreLevels, "More Levels", PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.modes = new ArrayList<LevelModeUI>();
        this.modes.add(new LevelModeUI("100 Characters", "[TEXT_COLOR]Play over [MAIN_COLOR]50 bonus[] levels based on" +
                " the awesome pixel art by Johan Vinet.",
                PixelLogicUIPageId.characters, this));
        this.modes.add(new LevelModeUI("Time trial", "[TEXT_COLOR]Play [MAIN_COLOR]infinite computer " +
                "generated[] levels against the clock and try to beat your highscore.",
                PixelLogicUIPageId.timeTrial, this));
        this.modes.add(new LevelModeUI("Art Puzzles", "[TEXT_COLOR]Discover beautiful and famous art",
                PixelLogicUIPageId.picture, this));
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
        fadeIn(null);
    }

    @Override
    protected Header buildHeader(String headerText, PixelLogicUIPageId backPageId) {
        return new PlayHeader(getAssets(), getEventManager(), headerText, backPageId);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        for (LevelModeUI mode : this.modes) {
            mode.resize();
        }
    }

    private class LevelModeUI extends Container<VerticalGroup> {

        private Container<Label> labelContainer;

        private PixelLogicUIButton button;

        public LevelModeUI(final String buttonText, final String description, final PixelLogicUIPageId pageId,
                           PixelLogicUIMoreLevelsPage page) {
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
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    getAppScreen().setPage(pageId, pageProperties);
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            getActor().pack();

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
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

            final PixelLogicTutorialMode tutorialMode = new PixelLogicTutorialMode();
            tutorialMode.setup(getGlobal());

            this.tutorialButton = new HeaderButton(assets.getIcon(6));
            this.tutorialButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    PixelLogicUIUtil.playButtonSound(getAssets());
                    tutorialMode.activate();
                    tutorialMode.run();

                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    pageProperties.put("menuBackId", PixelLogicUIPageId.moreLevels);
                    getAppScreen().setPage(PixelLogicUIPageId.tutorialLevel, pageProperties);
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
