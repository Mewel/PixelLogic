package de.mewel.pixellogic.ui.layer;

import de.mewel.pixellogic.ui.PixelLogicUIElement;

public interface PixelLogicUILayer extends PixelLogicUIElement {

    void render(float delta);

    void resize(int width, int height);

    void dispose();

}
