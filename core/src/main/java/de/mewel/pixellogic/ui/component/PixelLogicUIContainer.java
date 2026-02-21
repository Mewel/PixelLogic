package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIElement;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUIContainer<T extends WidgetGroup> extends Container<T> implements PixelLogicUIElement {

    private final PixelLogicGlobal global;

    private Texture backgroundTexture;

    public PixelLogicUIContainer(PixelLogicGlobal global, T actor) {
        super(actor);
        this.global = global;
        styleChanged(getStyle());
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        if (this.backgroundTexture != null) {
            this.backgroundTexture.dispose();
        }
        this.backgroundTexture = PixelLogicUIUtil.getTexture(getBackgroundColor());
        Sprite backgroundSprite = new Sprite(backgroundTexture);
        this.setBackground(new SpriteDrawable(backgroundSprite));
    }

    public Color getBackgroundColor() {
        return getStyle().getBlockColor();
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

    public PixelLogicUIStyle getStyle() {
        return global.getStyle();
    }

    public PixelLogicGlobal getGlobal() {
        return global;
    }

}
