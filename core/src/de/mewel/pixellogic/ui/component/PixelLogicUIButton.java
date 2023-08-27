package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.concurrent.atomic.AtomicBoolean;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

public abstract class PixelLogicUIButton extends PixelLogicUIGroup {

    private Label label;

    private String text;

    private final PixelLogicUIColoredSurface background;

    private final PixelLogicUIButtonListener listener;

    private final AtomicBoolean blocked;

    public PixelLogicUIButton(PixelLogicGlobal global, String text) {
        super(global);
        this.blocked = new AtomicBoolean(false);
        this.text = text;
        this.background = new PixelLogicUIColoredSurface(global);
        Color buttonColor = getGlobal().getStyle().getSecondaryColor();
        this.background.setColor(buttonColor);
        this.background.setBorder(1, new Color(buttonColor).mul(.5f));
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
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        Color buttonColor = style.getSecondaryColor();
        this.background.setColor(buttonColor);
        this.background.setBorder(1, new Color(buttonColor).mul(.5f));
        this.updateLabel(true);
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
        getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
        handleClick();
    }

    public abstract void handleClick();

    public boolean block() {
        return this.blocked.getAndSet(true);
    }

    public boolean unblock() {
        return this.blocked.getAndSet(false);
    }

    public PixelLogicUIColoredSurface getBackground() {
        return background;
    }

}
