package de.mewel.pixellogic.ui.style;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Colors;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class PixelLogicUIStyleController {

    public static final String LIGHT_STYLE = "light";

    public static final String DARK_STYLE = "dark";

    public PixelLogicUIStyle activeStyle;

    public void init() {
        String styleString = getPreferences().getString("style", LIGHT_STYLE);
        PixelLogicUIStyle style;
        if (styleString.equals(LIGHT_STYLE)) {
            style = new PixelLogicUILightStyle();
        } else if (styleString.equals(DARK_STYLE)) {
            style = new PixelLogicUIDarkStyle();
        } else {
            throw new GdxRuntimeException("Unknown color style: " + styleString);
        }
        set(style);
    }

    public PixelLogicUIStyle get() {
        return activeStyle;
    }

    private static Preferences getPreferences() {
        return Gdx.app.getPreferences("pixellogic_settings");
    }

    public PixelLogicUIStyle switchStyle() {
        set(this.activeStyle.getName().equals(LIGHT_STYLE)
                ? new PixelLogicUIDarkStyle() : new PixelLogicUILightStyle());
        return activeStyle;
    }

    public void set(PixelLogicUIStyle style) {
        getPreferences().putString("style", style.getName()).flush();
        updateColors(style);
        activeStyle = style;
    }

    private static void updateColors(PixelLogicUIStyle style) {
        Colors.put("MAIN_COLOR", style.getMainColor());
        Colors.put("SECONDARY_COLOR", style.getSecondaryColor());
        Colors.put("TEXT_COLOR", style.getTextColor());
        Colors.put("TEXT_LIGHT_COLOR", style.getTextSecondaryColor());
        Colors.put("LINE_COLOR", style.getLineColor());
        Colors.put("LINE_COMPLETE_COLOR", style.getLineCompleteColor());
    }

}
