package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

public class PixelLogicVerticalGroup extends VerticalGroup {

    PixelLogicUIColoredSurface background;

    public void setBackground(PixelLogicUIColoredSurface background) {
        this.background = background;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        if(this.background != null) {
            this.background.setSize(getWidth(), getHeight());
        }
    }

    @Override
    protected void positionChanged() {
        super.positionChanged();
        if(this.background != null) {
            this.background.setPosition(getX(), getY());
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (background != null) {
            background.draw(batch, parentAlpha);
        }
        super.draw(batch, parentAlpha);
    }

}
