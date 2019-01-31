package de.mewel.pixellogic;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Colors;

public abstract class PixelLogicConstants {

    public static int BASE_SIZE = 24;

    public static final Color MAIN_COLOR = Color.valueOf("#FA5882");
    public static final Color SECONDARY_COLOR = Color.valueOf("#FF9411");

    public static final Color APP_BACKGROUND = Color.valueOf("#f8f8f8");
    public static final Color BLOCK_COLOR = Color.valueOf("#fefefe");

    public static final Color PIXEL_EMPTY_COLOR = Color.valueOf("#CEE3F6");
    public static final Color PIXEL_FILLED_COLOR = Color.valueOf("#1C1C1C");
    public static final Color PIXEL_BLOCKED_COLOR = new Color(MAIN_COLOR);
    public static final Color LINE_COLOR = Color.valueOf("#F5DA81");
    public static final Color LINE_COMPLETE_COLOR = new Color(SECONDARY_COLOR);
    public static final Color TEXT_COLOR = Color.valueOf("#424242");
    public static final Color TEXT_LIGHT_COLOR = Color.valueOf("#A2A2A2");
    public static final Color GRID_COLOR = Color.valueOf("#8DBEEA");

    public static final Color TOOLBAR_SWITCHER_ACTIVE_COLOR = new Color(Color.WHITE);
    public static final Color TOOLBAR_SWITCHER_INACTIVE_COLOR = new Color(Color.DARK_GRAY);

    public static final String BUTTON_SOUND = "audio/164643__adam-n__pen-click-3.wav";
    public static final String DRAW_SOUND = "audio/119415__joedeshon__rocker-switch.wav";
    public static final String BLOCK_SOUND = "audio/63532__florian-reinke__click2.wav";
    public static final String SWITCHER_SOUND = "audio/134712__joedeshon__push-button-switch-04.wav";
    public static final String PUZZLE_SOLVED_SOUND = "audio/258142__tuudurt__level-win.wav";
    public static final String KEY_SOUND = "audio/378083__bigmonmulgrew__mechanical-key-soft.wav";

    public static final String MENU_MUSIC = "audio/tobias_weber_Between_Worlds.mp3";
    public static final String LEVEL_MUSIC = "audio/tobias_weber_The_Parting_Glass.mp3";

    public static final float BUTTON_SOUND_VOLUME = 1f;
    public static final float DRAW_SOUND_VOLUME = .3f;
    public static final float BLOCK_SOUND_VOLUME = .5f;
    public static final float SWITCHER_SOUND_VOLUME = .3f;
    public static final float PUZZLE_SOLVED_SOUND_VOLUME = .2f;
    public static final float KEY_SOUND_VOLUME = .2f;

    public static final float MENU_MUSIC_VOLUME = .4f;
    public static final float LEVEL_MUSIC_VOLUME = .4f;

    public static final String GAME_FONT = "fonts/visitor3.otf";
    public static final String LEVEL_FONT = "fonts/numbers.fnt";
    public static final String MAIN_FONT = "fonts/Quicksand-Regular.ttf";
    public static final int MAIN_FONT_PT = 22;

    static {
        Colors.put("MAIN_COLOR", MAIN_COLOR);
        Colors.put("SECONDARY_COLOR", SECONDARY_COLOR);
        Colors.put("TEXT_COLOR", TEXT_COLOR);
        Colors.put("TEXT_LIGHT_COLOR", TEXT_LIGHT_COLOR);
        Colors.put("LINE_COLOR", LINE_COLOR);
        Colors.put("LINE_COMPLETE_COLOR", LINE_COMPLETE_COLOR);
    }

}
