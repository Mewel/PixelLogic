package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.PixelLogicConstants.MAIN_COLOR;

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
        return new Header(getAssets(), getEventManager(), headerText, backPageId);
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
        fadeOut(new Runnable() {
            @Override
            public void run() {
                PixelLogicUIBasePage.super.deactivate(after);
            }
        });
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (this.header != null) {
            this.header.setHeight(height / 12);
            this.header.setWidth(width);
        }
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

    public boolean hasHeader() {
        return this.header != null;
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

        private PixelLogicUIColoredSurface background;

        private Label label;

        private String text;

        private HeaderButton backButton;

        public Header(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text, final PixelLogicUIPageId backPageId) {
            super(assets, eventManager);
            this.text = text;

            this.background = new PixelLogicUIColoredSurface(assets);
            this.background.setColor(MAIN_COLOR);
            this.addActor(this.background);

            this.backButton = new HeaderButton(assets.getIcon(3));
            this.backButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    PixelLogicUIUtil.playButtonSound(getAssets());
                    getAppScreen().setPage(backPageId, new PixelLogicUIPageProperties());
                }
            });
            this.addActor(this.backButton);

            this.updateLabel();
        }

        private void updateLabel() {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 3);
            if (this.label != null) {
                if (labelFont.equals(this.label.getStyle().font)) {
                    return;
                }
                this.removeActor(this.label);
            }
            Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
            this.label = new Label(this.text, style);
            this.addActor(this.label);
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

            // label
            int x = (int) (this.getWidth() / 2 - this.label.getPrefWidth() / 2);
            int y = (int) (this.getHeight() / 2 - this.label.getPrefHeight() / 2);
            this.label.setPosition(x, y);

            // back button
            float size = PixelLogicUIUtil.getIconBaseHeight();
            y = (int) (this.getHeight() / 2f - size / 2f);
            x = getPadding();
            this.backButton.setBounds(x, y, size, size);
        }

        @Override
        public void clear() {
            this.background.clear();
            super.clear();
        }

        protected class HeaderButton extends Actor {

            private Sprite sprite;

            public HeaderButton(Sprite sprite) {
                this.sprite = sprite;
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                Color color = getColor();
                batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);
                super.draw(batch, parentAlpha);

                Color spriteColor = Color.WHITE;

                batch.setColor(new Color(spriteColor.r, spriteColor.g, spriteColor.b, 1f));
                batch.draw(sprite, getX(), getY(), getWidth(), getHeight());
                batch.setColor(color);
            }

        }


    }

}
