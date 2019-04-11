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
import com.badlogic.gdx.utils.viewport.ExtendViewport;

import java.util.LinkedList;
import java.util.Queue;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.achievements.PixelLogicAchievement;
import de.mewel.pixellogic.achievements.PixelLogicAchievementEvent;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIAchievementBlock;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.PixelLogicConstants.MAIN_COLOR;

public class PixelLogicUIAchievementLayer implements PixelLogicUILayer, PixelLogicListener {

    private PixelLogicGlobal global;

    private Queue<PixelLogicAchievement> achievements;

    private PixelLogicAchievement currentDisplayedAchievment;

    private Stage stage;

    private PixelLogicUIAchievementBlock achievementBlock;

    public PixelLogicUIAchievementLayer(PixelLogicGlobal global) {
        this.global = global;
        this.getEventManager().listen(this);
        this.achievements = new LinkedList<PixelLogicAchievement>();

        this.stage = new Stage();
        this.achievementBlock = new PixelLogicUIAchievementBlock(global);
        this.achievementBlock.setHeaderStyle(getHeaderStyle());
        this.achievementBlock.setDescriptionStyle(getDescriptionStyle());

        PixelLogicUIColoredSurface background = new PixelLogicUIColoredSurface(global);
        background.setColor(new Color(MAIN_COLOR));
        background.setBorder(1, new Color(MAIN_COLOR).mul(.5f));
        this.achievementBlock.setBackground(background);

        this.stage.addActor(this.achievementBlock);
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
        this.resizeAchievementBlock(false);
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
        this.resizeAchievementBlock(this.currentDisplayedAchievment != null);
    }

    private void resizeAchievementBlock(boolean visible) {
        this.achievementBlock.setHeaderStyle(getHeaderStyle());
        this.achievementBlock.setDescriptionStyle(getDescriptionStyle());

        int width = this.stage.getViewport().getScreenWidth();
        int padding = getPadding();
        int blockWidth = width - (padding * 2);
        float bestBlockHeight = this.achievementBlock.getPrefHeight();
        float minBlockHeight = Gdx.graphics.getHeight() / 10f;
        int blockHeight = (int) Math.max(minBlockHeight, bestBlockHeight);
        int blockY = visible ? padding : -blockHeight;
        this.achievementBlock.setBounds(padding, blockY, blockWidth, blockHeight);
        this.achievementBlock.getContainer().pad(width / 72);
    }

    private int getPadding() {
        return Gdx.graphics.getWidth() / 64;
    }

    private Label.LabelStyle getHeaderStyle() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        return new Label.LabelStyle(labelFont, Color.WHITE);
    }

    private Label.LabelStyle getDescriptionStyle() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 0);
        return new Label.LabelStyle(labelFont, Color.WHITE);
    }

    private void updateViewport(int width, int height) {
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false);
        stage.setViewport(new ExtendViewport(width, height, camera));
    }

    @Override
    public void dispose() {
        this.getEventManager().remove(this);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public PixelLogicAssets getAssets() {
        return global.getAssets();
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return global.getEventManager();
    }

    @Override
    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

}
