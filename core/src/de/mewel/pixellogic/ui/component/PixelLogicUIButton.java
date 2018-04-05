package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    public PixelLogicUIButton(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text) {
        super(assets, eventManager);
        this.text = text;
        this.background = new PixelLogicUIColoredSurface(assets, eventManager);
        this.background.setColor(PixelLogicUIConstants.LINE_COMPLETE_COLOR);
        this.addActor(this.background);
        this.updateLabel();
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
        BitmapFont labelFont = getAssets().getGameFont((PixelLogicUIUtil.getBaseHeight() / 3) * 2);
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
        super.clear();
    }

}
