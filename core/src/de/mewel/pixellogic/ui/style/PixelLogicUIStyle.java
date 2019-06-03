package de.mewel.pixellogic.ui.style;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import de.mewel.pixellogic.asset.PixelLogicAssets;

public interface PixelLogicUIStyle {

    String getName();

    Color getMainColor();

    Color getSecondaryColor();

    Color getBackgroundColor();

    Color getBlockColor();

    Color getPixelEmptyColor();

    Color getPixelFilledColor();

    Color getPixelBlockedColor();

    Color getLineColor();

    Color getLineCompleteColor();

    Color getGridColor();

    Color getTextColor();

    Color getTextSecondaryColor();

    Color getMainMenuButtonGroupColor();

    Color getMainMenuBackdropColor();

    Texture getLevelBackgroundTexture(PixelLogicAssets assets);

}
