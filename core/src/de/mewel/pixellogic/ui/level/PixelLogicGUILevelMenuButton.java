package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TOOLBAR_ICON_SIZE;

public class PixelLogicGUILevelMenuButton extends Actor {

    private Sprite sprite;

    public PixelLogicGUILevelMenuButton(Texture icons) {
        this.sprite = new Sprite(icons, TOOLBAR_ICON_SIZE * 2, 0, TOOLBAR_ICON_SIZE, TOOLBAR_ICON_SIZE);
        this.sprite.flip(false, true);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        super.draw(batch, parentAlpha);

        float size = PixelLogicGUILevelResolutionManager.instance().getBaseHeight() / 2;
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
