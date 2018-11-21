package de.mewel.pixellogic.ui.level.animation;

import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;


public interface PixelLogicUILevelAnimation {

    PixelLogicUILevelPage getPage();

    void setPage(PixelLogicUILevelPage page);

    PixelLogicUILevel getLevelUI();

    /**
     * Executes the animation.
     *
     * @return the total execution time.
     */
    float execute();

    void destroy();

}
