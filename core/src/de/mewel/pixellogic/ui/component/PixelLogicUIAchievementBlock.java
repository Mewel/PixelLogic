package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIAchievementBlock extends PixelLogicUIGroup {

    private PixelLogicUIColoredSurface background;

    private final VerticalGroup container;

    private Label header, description;

    private String headerText, descriptionText;

    private Label.LabelStyle headerStyle, descriptionStyle;

    public PixelLogicUIAchievementBlock(PixelLogicGlobal global) {
        super(global);

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
        this.updateContainer();
    }

    public void updateContainer() {
        this.container.clearChildren();
        if (this.headerText != null && headerStyle != null) {
            this.header = new Label(this.headerText, headerStyle);
            this.header.setWrap(true);
            this.container.addActor(this.header);
            fixLabelHeight(this.header);
        }
        if (this.descriptionText != null && descriptionStyle != null) {
            this.description = new Label(this.descriptionText, descriptionStyle);
            this.description.setWrap(true);
            this.description.setAlignment(Align.topLeft);
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

    private void fixLabelHeight(Label label) {
        int padding = (int) (this.container.getPadLeft() + this.container.getPadRight());
        PixelLogicUIUtil.fixLabelHeight(label, this.getWidth() - padding);
    }

    public float getPrefHeight() {
        if (header == null || description == null) {
            return 0f;
        }
        int padding = (int) (this.container.getPadTop() + this.container.getPadBottom());
        return header.getPrefHeight() + description.getPrefHeight() + padding;
    }

    public VerticalGroup getContainer() {
        return container;
    }

}

