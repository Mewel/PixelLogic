package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.gui.screen.PixelLogicLevelStatus;

public class PixelLogicGUILevelToolbar extends Group implements PixelLogicLevelChangeListener {

    private Texture backgroundTexture;

    private PixelLogicGUILevelSwitcher switcher;

    private PixelLogicLevel level;

    private Label solvedLabel;

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicGUILevelToolbar() {
        this.level = null;
        this.solvedLabel = null;
        this.backgroundTexture = PixelLogicGUIUtil.getTexture(Color.BLACK);
        this.switcher = new PixelLogicGUILevelSwitcher();
        this.switcher.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switcher.swap();
                return super.touchDown(event, x, y, pointer, button);
            }

        });
        this.changeAdapter = new PixelLogicLevelChangeAdapter();
        this.changeAdapter.bind(this);

        this.addActor(this.switcher);
        this.updateBounds();
    }

    @Override
    public void onLevelChange(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelLoad(PixelLogicLevelChangeEvent event) {
        this.level = event.getLevel();
        this.switcher.addAction(Actions.fadeIn(.3f));
    }

    @Override
    public void onLevelSolved(PixelLogicLevelChangeEvent event) {
        // fade out switcher
        this.switcher.addAction(Actions.fadeOut(.3f));
        // display text
        if (this.solvedLabel == null) {
            BitmapFont labelFont = PixelLogicGUILevelResolutionManager.instance().getFont(this.getHeight(), Color.WHITE);
            Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
            this.solvedLabel = new Label(level.getName(), style);
            this.addActor(this.solvedLabel);
        } else {
            this.solvedLabel.setText(level.getName());
        }
        this.solvedLabel.setPosition(getWidth() / 2 - solvedLabel.getPrefWidth() / 2,
                getHeight() / 2 - solvedLabel.getPrefHeight() / 2);
        this.solvedLabel.getColor().a = 0f;
        this.solvedLabel.addAction(Actions.sequence(Actions.delay(.3f), Actions.fadeIn(.3f)));
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelChangeEvent event) {
        this.solvedLabel.addAction(Actions.fadeOut(.1f));
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha * .7f);
        batch.draw(this.backgroundTexture, getX(), getY(), getWidth() * getScaleX(),
                getHeight() * getScaleY());
        batch.setColor(color);
        super.draw(batch, parentAlpha);
    }

    @Override
    public void clear() {
        super.clear();
        this.backgroundTexture.dispose();
        this.changeAdapter.unbind();
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
