package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_FONT_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SCALE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIUtil {

    public static BitmapFont getNumberFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ObelusCompact.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = PIXEL_FONT_SIZE * PIXEL_SCALE;
        params.color = TEXT_COLOR;
        params.flip = true;
        try {
            return generator.generateFont(params);
        } finally {
            generator.dispose();
        }
    }

    public static Texture getWhiteTexture() {
        Pixmap linePixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        linePixmap.setColor(Color.WHITE);
        linePixmap.fill();
        return new Texture(linePixmap);
    }

}
