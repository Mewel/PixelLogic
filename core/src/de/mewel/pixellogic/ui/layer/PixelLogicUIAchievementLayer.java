package de.mewel.pixellogic.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.achievements.PixelLogicAchievementEvent;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUIAchievementLayer implements PixelLogicUILayer, PixelLogicListener {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private Queue<PixelLogicAchievement> achievements;

    private PixelLogicAchievement currentDisplayedAchievment;

    private Stage stage;

    private AchievementBlock achievementBlock;

    public PixelLogicUIAchievementLayer(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.eventManager.listen(this);
        this.achievements = new LinkedList<PixelLogicAchievement>();

        this.stage = new Stage();
        this.achievementBlock = new AchievementBlock(assets, eventManager);
        this.stage.addActor(this.achievementBlock);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicAchievementEvent) {
            PixelLogicAchievementEvent achievementEvent = (PixelLogicAchievementEvent) event;
            PixelLogicAchievement achievement = achievementEvent.getAchievement();
            this.achievements.add(achievement);
        }
    }

    @Override
    public void render(float delta) {
        if (this.currentDisplayedAchievment != null) {
            this.stage.act(delta);
            this.stage.draw();
        } else if (!this.achievements.isEmpty()) {
            this.currentDisplayedAchievment = this.achievements.poll();
            this.achievementBlock.setHeader(this.currentDisplayedAchievment.getName());
            this.achievementBlock.setDescription(this.currentDisplayedAchievment.getDescription());



            // this.achievementBlock.addAction(Actions.));
        }
    }

    @Override
    public void resize(int width, int height) {
        this.achievementBlock.setSize(width - (width / 17), height / 10);
    }

    @Override
    public void dispose() {
        this.eventManager.remove(this);
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

    private static class AchievementBlock extends PixelLogicUIGroup {

        private PixelLogicUIColoredSurface background;

        private Table container;

        private Label header, description;

        private String headerText, descriptionText;

        public AchievementBlock(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
            super(assets, eventManager);

            this.background = new PixelLogicUIColoredSurface(assets, eventManager);
            this.background.setColor(Color.TAN);
            this.addActor(this.background);

            this.container = new Table();

            this.headerText = "";
            this.descriptionText = "";
            updateHeader();
            updateDescription();

            container.add(this.header);
            container.row();
            container.add(this.description);

            this.addActor(this.container);
        }

        private Label.LabelStyle getHeaderStyle() {
            BitmapFont labelFont = getAssets().getGameFont((int) (PixelLogicUIUtil.getTextHeight() * 1.3f));
            return new Label.LabelStyle(labelFont, TEXT_COLOR);
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
            return new Label.LabelStyle(labelFont, TEXT_COLOR);
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            this.background.setSize(this.getWidth(), this.getHeight());
            updateHeader();
            updateDescription();
        }

        private void updateHeader() {
            this.header = updateLabel(this.header, this.headerText, getHeaderStyle());
        }

        private void updateDescription() {
            this.description = updateLabel(this.description, this.descriptionText, getDescriptionStyle());
        }

        private Label updateLabel(Label label, String text, Label.LabelStyle style) {
            if (label != null) {
                if (style.font.equals(label.getStyle().font)) {
                    return label;
                }
                this.removeActor(label);
            }
            label = new Label(text, style);
            this.addActor(label);
            return label;
        }

        private void setHeader(String text) {
            this.header.setText(text);
            updateHeader();
        }

        private void setDescription(String text) {
            this.description.setText(text);
            updateDescription();
        }

    }

}
