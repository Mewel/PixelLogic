package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIAchievementBlock;
import de.mewel.pixellogic.ui.component.PixelLogicUIContainer;

public class PixelLogicUIAchievementsPage extends PixelLogicUIBasePage {

    private List<AchievementContainer> achievementContainers;

    public PixelLogicUIAchievementsPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.achievements, global.getAssets().translate("achievements"), PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.achievementContainers = new ArrayList<>();
        List<PixelLogicAchievement> achievements = getGlobal().getAchievements().list();
        for (PixelLogicAchievement achievement : achievements) {
            AchievementContainer achievementContainer = new AchievementContainer(achievement, getGlobal());
            getPageRoot().addActor(achievementContainer);
            this.achievementContainers.add(achievementContainer);
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
        getPageRoot().padTop(getPadding() / 4f);
        getPageRoot().padBottom(getPadding() / 4f);
        getPageRoot().space(getSpace() / 8f);

        for (AchievementContainer achievementContainer : this.achievementContainers) {
            achievementContainer.resize((int) this.getWidth(), (int) this.getHeight());
        }

        getPageRoot().invalidate();
        getPageRoot().setHeight(getPageRoot().getPrefHeight());
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        for (AchievementContainer achievementContainer : this.achievementContainers) {
            achievementContainer.updateLogo();
        }
        updateSize();
        fadeIn(null);
    }

    private class AchievementContainer extends PixelLogicUIContainer<HorizontalGroup> {

        private final PixelLogicAchievement achievement;

        private final Logo logo;

        private final PixelLogicUIAchievementBlock block;

        AchievementContainer(PixelLogicAchievement achievement, PixelLogicGlobal global) {
            super(global, new HorizontalGroup());
            this.achievement = achievement;

            getActor().setFillParent(true);
            getActor().top();
            getActor().left();
            getActor().fill();

            this.logo = new Logo();
            getActor().addActor(this.logo);
            updateLogo();

            this.block = new PixelLogicUIAchievementBlock(global);
            this.block.setAchievement(achievement.getName(), achievement.getDescription());
            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());
            getActor().addActor(this.block);
        }

        public void updateLogo() {
            if (this.achievement.isDone()) {
                this.logo.setColor(getGlobal().getStyle().getMainColor());
                this.logo.setSprite(getAssets().getIcon(4));
            } else {
                this.logo.setColor(getGlobal().getStyle().getTextSecondaryColor());
                this.logo.setSprite(getAssets().getIcon(1));
            }
        }

        public void resize(int width, int height) {
            int padding = getPadding();
            float logoSize = Math.min(width / 10, height / 10);

            int groupPadding = padding / 5;
            getActor().pad(groupPadding);
            getActor().space(padding / 2f);

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
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            return new Label.LabelStyle(labelFont, this.achievement.isDone() ?
                    new Color(getStyle().getMainColor()) : getStyle().getTextSecondaryColor());
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 0);
            return new Label.LabelStyle(labelFont, getStyle().getTextColor());
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

                    batch.setColor(color.r, color.g, color.b, color.a * alpha);
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
