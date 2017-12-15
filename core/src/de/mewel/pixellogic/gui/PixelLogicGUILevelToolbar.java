package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Group;

import javax.xml.soap.Text;

import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGUILevelToolbar extends Group {

    private Texture backgroundTexture;

    private PixelLogicGUILevelSwitcher switcher;

    public PixelLogicGUILevelToolbar() {
        this.backgroundTexture = PixelLogicGUIUtil.getTexture(Color.BLACK);
        this.switcher = new PixelLogicGUILevelSwitcher();
        this.addActor(this.switcher);
        this.updateBounds();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(this.backgroundTexture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(color);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void clear() {
        this.backgroundTexture.dispose();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        updateBounds();
    }

    private void updateBounds() {
        float padding = this.getHeight() / 32;
        float switcherWidth = this.getHeight() * 2;
        float switcherHeight = this.getHeight() - (padding * 2);
        this.switcher.setBounds((this.getWidth() - switcherWidth) - padding,
                (this.getHeight() - switcherHeight) / 2, switcherWidth, switcherHeight);
    }

}
