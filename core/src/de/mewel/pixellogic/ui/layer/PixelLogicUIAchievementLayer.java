package de.mewel.pixellogic.ui.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.LinkedList;
import java.util.Queue;

import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.achievements.PixelLogicAchievementEvent;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIAchievementBlock;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_BLOCKED_COLOR;

public class PixelLogicUIAchievementLayer implements PixelLogicUILayer, PixelLogicListener {

    private PixelLogicAssets assets;

    private PixelLogicEventManager eventManager;

    private Queue<PixelLogicAchievement> achievements;

    private PixelLogicAchievement currentDisplayedAchievment;

    private Stage stage;

    private PixelLogicUIAchievementBlock achievementBlock;

    public PixelLogicUIAchievementLayer(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        this.assets = assets;
        this.eventManager = eventManager;
        this.eventManager.listen(this);
        this.achievements = new LinkedList<PixelLogicAchievement>();

        this.stage = new Stage();
        this.achievementBlock = new PixelLogicUIAchievementBlock(assets, eventManager);
        this.achievementBlock.setHeaderStyle(getHeaderStyle());
        this.achievementBlock.setDescriptionStyle(getDescriptionStyle());

        PixelLogicUIColoredSurface background = new PixelLogicUIColoredSurface(assets);
        Color bgColor = PIXEL_BLOCKED_COLOR;
        background.setColor(PIXEL_BLOCKED_COLOR);
        background.setBorder(1, new Color(bgColor).mul(.5f));
        this.achievementBlock.setBackground(background);

        this.stage.addActor(this.achievementBlock);
    }

    private Label.LabelStyle getHeaderStyle() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 2);
        return new Label.LabelStyle(labelFont, Color.WHITE);
    }

    private Label.LabelStyle getDescriptionStyle() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 0);
        return new Label.LabelStyle(labelFont, Color.WHITE);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicAchievementEvent) {
            PixelLogicAchievementEvent achievementEvent = (PixelLogicAchievementEvent) event;
            PixelLogicAchievement achievement = achievementEvent.getAchievement();
            this.achievements.add(achievement);
            Gdx.app.log("achievement layer", "added " + achievement);
        }
    }

    @Override
    public void render(float delta) {
        if (this.currentDisplayedAchievment != null) {
            this.stage.act(delta);
            this.stage.draw();
        } else if (!this.achievements.isEmpty()) {
            next();
        }
    }

    private void next() {
        this.currentDisplayedAchievment = this.achievements.poll();
        this.achievementBlock.setAchievement(this.currentDisplayedAchievment.getName(),
                this.currentDisplayedAchievment.getDescription());
        int padding = getPadding();
        MoveToAction moveIn = Actions.moveTo(padding, padding, .2f);
        DelayAction delay = Actions.delay(7f);
        MoveToAction moveOut = Actions.moveTo(padding, -this.achievementBlock.getHeight(), .2f);
        RunnableAction onEnd = Actions.run(new Runnable() {
            @Override
            public void run() {
                PixelLogicUIAchievementLayer.this.currentDisplayedAchievment = null;
            }
        });
        Action sequenceAction = Actions.sequence(moveIn, delay, moveOut, onEnd);
        this.achievementBlock.addAction(sequenceAction);
    }

    @Override
    public void resize(int width, int height) {
        this.updateViewport(width, height);
        this.stage.getViewport().update(width, height);
        int padding = getPadding();
        this.achievementBlock.setWidth(width - (padding * 2));
        if (this.currentDisplayedAchievment != null) {
            this.achievementBlock.setPosition(padding, padding);
        } else {
            this.achievementBlock.setPosition(padding, -this.achievementBlock.getHeight());
        }
    }

    private int getPadding() {
        return Gdx.graphics.getWidth() / 64;
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
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public PixelLogicAssets getAssets() {
        return assets;
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return eventManager;
    }

}
