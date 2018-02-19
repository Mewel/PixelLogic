package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUILevelToolbar extends PixelLogicUILevelGroup {

    private Texture icons, backgroundTexture;

    private PixelLogicUILevelMenuButton menuButton;

    private PixelLogicUILevelSwitcher switcher;

    private PixelLogicLevel level;

    private Label solvedLabel;

    public PixelLogicUILevelToolbar(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.level = null;
        this.solvedLabel = null;
        this.backgroundTexture = PixelLogicUIUtil.getTexture(Color.BLACK);
        this.icons = new Texture(Gdx.files.internal("gui/level/toolbar.png"));

        this.menuButton = new PixelLogicUILevelMenuButton(this.icons);
        this.menuButton.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getEventManager().fire(new PixelLogicUserEvent(PixelLogicUILevelToolbar.this, PixelLogicUserEvent.Type.TOOLBAR_MENU_CLICKED));
                return super.touchDown(event, x, y, pointer, button);
            }

        });

        this.switcher = new PixelLogicUILevelSwitcher(getAssets(), getEventManager(), this.icons);
        this.switcher.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switcher.swap();
                return super.touchDown(event, x, y, pointer, button);
            }

        });

        this.addActor(this.menuButton);
        this.addActor(this.switcher);
        this.updateBounds();
    }

    @Override
    public void onLevelChange(PixelLogicLevelStatusChangeEvent event) {
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        this.menuButton.addAction(Actions.fadeIn(.3f));
        this.switcher.addAction(Actions.fadeIn(.3f));
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
        // fade out toolbar elements
        this.menuButton.addAction(Actions.fadeOut(.3f));
        this.switcher.addAction(Actions.fadeOut(.3f));

        // display text
        if (this.solvedLabel == null) {
            BitmapFont labelFont = getAssets().getLevelFont((int) this.getHeight());
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
    public void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.solvedLabel.addAction(Actions.fadeOut(.4f));
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
        this.backgroundTexture.dispose();
        this.icons.dispose();
        super.clear();
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        super.setBounds(x, y, width, height);
        updateBounds();
    }

    private void updateBounds() {
        float padding = this.getHeight() / 32;
        float iconSize = this.getHeight() - (padding * 2);
        float switcherWidth = this.getHeight() * 2;
        this.menuButton.setBounds(padding, padding, iconSize, iconSize);
        this.switcher.setBounds((this.getWidth() - switcherWidth) - padding,
                (this.getHeight() - iconSize) / 2, switcherWidth, iconSize);
    }

}
