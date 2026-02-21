package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public abstract class PixelLogicUIText extends Container<HorizontalGroup> implements PixelLogicUIElement {

    protected final PixelLogicGlobal global;

    protected final Label label;

    protected final Container<Label> labelContainer;

    public PixelLogicUIText(PixelLogicGlobal global, final String text) {
        super(new HorizontalGroup());
        this.global = global;

        getActor().setFillParent(true);
        this.label = new Label(text, getLabelStyle());
        this.label.setWrap(true);
        this.labelContainer = new Container<>(label);
        getActor().addActor(this.labelContainer);
        getActor().pack();
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.labelContainer.width(getComponentWidth());
        this.labelContainer.getActor().setStyle(getLabelStyle());
    }

    public Label getLabel() {
        return label;
    }

    protected Label.LabelStyle getLabelStyle() {
        BitmapFont font = PixelLogicUIUtil.getMainFont(getAssets());
        return new Label.LabelStyle(font, Color.WHITE);
    }

    @Override
    public PixelLogicAssets getAssets() {
        return global.getAssets();
    }

    @Override
    public PixelLogicEventManager getEventManager() {
        return global.getEventManager();
    }

    @Override
    public PixelLogicAudio getAudio() {
        return global.getAudio();
    }

    public abstract int getComponentWidth();

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        this.label.setStyle(getLabelStyle());
    }

}
