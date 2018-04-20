package de.mewel.pixellogic.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

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
import de.mewel.pixellogic.ui.page.PixelLogicUITimeTrialPage;

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

            Gdx.app.log("achievment layer", "added " + achievement);
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
        this.updateViewport(width, height);
        int padding = width / 64;
        this.stage.getViewport().update(width, height);
        this.achievementBlock.setSize(width - (padding * 2), height / 10);
        this.achievementBlock.setPosition(padding, padding);
    }

    private void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        stage.setViewport(new ExtendViewport(width, height, camera));
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

        private VerticalGroup container;

        private Container descriptionContainer;

        private Label header, description;

        private String headerText, descriptionText;

        public AchievementBlock(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
            super(assets, eventManager);

            this.background = new PixelLogicUIColoredSurface(assets, eventManager);
            this.background.setColor(Color.TAN);
            this.addActor(this.background);

            this.container = new VerticalGroup();
            this.container.left();
            //this.container.top();
            this.container.pad(getPadding());
            this.container.setFillParent(true);

            this.headerText = "";
            this.descriptionText = "";
            updateHeader();
            updateDescription();

            this.header = new Label("HARD", getHeaderStyle());
            this.description = new Label("tesxt dasdas dasd as", getDescriptionStyle());

            //this.descriptionContainer = new Container<Label>(description);
            //description.setFillParent(true);
            //description.setPosition(0, 0);
            //description.setDebug(true);
            //this.descriptionContainer.setDebug(true);


            container.addActor(this.header);
            container.addActor(this.description);

            this.addActor(this.container);

            container.setDebug(true);
        }

        private Label.LabelStyle getHeaderStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets());
            return new Label.LabelStyle(labelFont, TEXT_COLOR);
        }

        private Label.LabelStyle getDescriptionStyle() {
            BitmapFont labelFont = PixelLogicUIUtil.getSmallAppFont(getAssets());
            return new Label.LabelStyle(labelFont, TEXT_COLOR);
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            this.container.pad(getPadding());
            this.background.setSize(this.getWidth(), this.getHeight());
            //this.descriptionContainer.setWidth(getWidth());
            //updateHeader();
            //updateDescription();
        }

        private void updateHeader() {
            //this.header = updateLabel(this.header, this.headerText, getHeaderStyle());
        }

        private void updateDescription() {
            //this.description = updateLabel(this.description, this.descriptionText, getDescriptionStyle());
            //this.description.setWrap(true);
        }

        private Label updateLabel(Label label, String text, Label.LabelStyle style) {
            if (label != null) {
                if (style.font.equals(label.getStyle().font)) {
                    return label;
                }
                //this.removeActor(label);
                return label;
            }
            return new Label(text, style);
        }

        private void setHeader(String text) {
            //this.header.setText(text);
            //updateHeader();
        }

        private void setDescription(String text) {
            //this.description.setText(text);
            //updateDescription();
        }

        public float getPadding() {
            return Gdx.graphics.getWidth() / 32;
        }
    }

}
