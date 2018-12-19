package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicCharactersMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;

import static de.mewel.pixellogic.PixelLogicConstants.BLOCK_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.TEXT_COLOR;

public class PixelLogicUICharactersPage extends PixelLogicUIBasePage {

    private PixelLogicCharactersMode mode;

    private List<LevelContainer> levelContainers;

    private Texture levelTexture;

    public PixelLogicUICharactersPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.characters, "100 Characters", PixelLogicUIPageId.play);
    }

    @Override
    protected void build() {
        this.mode = new PixelLogicCharactersMode();
        this.mode.setup(getGlobal());
        List<PixelLogicLevel> levels = this.mode.getLevels();

        PixelLogicLevelCollection collection = this.mode.getCollection();
        this.levelTexture = new Texture(collection.getPixmap());

        this.levelContainers = new ArrayList<LevelContainer>();
        for (final PixelLogicLevel level : levels) {
            Sprite image = collection.getSprite(level.getName(), levelTexture, 8, 8);
            LevelContainer levelContainer = new LevelContainer(level, image, getGlobal());

            levelContainer.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    pageProperties.put("menuBackId", PixelLogicUIPageId.characters);
                    getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties, new Runnable() {
                        @Override
                        public void run() {
                            mode.activate();
                            mode.run(level);
                        }
                    });
                }
            });

            getPageRoot().addActor(levelContainer);
            this.levelContainers.add(levelContainer);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.updateSize();
    }

    protected void updateSize() {
        getPageRoot().setWidth(this.getWidth());
        getPageRoot().pad(0);
        getPageRoot().padTop(getPadding() / 4);
        getPageRoot().padBottom(getPadding() / 4);
        getPageRoot().space(getSpace() / 8);

        for (LevelContainer levelContainer : this.levelContainers) {
            levelContainer.resize((int) this.getWidth(), (int) this.getHeight());
        }

        getPageRoot().invalidate();
        getPageRoot().setHeight(getPageRoot().getPrefHeight());
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        for (LevelContainer levelContainer : this.levelContainers) {
            levelContainer.updateLogo();
            levelContainer.updateLabel();
        }
        updateSize();
        fadeIn(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (this.levelTexture != null) {
            this.levelTexture.dispose();
        }
    }

    private class LevelContainer extends Container<HorizontalGroup> {

        private Sprite image;

        private PixelLogicLevel level;

        private PixelLogicGlobal global;

        private PixelLogicUISprite logo;

        private Container<Label> labelContainer;

        LevelContainer(PixelLogicLevel level, Sprite image, PixelLogicGlobal global) {
            super(new HorizontalGroup());
            this.level = level;
            this.image = image;
            this.global = global;

            getActor().setFillParent(true);
            getActor().top();
            getActor().left();
            getActor().fill();

            this.logo = new PixelLogicUISprite(getAssets(), getEventManager());
            getActor().addActor(this.logo);
            this.updateLogo();

            Label descriptionLabel = this.getLabel(level.getName(), TEXT_COLOR);
            descriptionLabel.setWrap(true);
            this.labelContainer = new Container<Label>(descriptionLabel);
            getActor().addActor(this.labelContainer);
            this.updateLabel();

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        private void updateLogo() {
            Sprite sprite = isSolved() ? image : global.getAssets().getIcon(5);
            this.logo.setSprite(sprite);
        }

        private void updateLabel() {
            String label = isSolved() ? this.level.getName() : "???";
            this.labelContainer.getActor().setText(label);
        }

        private boolean isSolved() {
            return mode.isLevelSolved(level);
        }

        public void resize(int width, int height) {
            int padding = getPadding();
            int logoSize = Math.max(width / 16, height / 16);

            int groupPadding = padding / 5;
            getActor().pad(groupPadding);
            getActor().space(padding / 2);

            this.logo.setSize(logoSize, logoSize);

            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle(TEXT_COLOR));

            this.width(width);
        }

        public Label getLabel(String text, Color color) {
            Label.LabelStyle style = getLabelStyle(color);
            return new Label(text, style);
        }

        private Label.LabelStyle getLabelStyle(Color color) {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 1);
            return new Label.LabelStyle(labelFont, color);
        }

    }

}
