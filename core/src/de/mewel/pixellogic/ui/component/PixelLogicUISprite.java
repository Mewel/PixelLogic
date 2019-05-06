package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIActor;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUISprite extends PixelLogicUIActor {

    protected Sprite sprite;

    protected int padding;

    protected boolean useDefaultIconSize;

    public PixelLogicUISprite(PixelLogicGlobal global) {
        super(global);
        this.padding = 0;
        this.useDefaultIconSize = true;
    }

    public PixelLogicUISprite(PixelLogicGlobal global, int spriteIndex) {
        super(global);
        this.sprite = global.getAssets().getIcon(spriteIndex);
        this.padding = 0;
        this.useDefaultIconSize = true;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void pad(int padding) {
        int newWidth = (int) (getWidth() + (padding * 2));
        int newHeight = (int) (getHeight() + (padding * 2));
        this.padding = padding;
        this.setSize(newWidth, newHeight);
    }

    public void setUseDefaultIconSize(boolean useDefaultIconSize) {
        this.useDefaultIconSize = useDefaultIconSize;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        draw(batch, parentAlpha, getSprite());
    }

    private void draw(Batch batch, float parentAlpha, Sprite sprite) {
        if (sprite == null) {
            return;
        }
        Color color = getColor();
        float alpha = parentAlpha * color.a;
        int iconWidth = useDefaultIconSize ? PixelLogicUIUtil.getIconBaseHeight() : (int) getWidth();
        int iconHeight = useDefaultIconSize ? PixelLogicUIUtil.getIconBaseHeight() : (int) getHeight();
        float y = MathUtils.floor(getY()) + getHeight() / 2 - iconWidth / 2;
        float x = MathUtils.floor(getX()) + getWidth() / 2 - iconHeight / 2;

        sprite.setColor(color.r, color.g, color.b, color.a * alpha);
        sprite.setPosition(x, y);
        sprite.setSize(getWidth() - this.padding * 2, getHeight() - this.padding * 2);
        sprite.draw(batch);
    }

}
