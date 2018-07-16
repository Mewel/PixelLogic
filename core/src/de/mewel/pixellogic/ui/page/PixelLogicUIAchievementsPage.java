package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIAchievementBlock;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BLOCK_COLOR;

public class PixelLogicUIAchievementsPage extends PixelLogicUIBasePage {

    private List<AchievementContainer> achievementContainers;

    public PixelLogicUIAchievementsPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.achievements, "Achievements", PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.achievementContainers = new ArrayList<AchievementContainer>();
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

        getPageRoot().setWidth(width);
        getPageRoot().pad(0);
        getPageRoot().padTop(getPadding() / 4);
        getPageRoot().padBottom(getPadding() / 4);
        getPageRoot().space(getSpace() / 8);

        for (AchievementContainer achievementContainer : this.achievementContainers) {
            achievementContainer.resize();
        }

        Gdx.app.log("width", width + "");
    }

    private static class AchievementContainer extends Container<HorizontalGroup> {

        private PixelLogicGlobal global;

        private Logo logo;

        private PixelLogicUIAchievementBlock block;

        AchievementContainer(PixelLogicAchievement achievement, PixelLogicGlobal global) {
            super(new HorizontalGroup());
            this.global = global;

            getActor().setFillParent(true);
            getActor().top();
            getActor().left();
            getActor().setDebug(true);
            getActor().fill();

            this.logo = new Logo(global.getAssets().getIcon(4));
            this.logo.setColor(achievement.isDone() ? Color.GREEN : Color.LIGHT_GRAY);
            getActor().addActor(this.logo);
            this.logo.setDebug(true);

            this.block = new PixelLogicUIAchievementBlock(global.getAssets(), global.getEventManager());
            this.block.setAchievement(achievement.getName(), achievement.getDescription());
            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());
            this.block.setDebug(true);
            getActor().addActor(this.block);

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void resize() {
            int width = Gdx.graphics.getWidth();
            int padding = getPadding();
            float logoSize = width / 5;

            getActor().pad(padding / 2);
            getActor().space(padding);

            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());
            this.block.setWidth(width - (padding * 2 + logoSize));
            this.logo.setSize(logoSize, logoSize);

            Gdx.app.log("block height", this.block.getHeight() + "");
            Gdx.app.log("logo height", this.logo.getHeight() + "");

            this.width(width);
            float componentHeight = Math.max(this.block.getHeight(), this.logo.getHeight());
            this.minHeight(componentHeight + componentHeight / 4);
        }

        private Label.LabelStyle getHeaderStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 2);
            return new Label.LabelStyle(labelFont, Color.BLACK);
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 0);
            return new Label.LabelStyle(labelFont, Color.BLACK);
        }


        private static class Logo extends Actor {

            private Sprite sprite;

            public Logo(Sprite sprite) {
                this.sprite = sprite;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                super.draw(batch, parentAlpha);

                float size = PixelLogicUIUtil.getIconBaseHeight();
                float offset = size / 2;
                float y = MathUtils.floor(getY()) + offset - 1;
                float x = MathUtils.floor(getX()) + offset;
                float alpha = parentAlpha * color.a;

                batch.setColor(new Color(color.r, color.g, color.b, color.a * alpha));
                batch.draw(sprite, x, y, size, size);
                batch.setColor(color);
            }

        }


    }

}
