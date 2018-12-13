package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUITutorialLevelPage extends PixelLogicUILevelPage {

    private int levelAlignment = 1; // center

    public PixelLogicUITutorialLevelPage(PixelLogicGlobal global) {
        super(global);
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        this.levelAlignment = 1;
    }

    /**
     * 0 = top
     * 1 = center == default
     */
    public void alignLevel(int align, float animationDuration) {
        this.levelAlignment = align;
        float targetY = getHeight() - this.levelUI.getHeight() - PixelLogicUIUtil.getToolbarPaddingTop();
        float amountY = targetY - levelUI.getY();
        this.levelUI.addAction(Actions.moveBy(0, amountY, animationDuration));
    }

    protected void updateLevelBounds(int width, int height) {
        int levelMaxHeight = PixelLogicUIUtil.getLevelMaxHeight();
        Vector2 levelSize = PixelLogicUIUtil.get(getLevel()).getLevelSize();
        this.levelUI.setSize(levelSize.x, levelSize.y);
        int toolbarHeightAndPadding = PixelLogicUIUtil.getToolbarHeight() + PixelLogicUIUtil.getToolbarPaddingTop();
        float x = width / 2f - this.levelUI.getWidth() / 2f;
        float y = levelAlignment == 1 ? levelMaxHeight / 2f - this.levelUI.getHeight() / 2f + toolbarHeightAndPadding :
                height - this.levelUI.getHeight() - PixelLogicUIUtil.getToolbarPaddingTop();
        this.levelUI.setPosition((int) x, (int) y);
    }

}
