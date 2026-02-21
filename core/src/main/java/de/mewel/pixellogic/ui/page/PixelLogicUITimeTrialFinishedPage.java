package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUITimeTrialFinishedPage extends PixelLogicUIPage {

    private final Label resultLabel;

    public PixelLogicUITimeTrialFinishedPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.timeTrialFinished);
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 2);
        Label.LabelStyle style = new Label.LabelStyle(labelFont, new Color(global.getStyle().getTextColor()));
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
        final PixelLogicTimeTrialModeOptions.Mode mode = properties.get("mode");
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
                PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
                data.put("mode", mode);
                data.put("time", time);
                data.put("rank", rank);
                getAppScreen().setPage(PixelLogicUIPageId.timeTrial, data);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        centerLabel(this.resultLabel);
    }

}
