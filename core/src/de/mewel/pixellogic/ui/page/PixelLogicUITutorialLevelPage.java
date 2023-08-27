package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.rafaskoberg.gdx.typinglabel.TypingAdapter;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import com.rafaskoberg.gdx.typinglabel.TypingListener;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIContainer;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelMenu;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelResolution;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelSwitcher;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelToolbar;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;
import static de.mewel.pixellogic.PixelLogicConstants.KEY_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.KEY_SOUND_VOLUME;

public class PixelLogicUITutorialLevelPage extends PixelLogicUILevelPage {

    private final MessageBox messageBox;

    private final PixelLogicUISprite switcherMarker;

    public PixelLogicUITutorialLevelPage(PixelLogicGlobal global) {
        super(global);
        this.messageBox = new MessageBox(global);
        this.switcherMarker = new PixelLogicUISprite(global);
        this.switcherMarker.setSprite(global.getAssets().getIcon(3));
        this.switcherMarker.getSprite().rotate90(false);
        this.switcherMarker.setColor(global.getStyle().getMainColor());
        this.toolbar.getUndoButton().remove();
    }

    protected PixelLogicUILevelMenu createMenu() {
        return new TutorialMenu(getGlobal(), this);
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

    /*@Override
    protected void onSolved() {
        showSolvedAnimation();
        getAssets().get().get(PixelLogicConstants.PUZZLE_SOLVED_SOUND, Sound.class).play(.2f);
    }*/

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
        this.setMessage(text, new TutorialTypingAdapter(getAudio()));
    }

    public void setMessage(String text, final Runnable action) {
        this.setMessage(text, new TutorialTypingAdapter(getAudio()) {
            @Override
            public void end() {
                showNextMessageButton(() -> {
                    hideNextButton();
                    action.run();
                });
            }
        });
    }

    public void setMessage(String text, TypingListener listener) {
        this.messageBox.addAction(Actions.fadeIn(.3f));
        this.messageBox.setText(text, listener);
        updateMessageBoxBounds();
    }

    public void hideMessage() {
        this.messageBox.addAction(Actions.fadeOut(.3f));
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

    public void showSwitcher(Runnable after) {
        PixelLogicUILevelToolbar toolbar = getToolbar();
        final PixelLogicUILevelSwitcher switcher = toolbar.getSwitcher();
        switcher.remove();
        switcher.clearActions();
        switcher.getColor().a = 0f;
        switcher.setPosition(getWidth() / 2, getHeight() / 2, Align.center);
        getStage().getRoot().addActor(switcher);

        final SwitcherModal switcherModal = new SwitcherModal(getGlobal(), getStage().getRoot());
        switcherModal.setBounds(0, 0, getWidth(), getHeight());

        int padding = (int) toolbar.getHeight() / 32;
        int x = (int) (toolbar.getWidth() - switcher.getWidth()) - padding;
        int y = (int) (toolbar.getHeight() - (toolbar.getHeight() - (padding * 2))) / 2;

        SequenceAction sequence = new SequenceAction();
        sequence.addAction(Actions.delay(.5f));
        sequence.addAction(Actions.run(() -> switcherModal.show(switcher)));
        sequence.addAction(Actions.delay(1f));
        sequence.addAction(Actions.fadeIn(.5f));
        sequence.addAction(Actions.delay(1f));
        sequence.addAction(Actions.run(switcher::swap));
        sequence.addAction(Actions.delay(1f));
        sequence.addAction(Actions.moveTo(x, y, .2f));
        sequence.addAction(Actions.delay(.2f));
        sequence.addAction(Actions.run(switcherModal::close));
        sequence.addAction(Actions.delay(.2f));
        sequence.addAction(Actions.run(after));
        switcher.addAction(sequence);
    }

    public void showSwitcherMarker() {
        int size = PixelLogicUIUtil.getIconBaseHeight();
        this.switcherMarker.setSize(size, size);

        PixelLogicUILevelToolbar toolbar = getToolbar();
        final PixelLogicUILevelSwitcher switcher = toolbar.getSwitcher();
        int x = (int) (toolbar.getWidth() - switcher.getWidth() / 2 - switcherMarker.getWidth() / 2);
        int y = (int) (toolbar.getHeight() + switcherMarker.getHeight() / 3);
        this.switcherMarker.setPosition(x, y);

        this.switcherMarker.addAction(Actions.forever(Actions.sequence(
                Actions.moveBy(0, this.switcherMarker.getHeight(), .8f),
                Actions.moveBy(0, -this.switcherMarker.getHeight(), .8f))));

        getStage().addActor(this.switcherMarker);
    }

    public void hideSwitcherMarker() {
        this.switcherMarker.remove();
        this.switcherMarker.clearActions();
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        this.messageBox.styleChanged(style);
    }

    @Override
    public TutorialScreenListener getScreenListener() {
        return (TutorialScreenListener) super.getScreenListener();
    }

    public static class MessageBox extends PixelLogicUIContainer<HorizontalGroup> {

        private Container<Label> labelContainer;

        private TypingLabel label;

        private final PixelLogicUISprite next;

        public MessageBox(PixelLogicGlobal global) {
            super(global, new HorizontalGroup());
            createLabel("", null);
            this.next = new PixelLogicUISprite(getGlobal(), 7);
            this.next.setColor(new Color(getGlobal().getStyle().getSecondaryColor()));
            this.hideNextButton(0);

            getActor().addActor(this.labelContainer);
            getActor().addActor(this.next);
        }

        @Override
        public Color getBackgroundColor() {
            return getGlobal().getStyle().getPixelEmptyColor();
        }

        private void createLabel(String text, TypingListener typingListener) {
            // build label
            BitmapFont font = PixelLogicUIUtil.getMainFont(getAssets());
            this.label = new TypingLabel(text,
                    new Label.LabelStyle(font, new Color(Color.WHITE)));
            this.label.setWrap(true);
            if (typingListener != null) {
                this.label.setTypingListener(typingListener);
            }
            // build container
            if (this.labelContainer == null) {
                this.labelContainer = new Container<>(this.label);
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
            this.next.setSize(iconSize, iconSize);
            this.next.pad(iconPadding / 2);

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
            return lineHeight * 6 + getPadBottom() + getPadTop() + 2 * getPadding();
        }

        public void showNextButton(final Runnable action) {
            this.next.setColor(new Color(getGlobal().getStyle().getSecondaryColor()));
            this.next.getColor().a = 0f;
            this.next.addAction(Actions.sequence(
                    Actions.fadeIn(.2f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            next.addListener(new InputListener() {
                                @Override
                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                    getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
                                    action.run();
                                    return super.touchDown(event, x, y, pointer, button);
                                }
                            });
                        }
                    }),
                    PixelLogicUIUtil.blinkAction(new Color(getGlobal().getStyle().getSecondaryColor()), .8f)));
        }

        private void hideNextButton(float fadeOutDuration) {
            this.next.clearActions();
            this.next.addAction(Actions.fadeOut(fadeOutDuration));
            this.next.getListeners().clear();
        }

    }

    public static class TutorialScreenListener extends ScreenListener {

        private boolean destroyLevelOnClick = false;

        public TutorialScreenListener(PixelLogicUILevelPage page) {
            super(page);
        }

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return !destroyLevelOnClick || super.touchDown(event, x, y, pointer, button);
        }

        public void setDestroyLevelOnClick(boolean destroyLevelOnClick) {
            this.destroyLevelOnClick = destroyLevelOnClick;
        }

        @Override
        protected void handleAutoBlock(PixelLogicBoardChangedEvent event) {
        }

    }

    private static class TutorialMenu extends PixelLogicUILevelMenu {

        public TutorialMenu(PixelLogicGlobal global, PixelLogicUILevelPage screen) {
            super(global, screen);
        }

        @Override
        protected void buildContent() {
            // continue
            this.continueButton = new PixelLogicUIButton(getGlobal(), "continue") {
                @Override
                public void handleClick() {
                    close();
                }
            };

            // back
            this.backButton = new PixelLogicUIButton(getGlobal(), "skip tutorial") {
                @Override
                public void handleClick() {
                    close();
                    back();
                }
            };
        }
    }

    protected static class SwitcherModal extends PixelLogicUIModal {

        public SwitcherModal(PixelLogicGlobal global, Group parent) {
            super(global, parent);
        }

    }

    public static class TutorialTypingAdapter extends TypingAdapter {

        private final PixelLogicAudio audio;

        @Override
        public void onChar(Character ch) {
            audio.playSound(KEY_SOUND, KEY_SOUND_VOLUME);
            super.onChar(ch);
        }

        public TutorialTypingAdapter(PixelLogicAudio audio) {
            this.audio = audio;
        }

    }

}
