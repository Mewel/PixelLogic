package de.mewel.pixellogic.ui.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.mewel.pixellogic.asset.PixelLogicAssets;

public class PixelLogicUILightStyle implements PixelLogicUIStyle {

    private static final Color MAIN_COLOR = Color.valueOf("#FA5882");
    private static final Color SECONDARY_COLOR = Color.valueOf("#FF9411");

    private static final Color BACKGROUND_COLOR = Color.valueOf("#f8f8f8");
    private static final Color BLOCK_COLOR = Color.valueOf("#fefefe");

    private static final Color PIXEL_EMPTY_COLOR = Color.valueOf("#CEE3F6");
    private static final Color PIXEL_FILLED_COLOR = Color.valueOf("#1C1C1C");
    private static final Color PIXEL_BLOCKED_COLOR = new Color(MAIN_COLOR);
    private static final Color LINE_COLOR = Color.valueOf("#F5DA81");
    private static final Color LINE_COMPLETE_COLOR = new Color(SECONDARY_COLOR);
    private static final Color TEXT_COLOR = Color.valueOf("#424242");
    private static final Color TEXT_SECONDARY_COLOR = Color.valueOf("#A2A2A2");
    private static final Color GRID_COLOR = Color.valueOf("#8DBEEA");

    private static final Color MAIN_MENU_BUTTONGROUP_COLOR = new Color(1f, 1f, 1f, .5f);
    private static final Color MAIN_MENU_BACKDROP_COLOR = new Color(.95f, .95f, .95f, .5f);

    @Override
    public String getName() {
        return "light";
    }

    @Override
    public Color getMainColor() {
        return new Color(MAIN_COLOR);
    }

    @Override
    public Color getSecondaryColor() {
        return new Color(SECONDARY_COLOR);
    }

    @Override
    public Color getBackgroundColor() {
        return new Color(BACKGROUND_COLOR);
    }

    @Override
    public Color getBlockColor() {
        return new Color(BLOCK_COLOR);
    }

    @Override
    public Color getPixelEmptyColor() {
        return new Color(PIXEL_EMPTY_COLOR);
    }

    @Override
    public Color getPixelFilledColor() {
        return new Color(PIXEL_FILLED_COLOR);
    }

    @Override
    public Color getPixelBlockedColor() {
        return new Color(PIXEL_BLOCKED_COLOR);
    }

    @Override
    public Color getLineColor() {
        return new Color(LINE_COLOR);
    }

    @Override
    public Color getLineCompleteColor() {
        return new Color(LINE_COMPLETE_COLOR);
    }

    @Override
    public Color getGridColor() {
        return new Color(GRID_COLOR);
    }

    @Override
    public Color getTextColor() {
        return new Color(TEXT_COLOR);
    }

    @Override
    public Color getTextSecondaryColor() {
        return new Color(TEXT_SECONDARY_COLOR);
    }

    @Override
    public Color getMainMenuButtonGroupColor() {
        return new Color(MAIN_MENU_BUTTONGROUP_COLOR);
    }

    @Override
    public Color getMainMenuBackdropColor() {
        return MAIN_MENU_BACKDROP_COLOR;
    }

    @Override
    public Texture getLevelBackgroundTexture(PixelLogicAssets assets) {
        return assets.get().get("background/light.png", Texture.class);
    }

}
