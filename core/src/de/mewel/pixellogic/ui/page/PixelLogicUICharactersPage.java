package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.mode.PixelLogicCharactersMode;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIAchievementBlock;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BLOCK_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUICharactersPage extends PixelLogicUIBasePage {

    private PixelLogicCharactersMode mode;

    private List<LevelContainer> levelContainers;

    public PixelLogicUICharactersPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.characters, "100 Characters", PixelLogicUIPageId.moreLevels);
    }

    @Override
    protected void build() {
        this.mode = new PixelLogicCharactersMode();
        this.mode.setup(getGlobal());
        List<PixelLogicLevel> levels = this.mode.getLevels();

        this.levelContainers = new ArrayList<LevelContainer>();
        for (PixelLogicLevel level : levels) {
            LevelContainer levelContainer = new LevelContainer(level, getGlobal());
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
            //levelContainer.updateLogo();
        }
        updateSize();
        fadeIn(null);
    }

    private class LevelContainer extends Container<HorizontalGroup> {

        private PixelLogicLevel level;

        private PixelLogicGlobal global;

        private Logo logo;

        private Container<Label> labelContainer;

        LevelContainer(PixelLogicLevel level, PixelLogicGlobal global) {
            super(new HorizontalGroup());
            this.level = level;
            this.global = global;

            getActor().setFillParent(true);
            getActor().top();
            getActor().left();
            getActor().fill();

            this.logo = new Logo();
            //this.logo.setSprite(level.get);
            this.logo.setSprite(global.getAssets().getIcon(5));
            getActor().addActor(this.logo);

            Label descriptionLabel = this.getLabel(level.getName(), TEXT_COLOR);
            descriptionLabel.setWrap(true);
            this.labelContainer = new Container<Label>(descriptionLabel);
            getActor().addActor(this.labelContainer);

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
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
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 2);
            return new Label.LabelStyle(labelFont, color);
        }

        private class Logo extends Actor {

            private Sprite sprite;

            public void setSprite(Sprite sprite) {
                this.sprite = sprite;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                super.draw(batch, parentAlpha);
                if (sprite != null) {
                    float alpha = parentAlpha * color.a;
                    batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
                    batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
                }
                batch.setColor(color);
            }

        }


    }

}
