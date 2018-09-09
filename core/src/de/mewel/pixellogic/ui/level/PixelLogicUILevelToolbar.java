package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
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

    private Texture backgroundTexture;

    private MenuButton menuButton;

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

        this.menuButton = new MenuButton(getAssets().getIcon(2));
        this.menuButtonListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getEventManager().fire(new PixelLogicUserEvent(PixelLogicUILevelToolbar.this, PixelLogicUserEvent.Type.LEVEL_MENU_CLICKED));
                return super.touchDown(event, x, y, pointer, button);
            }

        };
        this.switcher = new PixelLogicUILevelSwitcher(getAssets(), getEventManager());
        this.switcherListener = new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                switcher.swap();
                return true;
            }

        };

        this.addActor(this.menuButton);
        this.addActor(this.switcher);
        this.updateBounds();

        this.getEventManager().listen(this);
    }

    public void show(PixelLogicLevel level) {
        this.level = level;
        this.menuButton.addAction(Actions.fadeIn(.3f));
        this.switcher.addAction(Actions.fadeIn(.3f));

        this.menuButton.addListener(this.menuButtonListener);
        this.switcher.addListener(this.switcherListener);
    }

    public void solve() {
        // rm toolbar listeners
        this.menuButton.removeListener(menuButtonListener);
        this.switcher.removeListener(switcherListener);

        // fade out toolbar elements
        this.menuButton.addAction(Actions.fadeOut(.3f));
        this.switcher.addAction(Actions.fadeOut(.3f));

        // display solved text
        if (this.solvedLabel == null) {
            BitmapFont labelFont = getToolbarFont();
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

    private BitmapFont getToolbarFont() {
        return getAssets().getGameFont((int) this.getHeight());
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        show(event.getLevel());
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
        solve();
    }

    @Override
    public void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.timerRunning = false;
        if (this.solvedLabel != null) {
            this.solvedLabel.addAction(Actions.sequence(Actions.fadeOut(.4f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    removeActor(solvedLabel);
                    solvedLabel = null;
                }
            })));
        }
        if (this.timerLabel != null) {
            this.timerLabel.addAction(Actions.sequence(Actions.fadeOut(.4f), Actions.run(new Runnable() {
                @Override
                public void run() {
                    removeActor(timerLabel);
                    timerLabel = null;
                }
            })));
        }
    }

    @Override
    public void handle(PixelLogicEvent event) {
        if (event instanceof PixelLogicTimerEvent) {
            PixelLogicTimerEvent timerEvent = (PixelLogicTimerEvent) event;
            if (this.timerLabel == null) {
                BitmapFont labelFont = getToolbarFont();
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
            if (timerEvent.getStatus().equals(PixelLogicTimerEvent.Status.resume)) {
                this.timerRunning = true;
                setTimer(timerEvent.getTime());
            }
            if (timerEvent.getStatus().equals(PixelLogicTimerEvent.Status.stop) || timerEvent.getStatus().equals(PixelLogicTimerEvent.Status.pause)) {
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

    private static class MenuButton extends Actor {

        private Sprite sprite;

        public MenuButton(Sprite sprite) {
            this.sprite = sprite;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
            super.draw(batch, parentAlpha);

            float size = PixelLogicUIUtil.getIconBaseHeight();
            float offset = size / 2;
            float y = MathUtils.floor(getY()) + offset - 1;
            float x = MathUtils.floor(getX()) + offset;
            float alpha = parentAlpha * color.a;
            Color spriteColor = Color.WHITE;

            batch.setColor(new Color(spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a * alpha));
            batch.draw(sprite, x, y, size, size);
            batch.setColor(color);
        }

    }


}
