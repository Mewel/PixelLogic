package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

public class PixelLogicUIUndoButton extends PixelLogicUIGroup {

    private PixelLogicUIColoredSurface background;

    private Sprite sprite;

    public PixelLogicUIUndoButton(PixelLogicGlobal global) {
        super(global);
        this.sprite = getAssets().getIcon(13);
        this.background = new PixelLogicUIColoredSurface(global);
        this.background.setColor(getGlobal().getStyle().getSecondaryColor());
        this.addActor(this.background);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.background.setSize(this.getWidth(), this.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        super.draw(batch, parentAlpha);
        float alpha = parentAlpha * color.a;
        batch.setColor(this.getColor().r, this.getColor().g, this.getColor().b, this.getColor().a * alpha);

        float size = PixelLogicUIUtil.getIconBaseHeight();
        float y = MathUtils.floor(getY()) + getHeight() / 2 - size / 2;
        float x = MathUtils.floor(getX()) + getWidth() / 2 - size / 2;
        batch.draw(this.sprite, x, y, size, size);
        batch.setColor(color);
    }

    @Override
    public void clear() {
        this.background.clear();
        super.clear();
    }

}
