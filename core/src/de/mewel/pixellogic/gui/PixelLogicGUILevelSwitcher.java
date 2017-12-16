package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class PixelLogicGUILevelSwitcher extends Group {

    private Texture background;

    private Marker marker;

    public PixelLogicGUILevelSwitcher() {
        Texture texture = new Texture(Gdx.files.internal("gui/level/toolbar.png"));
        //this.background = new Sprite(texture, 0, 0, 1, 20);
        //this.background.flip(false, true);

        this.background = PixelLogicGUIUtil.getTexture(Color.LIGHT_GRAY);
        this.marker = new Marker();
        this.addActor(this.marker);

        this.updateBounds();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        //batch.draw(this.marker, getX(), getY(), 12 * getScaleX(), 12 * getScaleY());
        batch.setColor(color);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        this.updateBounds();
    }

    private void updateBounds() {
        this.marker.setBounds(0, 0, this.getWidth() / 2, this.getHeight());
    }

    @Override
    public void clear() {
        super.clear();
        this.background.dispose();
    }

    private static class Marker extends Actor {

        private Texture texture;

        Marker() {
            this.texture = PixelLogicGUIUtil.getTexture(Color.ORANGE);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            batch.draw(texture, getX(), getY(), getWidth() * getScaleX(),
                    getHeight() * getScaleY());
            batch.setColor(color);
        }

        @Override
        public void clear() {
            super.clear();
            this.texture.dispose();
        }
    }

}
