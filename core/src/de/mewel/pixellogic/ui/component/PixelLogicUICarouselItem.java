package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Texture;

public class PixelLogicUICarouselItem {

    private String label;

    private Texture texture;

    public PixelLogicUICarouselItem(String label, Texture texture) {
        this.label = label;
        this.texture = texture;
    }

    public String getLabel() {
        return label;
    }

    public Texture getTexture() {
        return texture;
    }

}
