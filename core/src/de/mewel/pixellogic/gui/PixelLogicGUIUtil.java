package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.TEXT_COLOR;

public class PixelLogicGUIUtil {

    public static BitmapFont getNumberFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/FFFFORWA.TTF"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 8;
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
