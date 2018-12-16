package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelResolution;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelSwitcher;

import static de.mewel.pixellogic.PixelLogicConstants.SECONDARY_COLOR;

public class PixelLogicUITutorialLevelPage extends PixelLogicUILevelPage {

    private MessageBox messageBox;

    public PixelLogicUITutorialLevelPage(PixelLogicGlobal global) {
        super(global);
        this.messageBox = new MessageBox();
    }

    @Override
    protected ScreenListener createScreenListener() {
        return new TutorialScreenListener(this);
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
    }

    @Override
    protected void updateBounds(int width, int height) {
        updateMessageBoxBounds();
        super.updateBounds(width, height);
    }

    protected void updateLevelBounds(int width, int height) {
        if (this.messageBox == null) {
            return;
        }
        float messageBoxOffset = this.messageBox.getY() + this.messageBox.getHeight();
        int maxHeight = (int) (height - messageBoxOffset);
        PixelLogicUILevelResolution resolution = new PixelLogicUILevelResolution(getLevel(), width, maxHeight);
        Vector2 levelSize = resolution.getLevelSize();
        this.levelUI.setSize(levelSize.x, levelSize.y);
        this.levelUI.updateLevelResolution(resolution);
        float x = width / 2f - this.levelUI.getWidth() / 2f;
        float y = height - levelSize.y - ((height - levelSize.y - messageBoxOffset) / 2);
        this.levelUI.setPosition((int) x, (int) y);
    }

    public void updateMessageBoxBounds() {
        PixelLogicUILevel levelUI = getLevelUI();
        if (this.messageBox == null || levelUI == null) {
            return;
        }
        int space = PixelLogicUIUtil.getToolbarPaddingTop() * 2;
        this.messageBox.setX(space);
        this.messageBox.setWidth(getWidth() - 2 * space);
        this.messageBox.setHeight(this.messageBox.getPrefHeight());
        int y = PixelLogicUIUtil.getToolbarHeight() + PixelLogicUIUtil.getToolbarPaddingTop() * 3;
        this.messageBox.setY(y);
    }

    @Override
    public void loadLevel(PixelLogicLevel level) {
        super.loadLevel(level);
        getStage().addActor(this.messageBox);
    }

    @Override
    protected void onSolved() {
        showSolvedAnimation();
    }

    @Override
    public void destroyLevel() {
        super.destroyLevel();
        if (this.messageBox != null) {
            this.messageBox.hideNextButton(0);
            this.messageBox.clearText();
            this.messageBox.remove();
        }
    }

    public void setMessage(String text) {
        this.setMessage(text, (TypingListener) null);
    }

    public void setMessage(String text, final Runnable action) {
        this.setMessage(text, new TypingAdapter() {
            @Override
            public void end() {
                Gdx.app.log("tutorial page", "show next message button");
                showNextMessageButton(new Runnable() {
                    @Override
                    public void run() {
                        hideNextButton();
                        action.run();
                    }
                });
            }
        });
    }

    public void setMessage(String text, TypingListener listener) {
        this.messageBox.setText(text, listener);
        updateMessageBoxBounds();
    }

    public void showNextMessageButton(Runnable action) {
        this.messageBox.showNextButton(action);
    }

    public void hideNextButton() {
        this.messageBox.hideNextButton(.2f);
    }

    public void hideSwitcher() {
        PixelLogicUILevelSwitcher switcher = getToolbar().getSwitcher();
        switcher.remove();
    }


    public class MessageBox extends Container<HorizontalGroup> {

        private Container<Label> labelContainer;

        private TypingLabel label;

        private NextButton next;

        public MessageBox() {
            super(new HorizontalGroup());
            createLabel("", null);
            this.next = new NextButton(getAssets().getIcon(7));
            this.next.setColor(SECONDARY_COLOR);
            this.hideNextButton(0);

            getActor().addActor(this.labelContainer);
            getActor().addActor(this.next);

            Texture whiteTexture = PixelLogicUIUtil.getTexture(PixelLogicConstants.PIXEL_EMPTY_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        private void createLabel(String text, TypingListener typingListener) {
            // build label
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            this.label = new TypingLabel(text,
                    new Label.LabelStyle(font, Color.WHITE));
            this.label.setWrap(true);
            if (typingListener != null) {
                this.label.setTypingListener(typingListener);
            }
            // build container
            if (this.labelContainer == null) {
                this.labelContainer = new Container<Label>(this.label);
                this.labelContainer.align(Align.topLeft);
            } else {
                this.labelContainer.setActor(this.label);
            }
            // update size
            updateSize();
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            getActor().setWidth(getWidth());
            getActor().setHeight(getHeight());
            getActor().pad(getPadding());
            updateSize();
        }

        private void updateSize() {
            if (this.next == null || this.labelContainer == null) {
                return;
            }
            int iconSize = PixelLogicUIUtil.getIconBaseHeight();
            int iconPadding = iconSize / 2;
            this.next.setSize(iconSize + iconPadding, iconSize + iconPadding);
            this.labelContainer.width(getWidth() - 2 * getPadding() - (iconSize + iconPadding));
            this.labelContainer.height(getHeight() - 2 * getPadding());
        }

        public int getPadding() {
            return PixelLogicUIUtil.getToolbarPaddingTop() * 2;
        }

        public void setText(String text, TypingListener listener) {
            clearText();
            createLabel(text, listener);
        }

        public void clearText() {
            this.label.remove();
            this.label.setTypingListener(null);
        }

        public TypingLabel getLabel() {
            return label;
        }

        @Override
        public float getPrefHeight() {
            float lineHeight = label.getBitmapFontCache().getFont().getLineHeight();
            return lineHeight * 5 + getPadBottom() + getPadTop() + 2 * getPadding();
        }

        public void showNextButton(final Runnable action) {
            this.next.setColor(new Color(SECONDARY_COLOR));
            this.next.getColor().a = 0f;
            this.next.addAction(Actions.sequence(
                    Actions.fadeIn(.2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            next.addListener(new InputListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    action.run();
                                    return super.touchDown(event, x, y, pointer, button);
                                }
                            });
                        }
                    })/*,
                    PixelLogicUIUtil.blinkAction(new Color(SECONDARY_COLOR), .8f)*/));
        }

        private void hideNextButton(float fadeOutDuration) {
            this.next.getListeners().clear();
            this.next.clearActions();
            this.next.addAction(Actions.fadeOut(fadeOutDuration));
        }

        private class NextButton extends Actor {

            private Sprite sprite;

            public NextButton(Sprite sprite) {
                this.sprite = sprite;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                super.draw(batch, parentAlpha);

                float size = PixelLogicUIUtil.getIconBaseHeight();
                float y = MathUtils.floor(getY()) + getHeight() / 2 - size / 2;
                float x = MathUtils.floor(getX()) + getWidth() / 2 - size / 2;
                float alpha = parentAlpha * color.a;
                Color spriteColor = getColor();

                batch.setColor(new Color(spriteColor.r, spriteColor.g, spriteColor.b, spriteColor.a * alpha));
                batch.draw(sprite, x, y, size, size);
                batch.setColor(color);
            }

        }


    }

    protected static class TutorialScreenListener extends ScreenListener {

        public TutorialScreenListener(PixelLogicUILevelPage page) {
            super(page);
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            // do not destroy levels on click
            return true;
        }

    }

}
