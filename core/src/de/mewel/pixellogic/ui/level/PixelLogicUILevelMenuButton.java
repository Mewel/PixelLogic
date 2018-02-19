package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BASE_SIZE;

public class PixelLogicUILevelMenuButton extends Actor {

    private Sprite sprite;

    public PixelLogicUILevelMenuButton(Texture icons) {
        this.sprite = new Sprite(icons, BASE_SIZE * 2, 0, BASE_SIZE, BASE_SIZE);
        this.sprite.flip(false, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);

        float size = PixelLogicUIUtil.getIconBaseHeight();
        float offset = size / 2;
        float y = MathUtils.floor(getY()) + offset - 1;
        float x = MathUtils.floor(getX()) + offset;
        float alpha = parentAlpha * color.a;
        Color spriteColor = Color.WHITE;

        batch.setColor(new Color(spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a * alpha));
        batch.draw(sprite, x, y, size, size);
        batch.setColor(color);
    }

}
