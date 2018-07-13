package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.PIXEL_BLOCKED_COLOR;

public class PixelLogicUIAchievementBlock extends PixelLogicUIGroup {

    private PixelLogicUIColoredSurface background;

    private VerticalGroup container;

    private Label header, description;

    private String headerText, descriptionText;

    private Label.LabelStyle headerStyle, descriptionStyle;

    public PixelLogicUIAchievementBlock(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        this.container = new VerticalGroup();
        this.container.left();
        this.container.top();
        this.container.grow();
        this.container.setFillParent(true);

        this.headerText = null;
        this.descriptionText = null;

        this.addActor(this.container);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if (this.background != null) {
            this.background.setSize(this.getWidth(), this.getHeight());
        }
    }

    protected void updateContainer() {
        this.container.clearChildren();
        this.container.pad(getPadding());

        if (this.headerText != null && headerStyle != null) {
            this.header = new Label(this.headerText, headerStyle);
            this.header.setWrap(true);
            this.container.addActor(this.header);
            fixLabelHeight(this.header);
        }
        if (this.descriptionText != null && descriptionStyle != null) {
            this.description = new Label(this.descriptionText, descriptionStyle);
            this.description.setWrap(true);
            this.container.addActor(description);
            fixLabelHeight(this.description);
        }
        if (header != null && description != null) {
            updateHeight();
        }
    }

    private void updateHeight() {
        float bestHeight = header.getPrefHeight() + description.getPrefHeight() + getPadding() * 2f;
        float minHeight = Gdx.graphics.getHeight() / 10f;
        float height = Math.max(minHeight, bestHeight);
        this.setY(-height);
        this.setHeight(height);
    }

    public void setAchievement(String header, String description) {
        this.headerText = header;
        this.descriptionText = description;
        this.updateContainer();
    }

    public void setHeaderStyle(Label.LabelStyle headerStyle) {
        this.headerStyle = headerStyle;
    }

    public void setDescriptionStyle(Label.LabelStyle descriptionStyle) {
        this.descriptionStyle = descriptionStyle;
    }

    public void setBackground(PixelLogicUIColoredSurface background) {
        this.background = background;
        this.addActorAt(0, this.background);
    }

    public float getPadding() {
        return Gdx.graphics.getWidth() / 72;
    }

    private void fixLabelHeight(Label label) {
        PixelLogicUIUtil.fixLabelHeight(label, this.getWidth() - (getPadding() * 2));
    }

}

