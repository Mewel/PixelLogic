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
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicTimerEvent;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUILevelToolbar extends PixelLogicUILevelGroup implements PixelLogicListener {

    private Texture icons, backgroundTexture;

    private PixelLogicUILevelMenuButton menuButton;

    private PixelLogicUILevelSwitcher switcher;

    private InputListener menuButtonListener, switcherListener;

    private PixelLogicLevel level;

    private Label solvedLabel;

    private Label timerLabel;

    private long timerStart;

    private boolean timerRunning;

    public PixelLogicUILevelToolbar(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.level = null;
        this.solvedLabel = null;
        this.timerLabel = null;
        this.timerStart = 0;
        this.timerRunning = false;
        this.backgroundTexture = PixelLogicUIUtil.getTexture(Color.BLACK);
        this.icons = new Texture(Gdx.files.internal("gui/level/toolbar.png"));

        this.menuButton = new PixelLogicUILevelMenuButton(this.icons);
        this.menuButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getEventManager().fire(new PixelLogicUserEvent(PixelLogicUILevelToolbar.this, PixelLogicUserEvent.Type.TOOLBAR_MENU_CLICKED));
                return super.touchDown(event, x, y, pointer, button);
            }

        };
        this.switcher = new PixelLogicUILevelSwitcher(getAssets(), getEventManager(), this.icons);
        this.switcherListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switcher.swap();
                return super.touchDown(event, x, y, pointer, button);
            }

        };

        this.addActor(this.menuButton);
        this.addActor(this.switcher);
        this.updateBounds();

        this.getEventManager().listen(this);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        this.menuButton.addAction(Actions.fadeIn(.3f));
        this.switcher.addAction(Actions.fadeIn(.3f));

        this.menuButton.addListener(this.menuButtonListener);
        this.switcher.addListener(this.switcherListener);
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
        // rm toolbar listeners
        this.menuButton.removeListener(menuButtonListener);
        this.switcher.removeListener(switcherListener);

        // fade out toolbar elements
        this.menuButton.addAction(Actions.fadeOut(.3f));
        this.switcher.addAction(Actions.fadeOut(.3f));

        // display solved text
        if (this.solvedLabel == null) {
            BitmapFont labelFont = getAssets().getGameFont((int) this.getHeight());
            Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
            this.solvedLabel = new Label(level.getName(), style);
            this.addActor(this.solvedLabel);
        } else {
            this.solvedLabel.setText(level.getName());
        }
        centerLabel(this.solvedLabel);
        this.solvedLabel.getColor().a = 0f;
        this.solvedLabel.addAction(Actions.sequence(Actions.delay(.3f), Actions.fadeIn(.3f)));

        // clock
        if (this.timerLabel != null) {
            this.timerLabel.addAction(Actions.fadeOut(.3f));
        }
    }

    @Override
    public void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.solvedLabel.addAction(Actions.fadeOut(.4f));
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicTimerEvent) {
            PixelLogicTimerEvent timerEvent = (PixelLogicTimerEvent) event;
            if (this.timerLabel == null) {
                BitmapFont labelFont = getAssets().getGameFont((int) this.getHeight());
                Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
                this.timerLabel = new Label("00:00", style);
                this.addActor(this.timerLabel);
            }
            if (timerEvent.getStatus().equals(PixelLogicTimerEvent.Status.start)) {
                this.timerRunning = true;
                this.timerLabel.getColor().a = 0f;
                this.timerLabel.addAction(Actions.fadeIn(.3f));
                setTimer(timerEvent.getTime());
            }
            if (timerEvent.getStatus().equals(PixelLogicTimerEvent.Status.stop)) {
                this.timerRunning = false;
            }
        }
    }

    private void centerLabel(Label label) {
        if (label == null) {
            return;
        }
        label.setPosition(getWidth() / 2 - label.getPrefWidth() / 2, getHeight() / 2 - label.getPrefHeight() / 2);
    }

    private void setTimer(long passedTime) {
        this.timerStart = System.currentTimeMillis() - passedTime;
        this.timerLabel.setText(PixelLogicUIUtil.formatMilliseconds(System.currentTimeMillis() - this.timerStart));
        centerLabel(this.timerLabel);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (this.timerLabel != null && this.timerRunning) {
            this.timerLabel.setText(PixelLogicUIUtil.formatMilliseconds(System.currentTimeMillis() - this.timerStart));
        }
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
        this.getEventManager().remove(this);
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
        centerLabel(this.solvedLabel);
        centerLabel(this.timerLabel);
    }

}
