package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIButton extends PixelLogicUIGroup {

    private Label label;

    private String text;

    private PixelLogicUIColoredSurface background;

    private ButtonListener listener;

    public PixelLogicUIButton(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text) {
        super(assets, eventManager);
        this.text = text;
        this.background = new PixelLogicUIColoredSurface(assets, eventManager);
        Color bgColor = PixelLogicUIConstants.LINE_COMPLETE_COLOR;
        this.background.setColor(bgColor);
        this.background.setBorder(1, new Color(bgColor).mul(.5f));
        this.addActor(this.background);
        this.updateLabel();
        this.addListener(this.listener = new ButtonListener());
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.background.setSize(this.getWidth(), this.getHeight());
        updateLabel();
    }

    public void setText(String text) {
        this.text = text;
        updateLabel();
    }

    private void updateLabel() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 2);
        if (this.label != null) {
            if (labelFont.equals(this.label.getStyle().font)) {
                return;
            }
            this.removeActor(this.label);
        }
        Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
        this.label = new Label(this.text, style);
        this.addActor(this.label);
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        float x = this.getWidth() / 2 - this.label.getPrefWidth() / 2;
        float y = this.getHeight() / 2 - this.label.getPrefHeight() / 2;
        this.label.setPosition(x, y);
    }

    @Override
    public void clear() {
        this.background.clear();
        this.removeListener(this.listener);
        super.clear();
    }

    public void onClick() {
        // not implemented
    }

    private class ButtonListener extends InputListener {

        private Vector2 start;

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            this.start = new Vector2(x, y);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            int maxDragUntilCancel = Math.min(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()) / 20;
            if (start.dst(new Vector2(x, y)) < maxDragUntilCancel) {
                onClick();
            }
        }

    }

}
