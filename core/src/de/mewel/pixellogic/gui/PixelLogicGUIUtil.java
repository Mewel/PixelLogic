package de.mewel.pixellogic.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class PixelLogicGUIUtil {

    public static Texture getWhiteTexture() {
        return getTexture(Color.WHITE);
    }

    public static Texture getTexture(Color color) {
        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(color);
        linePixmap.fill();
        return new Texture(linePixmap);
    }

}
