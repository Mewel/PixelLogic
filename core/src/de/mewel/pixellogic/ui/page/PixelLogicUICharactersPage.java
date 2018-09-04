package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
            levelContainer.updateLogo();
        }
        updateSize();
        fadeIn(null);
    }

    private class LevelContainer extends Container<HorizontalGroup> {

        private PixelLogicLevel level;

        private PixelLogicGlobal global;

        private Logo logo;

        private PixelLogicUIAchievementBlock block;

        LevelContainer(PixelLogicLevel level, PixelLogicGlobal global) {
            super(new HorizontalGroup());
            this.level = level;
            this.global = global;

            getActor().setFillParent(true);
            getActor().top();
            getActor().left();
            getActor().fill();

            this.logo = new Logo();
            getActor().addActor(this.logo);
            updateLogo();

            this.block = new PixelLogicUIAchievementBlock(global.getAssets(), global.getEventManager());
            this.block.setAchievement(achievement.getName(), achievement.getDescription());
            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());
            getActor().addActor(this.block);

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void updateLogo() {
            if (this.achievement.isDone()) {
                this.logo.setColor(PixelLogicUIConstants.PIXEL_BLOCKED_COLOR);
                this.logo.setSprite(global.getAssets().getIcon(4));
            } else {
                this.logo.setColor(PixelLogicUIConstants.TEXT_LIGHT_COLOR);
                this.logo.setSprite(global.getAssets().getIcon(1));
            }
        }

        public void resize(int width, int height) {
            int padding = getPadding();
            float logoSize = Math.min(width / 10, height / 10);

            int groupPadding = padding / 5;
            getActor().pad(groupPadding);
            getActor().space(padding / 2);

            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());

            this.block.setWidth((int) (width - (padding + logoSize)));
            this.block.setHeight((int) this.block.getPrefHeight());
            this.logo.setSize(logoSize, logoSize);

            this.width(width);

            float componentHeight = Math.max(this.block.getPrefHeight(), this.logo.getHeight());
            float containerHeight = componentHeight + getActor().getPadBottom() + getActor().getPadTop();
            this.height(containerHeight);

            getActor().setHeight(containerHeight - groupPadding * 2);
        }

        private Label.LabelStyle getHeaderStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 2);
            return new Label.LabelStyle(labelFont, Color.BLACK);
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 0);
            return new Label.LabelStyle(labelFont, Color.BLACK);
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
                    float size = PixelLogicUIUtil.getIconBaseHeight();
                    float alpha = parentAlpha * color.a;

                    batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
                    int x = (int) (getX() + getWidth() / 2 - size / 2);
                    int y = (int) (getY() + getHeight() / 2 - size / 2);
                    batch.draw(sprite, x,
                            y, size, size);
                }
                batch.setColor(color);
            }

        }


    }

}
