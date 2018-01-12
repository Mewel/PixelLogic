package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.ui.level.PixelLogicGUILevelResolutionManager;

public class PixelLogicUIButton extends Group {

    private PixelLogicUIColoredSurface background;

    public PixelLogicUIButton(String text) {
        this.background = new PixelLogicUIColoredSurface(Color.BLUE);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        this.background.setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        batch.setColor(color);
        super.draw(batch, parentAlpha);
    }

}
