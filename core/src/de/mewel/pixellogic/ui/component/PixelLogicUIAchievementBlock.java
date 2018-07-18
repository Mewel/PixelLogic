package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

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

    public void updateContainer() {
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
    }

    public void setAchievement(String header, String description) {
        this.headerText = header;
        this.descriptionText = description;
        this.updateContainer();
    }

    public void setHeaderStyle(Label.LabelStyle headerStyle) {
        if (PixelLogicUIUtil.equalLabelStyle(this.headerStyle, headerStyle)) {
            return;
        }
        this.headerStyle = headerStyle;
        updateContainer();
    }

    public void setDescriptionStyle(Label.LabelStyle descriptionStyle) {
        if (PixelLogicUIUtil.equalLabelStyle(this.descriptionStyle, descriptionStyle)) {
            return;
        }
        this.descriptionStyle = descriptionStyle;
        updateContainer();
    }

    public void setBackground(PixelLogicUIColoredSurface background) {
        this.background = background;
        this.addActorAt(0, this.background);
        updateContainer();
    }

    public float getPadding() {
        return this.getWidth() / 72;
    }

    private void fixLabelHeight(Label label) {
        PixelLogicUIUtil.fixLabelHeight(label, this.getWidth() - (getPadding() * 2));
    }

    public float getPrefHeight() {
        if (header == null || description == null) {
            return 0f;
        }
        return header.getPrefHeight() + description.getPrefHeight() + getPadding() * 2f;
    }

}

