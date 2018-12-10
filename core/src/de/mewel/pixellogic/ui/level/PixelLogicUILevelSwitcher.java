package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelSwitcherChangedEvent;

import static de.mewel.pixellogic.PixelLogicConstants.SWITCHER_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;
import static de.mewel.pixellogic.PixelLogicConstants.TOOLBAR_SWITCHER_INACTIVE_COLOR;

public class PixelLogicUILevelSwitcher extends PixelLogicUIGroup {

    private Texture background;

    private Sprite penSprite, xSprite;

    private Color penColor, xColor;

    private Marker marker;

    private boolean fillPixel;

    PixelLogicUILevelSwitcher(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.penSprite = getAssets().getIcon(0);
        this.xSprite = getAssets().getIcon(1);
        this.penColor = PixelLogicConstants.TOOLBAR_SWITCHER_ACTIVE_COLOR;
        this.xColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;

        this.fillPixel = true;

        this.background = PixelLogicUIUtil.getTexture(Color.LIGHT_GRAY);
        this.marker = new Marker();
        this.addActor(this.marker);

        this.updateBounds();
    }

    public void reset() {
        this.fillPixel = true;
        this.marker.setX(0);
        this.penColor = TOOLBAR_SWITCHER_ACTIVE_COLOR;
        this.xColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;
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
        batch.setColor(new Color(this.penColor.r, this.penColor.g, this.penColor.b, this.penColor.a * alpha));
        batch.draw(penSprite, x, y, size, size);
        batch.setColor(new Color(this.xColor.r, this.xColor.g, this.xColor.b, this.xColor.a * alpha));
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
        Sound sound = getAssets().get().get(SWITCHER_SOUND, Sound.class);
        sound.play(.05f);

        // animate
        this.penColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;
        this.xColor = TOOLBAR_SWITCHER_INACTIVE_COLOR;
        float x = this.fillPixel ? 0 : this.getWidth() - this.marker.getWidth();
        this.marker.addAction(Actions.sequence(
                Actions.moveTo(x, 0, 0.1f),
                Actions.color(TOOLBAR_SWITCHER_ACTIVE_COLOR, 0.05f),
                Actions.color(Color.ORANGE, 0.1f),
                Actions.run(new Runnable() {
                                @Override
                                public void run() {
                                    penColor = fillPixel ? TOOLBAR_SWITCHER_ACTIVE_COLOR : TOOLBAR_SWITCHER_INACTIVE_COLOR;
                                    xColor = !fillPixel ? TOOLBAR_SWITCHER_ACTIVE_COLOR : TOOLBAR_SWITCHER_INACTIVE_COLOR;
                                }
                            }
                )));
    }

    private static class Marker extends Actor {

        private Texture texture;

        Marker() {
            this.texture = PixelLogicUIUtil.getTexture(Color.WHITE);
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
