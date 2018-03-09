package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialFinishedScreen extends PixelLogicUIScreen {

    private Label resultLabel;

    public PixelLogicUITimeTrialFinishedScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        BitmapFont labelFont = getAssets().getGameFont(PixelLogicUIUtil.getBaseHeight() * 2);
        Label.LabelStyle style = new Label.LabelStyle(labelFont, TEXT_COLOR);
        this.resultLabel = new Label("00:00", style);
        getStage().addActor(this.resultLabel);
        centerLabel(this.resultLabel);
    }

    private void centerLabel(Label label) {
        if (label == null) {
            return;
        }
        label.setPosition(getStage().getWidth() / 2 - label.getPrefWidth() / 2, getStage().getHeight() / 2 - label.getPrefHeight() / 2);
    }

    @Override
    public void activate(PixelLogicUIScreenData data) {
        super.activate(data);
        Long time = data.getLong("time");
        this.resultLabel.setText(PixelLogicUIUtil.formatMilliseconds(time));
        centerLabel(this.resultLabel);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        centerLabel(this.resultLabel);
    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

}
