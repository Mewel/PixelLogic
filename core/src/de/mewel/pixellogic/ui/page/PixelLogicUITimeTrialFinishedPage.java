package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialFinishedPage extends PixelLogicUIPage {

    private Label resultLabel;

    public PixelLogicUITimeTrialFinishedPage(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 10);
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
    public void activate(final PixelLogicUIPageProperties properties) {
        super.activate(properties);
        final String mode = properties.getString("mode");
        final Long time = properties.getLong("time");
        final Integer rank = properties.getInt("rank");
        this.resultLabel.setText(PixelLogicUIUtil.formatMilliseconds(time));
        centerLabel(this.resultLabel);

        this.getStage().addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUIPageProperties changeScreenData = new PixelLogicUIPageProperties();
                changeScreenData.put("pageId", PixelLogicUIPageId.timeTrial);
                changeScreenData.put("mode", mode);
                changeScreenData.put("time", time);
                changeScreenData.put("rank", rank);
                getEventManager().fire(new PixelLogicUIPageChangeEvent(PixelLogicUITimeTrialFinishedPage.this, changeScreenData));
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        centerLabel(this.resultLabel);
    }

}
