package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.ui.PixelLogicGUIConstants;
import de.mewel.pixellogic.ui.level.PixelLogicGUILevelResolutionManager;

public class PixelLogicUIButton extends Group {

    private Label label;

    private String text;

    private PixelLogicUIColoredSurface background;

    public PixelLogicUIButton(String text) {
        this.text = text;
        this.background = new PixelLogicUIColoredSurface(PixelLogicGUIConstants.LINE_COLOR);
        this.addActor(this.background);
        this.updateLabel();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.background.setSize(this.getWidth(), this.getHeight());
        updateLabel();
    }

    private void updateLabel() {
        PixelLogicGUILevelResolutionManager resolutionManager = PixelLogicGUILevelResolutionManager.instance();
        BitmapFont labelFont = resolutionManager.getFont(resolutionManager.getBaseHeight(), Color.WHITE);
        if(this.label != null) {
            if(labelFont.equals(this.label.getStyle().font)) {
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
        super.clear();
        this.background.clear();
        this.label.clear();
    }

}
