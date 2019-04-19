package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIActor;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUISpriteButton extends PixelLogicUIActor {

    private Sprite sprite;

    public PixelLogicUISpriteButton(PixelLogicGlobal global, int spriteIndex) {
        super(global);
        this.sprite = global.getAssets().getIcon(spriteIndex);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        super.draw(batch, parentAlpha);

        float alpha = parentAlpha * color.a;
        float size = PixelLogicUIUtil.getIconBaseHeight();
        float y = MathUtils.floor(getY()) + getHeight() / 2 - size / 2;
        float x = MathUtils.floor(getX()) + getWidth() / 2 - size / 2;

        sprite.setColor(color.r, color.g, color.b, color.a * alpha);
        sprite.setPosition(x, y);
        sprite.setSize(getWidth(), getHeight());
        sprite.draw(batch);
    }

    public Sprite getSprite() {
        return sprite;
    }

}
