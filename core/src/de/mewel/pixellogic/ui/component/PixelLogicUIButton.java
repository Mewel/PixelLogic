package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.concurrent.atomic.AtomicBoolean;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public abstract class PixelLogicUIButton extends PixelLogicUIGroup {

    private Label label;

    private String text;

    private PixelLogicUIColoredSurface background;

    private PixelLogicUIButtonListener listener;

    private AtomicBoolean blocked;

    public PixelLogicUIButton(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text) {
        super(assets, eventManager);
        this.blocked = new AtomicBoolean(false);
        this.text = text;
        this.background = new PixelLogicUIColoredSurface(assets);
        Color bgColor = PixelLogicConstants.SECONDARY_COLOR;
        this.background.setColor(bgColor);
        this.background.setBorder(1, new Color(bgColor).mul(.5f));
        this.addActor(this.background);
        this.updateLabel(false);
        this.addListener(this.listener = new PixelLogicUIButtonListener() {
            @Override
            public void onClick() {
                PixelLogicUIButton.this.onClick();
            }
        });
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.background.setSize(this.getWidth(), this.getHeight());
        updateLabel(false);
    }

    public void setText(String text) {
        this.text = text;
        updateLabel(true);
    }

    private void updateLabel(boolean force) {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        if (this.label != null) {
            if (!force && labelFont.equals(this.label.getStyle().font)) {
                return;
            }
            this.removeActor(this.label);
        }
        Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
        this.label = new Label(this.text, style);
        this.addActor(this.label);
        this.updateLabelPosition();
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        this.updateLabelPosition();
    }

    private void updateLabelPosition() {
        float x = this.getWidth() / 2 - this.label.getPrefWidth() / 2;
        float y = this.getHeight() / 2 - this.label.getPrefHeight() / 2;
        this.label.setPosition(x, y);
    }

    @Override
    public void clear() {
        Gdx.app.log("button", "clear() called");
        this.background.clear();
        this.removeListener(this.listener);
        super.clear();
    }

    public void onClick() {
        if (block()) {
            return;
        }
        PixelLogicUIUtil.playButtonSound(getAssets());
        handleClick();
    }

    public abstract void handleClick();

    public boolean block() {
        return this.blocked.getAndSet(true);
    }

    public boolean unblock() {
        return this.blocked.getAndSet(false);
    }

}
