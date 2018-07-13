package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
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
        getPageRoot().setDebug(true);
        getPageRoot().pad(0);
        getPageRoot().padTop(getPadding() / 4);
        getPageRoot().padBottom(getPadding() / 4);
        getPageRoot().space(getSpace() / 8);

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
        for (AchievementContainer achievementContainer : this.achievementContainers) {
            achievementContainer.resize();
        }
    }

    private static class AchievementContainer extends Container<HorizontalGroup> {

        private PixelLogicGlobal global;

        private PixelLogicUIAchievementBlock block;

        AchievementContainer(PixelLogicAchievement achievement, PixelLogicGlobal global) {
            super(new HorizontalGroup());
            this.global = global;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            if (achievement.isDone()) {

            }

            this.block = new PixelLogicUIAchievementBlock(global.getAssets(), global.getEventManager());
            this.block.setAchievement(achievement.getName(), achievement.getDescription());
            this.block.setHeaderStyle(getHeaderStyle());
            this.block.setDescriptionStyle(getDescriptionStyle());
            getActor().addActor(this.block);
            this.block.setDebug(true);

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void resize() {
            this.width(Gdx.graphics.getWidth());
            float padding = getPadding() / 2;
            this.pad(padding, 0, padding, padding * 2);
            this.block.setWidth(getComponentWidth());
            //this.block.setHeight(100);
        }

        private Label.LabelStyle getHeaderStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 2);
            return new Label.LabelStyle(labelFont, Color.BLACK);
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(global.getAssets(), 0);
            return new Label.LabelStyle(labelFont, Color.BLACK);
        }

    }

}
