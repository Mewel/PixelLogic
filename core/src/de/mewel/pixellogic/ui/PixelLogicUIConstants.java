package de.mewel.pixellogic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public abstract class PixelLogicUIConstants {

    public static int BASE_SIZE = 24;

    public static final Color MAIN_COLOR = Color.valueOf("#FA5882");
    public static final Color SECONDARY_COLOR = Color.valueOf("#FF8000");

    public static final Color APP_BACKGROUND = Color.valueOf("#f8f8f8");
    public static final Color BLOCK_COLOR = Color.valueOf("#fefefe");

    public static final Color PIXEL_EMPTY_COLOR = Color.valueOf("#CEE3F6");
    public static final Color PIXEL_FILLED_COLOR = Color.valueOf("#1C1C1C");
    public static final Color PIXEL_BLOCKED_COLOR = MAIN_COLOR;
    public static final Color LINE_COLOR = Color.valueOf("#F5DA81");
    public static final Color LINE_COMPLETE_COLOR = SECONDARY_COLOR;
    public static final Color TEXT_COLOR = Color.valueOf("#424242");
    public static final Color TEXT_LIGHT_COLOR = Color.valueOf("#A2A2A2");
    public static final Color GRID_COLOR = Color.valueOf("#8DBEEA");

    public static final Color TOOLBAR_SWITCHER_ACTIVE_COLOR = Color.WHITE;
    public static final Color TOOLBAR_SWITCHER_INACTIVE_COLOR = Color.DARK_GRAY;

    static {
        Colors.put("MAIN_COLOR", MAIN_COLOR);
        Colors.put("SECONDARY_COLOR", SECONDARY_COLOR);
        Colors.put("TEXT_COLOR", TEXT_COLOR);
    }

}
