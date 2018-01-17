package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicSwitcherChangedEvent;
import de.mewel.pixellogic.ui.PixelLogicGUIConstants;
import de.mewel.pixellogic.ui.PixelLogicGUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.BASE_SIZE;
import static de.mewel.pixellogic.ui.PixelLogicGUIConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;

public class PixelLogicGUILevelSwitcher extends Group {

    private Texture background;

    private Sprite penSprite, xSprite;

    private Color penColor, xColor;

    private Marker marker;

    private boolean fillPixel;

    private boolean swappingAnimationActivated;

    private boolean swappingAnimationStarted;

    public PixelLogicGUILevelSwitcher(Texture icons) {
        this.penSprite = new Sprite(icons, 0, 0, BASE_SIZE, BASE_SIZE);
        this.xSprite = new Sprite(icons, BASE_SIZE, 0, BASE_SIZE, BASE_SIZE);
        this.penSprite.flip(false, true);
        this.xSprite.flip(false, true);
        this.penColor = PixelLogicGUIConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;
        this.xColor = PixelLogicGUIConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;

        this.fillPixel = true;
        this.swappingAnimationActivated = false;
        this.swappingAnimationStarted = false;

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
        super.draw(batch, parentAlpha);

        float size = PixelLogicGUILevelResolutionManager.instance().getIconBaseHeight();
        float offset = size / 2;
        float y = MathUtils.floor(getY()) + offset;
        float x = MathUtils.floor(getX()) + offset;
        float alpha = parentAlpha * color.a;
        batch.setColor(new Color(this.penColor.r, this.penColor.g, this.penColor.b, this.penColor.a * alpha));
        batch.draw(penSprite, x, y, size, size);
        batch.setColor(new Color(this.xColor.r, this.xColor.g, this.xColor.b,this.xColor.a * alpha));
        batch.draw(xSprite, x + size + 2 * offset, y, size, size);
        batch.setColor(color);
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
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        this.background.dispose();
        super.clear();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if(this.swappingAnimationActivated) {
            this.penColor = PixelLogicGUIConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;
            this.xColor = PixelLogicGUIConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;
            float x = this.fillPixel ? 0 : this.getWidth() - this.marker.getWidth();
            this.marker.addAction(Actions.sequence(
                    Actions.moveTo(x, 0, 0.1f),
                    Actions.color(TOOLBAR_SWITCHER_ACTIVE_COLOR, 0.05f),
                    Actions.color(Color.ORANGE, 0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            swappingAnimationStarted = false;
                            penColor = fillPixel ? TOOLBAR_SWITCHER_ACTIVE_COLOR : PixelLogicGUIConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;
                            xColor = !fillPixel ? TOOLBAR_SWITCHER_ACTIVE_COLOR : PixelLogicGUIConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;
                            PixelLogicEventManager.instance().fire(
                                    new PixelLogicSwitcherChangedEvent(
                                            PixelLogicGUILevelSwitcher.this, fillPixel
                                    ));
                        }
                    }
            )));
            this.swappingAnimationActivated = false;
            this.swappingAnimationStarted = true;
        }
    }

    public void swap() {
        if(this.swappingAnimationStarted) {
            return;
        }
        this.swappingAnimationActivated = true;
        this.fillPixel = !this.fillPixel;
    }

    private static class Marker extends Actor {

        private Texture texture;

        Marker() {
            this.texture = PixelLogicGUIUtil.getTexture(Color.WHITE);
            this.setColor(Color.ORANGE);
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
