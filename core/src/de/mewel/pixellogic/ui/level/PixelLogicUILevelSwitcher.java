package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelSwitcherChangedEvent;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyleable;

import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;

public class PixelLogicUILevelSwitcher extends PixelLogicUIGroup {

    private Texture background;

    private Sprite penSprite, xSprite;

    private Color penColor, xColor;

    private Marker marker;

    private boolean fillPixel;

    PixelLogicUILevelSwitcher(PixelLogicGlobal global) {
        super(global);
        this.penSprite = getAssets().getIcon(0);
        this.xSprite = getAssets().getIcon(1);
        this.penColor = PixelLogicConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;
        this.xColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;

        this.fillPixel = true;

        this.background = PixelLogicUIUtil.getTexture(Color.LIGHT_GRAY);
        this.marker = new Marker(global);
        this.addActor(this.marker);

        this.updateBounds();
    }

    public void reset() {
        this.fillPixel = true;
        this.marker.setX(0);
        this.penColor = new Color(TOOLBAR_SWITCHER_ACTIVE_COLOR);
        this.xColor = new Color(TOOLBAR_SWITCHER_INACTIVE_COLOR);
        PixelLogicUIUtil.changeStyle(this.marker, getGlobal().getStyle());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
        batch.draw(background, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        super.draw(batch, parentAlpha);

        float size = PixelLogicUIUtil.getIconBaseHeight();
        float offset = size / 2;
        float y = MathUtils.floor(getY()) + offset;
        float x = MathUtils.floor(getX()) + offset;
        float alpha = parentAlpha * color.a;
        batch.setColor(this.penColor.r, this.penColor.g, this.penColor.b, this.penColor.a * alpha);
        batch.draw(penSprite, x, y, size, size);
        batch.setColor(this.xColor.r, this.xColor.g, this.xColor.b, this.xColor.a * alpha);
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

    public void swap() {
        this.fillPixel = !this.fillPixel;
        getEventManager().fire(new PixelLogicLevelSwitcherChangedEvent(PixelLogicUILevelSwitcher.this, fillPixel));

        // sound
        getAudio().playSound(SWITCHER_SOUND, SWITCHER_SOUND_VOLUME);

        // animate
        this.penColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;
        this.xColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;
        float x = this.fillPixel ? 0 : this.getWidth() - this.marker.getWidth();
        this.marker.addAction(Actions.sequence(
                Actions.moveTo(x, 0, 0.1f),
                Actions.color(new Color(TOOLBAR_SWITCHER_ACTIVE_COLOR), 0.05f),
                Actions.color(getGlobal().getStyle().getSecondaryColor(), 0.1f),
                Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    penColor = fillPixel ? new Color(TOOLBAR_SWITCHER_ACTIVE_COLOR) : new Color(TOOLBAR_SWITCHER_INACTIVE_COLOR);
                                    xColor = !fillPixel ? new Color(TOOLBAR_SWITCHER_ACTIVE_COLOR) : new Color(TOOLBAR_SWITCHER_INACTIVE_COLOR);
                                }
                            }
                )));
    }

    private static class Marker extends Actor implements PixelLogicUIStyleable {

        private PixelLogicGlobal global;

        private Texture texture;

        Marker(PixelLogicGlobal global) {
            this.global = global;
            this.texture = PixelLogicUIUtil.getTexture(Color.WHITE);
            this.setColor(global.getStyle().getSecondaryColor());
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
        public void styleChanged(PixelLogicUIStyle style) {
            this.setColor(global.getStyle().getSecondaryColor());
        }

        @Override
        public void clear() {
            super.clear();
            this.texture.dispose();
        }
    }

}
