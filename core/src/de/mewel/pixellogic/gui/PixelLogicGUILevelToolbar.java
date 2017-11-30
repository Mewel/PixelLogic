package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PixelLogicGUILevelToolbar extends Group {

    private Texture texture;

    private Sprite background;

    private PixelLogicGUILevelSwitcher switcher;

    public PixelLogicGUILevelToolbar() {
        this.texture = new Texture(Gdx.files.internal("gui/level/toolbar.png"));
        this.background = new Sprite(texture, 0, 0, 1, 20);
        this.background.flip(false, true);

        this.switcher = new PixelLogicGUILevelSwitcher(texture);
        this.switcher.setBounds(0, 0, 16, 16);
        this.addActor(this.switcher);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(color);

        super.draw(batch, parentAlpha);
    }

    @Override
    public void clear() {
        this.texture.dispose();
    }

}
