package de.mewel.pixellogic.ui.page;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;

public abstract class PixelLogicUIBasePage extends PixelLogicUIPage implements PixelLogicListener {

    protected Header header;

    protected VerticalGroup pageRoot;

    protected PixelLogicUIPageId backPageId;

    public PixelLogicUIBasePage(PixelLogicGlobal global, PixelLogicUIPageId pageId) {
        this(global, pageId, null, null);
    }

    public PixelLogicUIBasePage(PixelLogicGlobal global, PixelLogicUIPageId pageId, String headerText, PixelLogicUIPageId backPageId) {
        super(global, pageId);
        this.header = null;
        buildGui(headerText, backPageId);
    }

    protected void buildGui(String headerText, PixelLogicUIPageId backPageId) {
        this.backPageId = backPageId;
        this.pageRoot = buildRoot();
        ScrollPane scrollPane;
        if (headerText != null) {
            this.header = buildHeader(headerText, backPageId);
            VerticalGroup baseGroup = new VerticalGroup();
            baseGroup.top();
            baseGroup.addActor(this.header);
            baseGroup.addActor(this.pageRoot);
            scrollPane = new ScrollPane(baseGroup);
        } else {
            scrollPane = new ScrollPane(this.pageRoot);
        }

        getStage().addActor(scrollPane);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setClamp(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFillParent(true);

        build();
    }

    protected Header buildHeader(String headerText, PixelLogicUIPageId backPageId) {
        return new Header(getGlobal(), headerText, backPageId);
    }

    protected VerticalGroup buildRoot() {
        VerticalGroup root = new VerticalGroup();
        root.top();
        root.pad(getPadding());
        root.space(getSpace());
        return root;
    }

    protected abstract void build();


    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        this.getEventManager().listen(this);
        super.activate(properties);
    }

    @Override
    public void deactivate(final Runnable after) {
        this.getEventManager().remove(this);
        fadeOut(() -> PixelLogicUIBasePage.super.deactivate(after));
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (this.header != null) {
            // Make the header taller to reach under the notch
            int safeTop = Gdx.graphics.getSafeInsetTop() / 2;
            this.header.setHeight((height / 12f) + safeTop);
            this.header.setWidth(width);
        }
        // Add space to the bottom of the scrollable list so it isn't hidden by the nav bar
        this.pageRoot.padBottom(getPadding() + Gdx.graphics.getSafeInsetBottom());
    }

    public int getPadding() {
        return (int) this.getHeight() / 24;
    }

    public int getSpace() {
        return (int) (getPadding() * 1.5f);
    }

    public int getComponentWidth() {
        return (int) this.getWidth() - 2 * getPadding();
    }

    public static int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    public static int getButtonWidth() {
        return (int) (getButtonHeight() * 4.5f);
    }

    public VerticalGroup getPageRoot() {
        return pageRoot;
    }

    @Override
    public void handle(PixelLogicEvent event) {
        Gdx.app.log("handle", this.getClass().getName());
        if (backPageId != null && event instanceof PixelLogicUserEvent) {
            PixelLogicUserEvent userEvent = (PixelLogicUserEvent) event;
            if (PixelLogicUserEvent.Type.BACK_BUTTON_CLICKED.equals(userEvent.getType())) {
                getAppScreen().setPage(backPageId, new PixelLogicUIPageProperties());
            }
        }
    }

    protected class Header extends PixelLogicUIGroup {

        private final PixelLogicUIColoredSurface background;

        private Label label;

        private final String text;

        private final PixelLogicUISprite backButton;

        public Header(PixelLogicGlobal global, String text, final PixelLogicUIPageId backPageId) {
            super(global);
            this.text = text;

            this.background = new PixelLogicUIColoredSurface(global);
            this.background.setColor(new Color(global.getStyle().getMainColor()));
            this.addActor(this.background);

            this.backButton = new PixelLogicUISprite(global, 3);
            this.backButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
                    getAppScreen().setPage(backPageId, new PixelLogicUIPageProperties());
                }
            });
            this.addActor(this.backButton);

            this.updateLabel();
        }

        private void updateLabel() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 2);
            if (this.label != null) {
                if (labelFont.equals(this.label.getStyle().font)) {
                    return;
                }
                this.removeActor(this.label);
            }
            Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
            this.label = new Label(this.text, style);
            this.addActorBefore(this.backButton, this.label);
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            this.background.setSize(this.getWidth(), this.getHeight());
            updateLabel();
        }

        @Override
        protected void positionChanged() {
            super.positionChanged();
            int safeTop = Gdx.graphics.getSafeInsetTop() / 2;

            // Push the label down by the notch height
            int x = (int) (this.getWidth() / 2 - this.label.getPrefWidth() / 2);
            int y = (int) ((this.getHeight() - safeTop) / 2 - this.label.getPrefHeight() / 2);
            this.label.setPosition(x, y);

            // Push the back button down by the notch height
            int size = PixelLogicUIUtil.getIconBaseHeight();
            int iconPadding = size / 2;
            y = (int) ((this.getHeight() - safeTop) / 2f - size / 2f) - iconPadding;
            x = getPadding() - iconPadding;
            this.backButton.setBounds(x, y, size, size);
            this.backButton.pad(iconPadding);
        }

        @Override
        public void clear() {
            this.background.clear();
            super.clear();
        }

    }

}
