package de.mewel.pixellogic.ui.page;

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
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIConstants;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.level.PixelLogicUILevelGroup;

public abstract class PixelLogicUIBasePage extends PixelLogicUIPage {

    protected Header header;

    protected VerticalGroup pageRoot;

    public PixelLogicUIBasePage(PixelLogicGlobal global, PixelLogicUIPageId pageId) {
        this(global, pageId, null, null);
    }

    public PixelLogicUIBasePage(PixelLogicGlobal global, PixelLogicUIPageId pageId, String headerText, PixelLogicUIPageId backPageId) {
        super(global, pageId);
        this.header = null;
        buildGui(headerText, backPageId);
    }

    protected void buildGui(String headerText, PixelLogicUIPageId backPageId) {
        this.pageRoot = buildRoot();
        ScrollPane scrollPane;
        if (headerText != null) {
            this.header = new Header(getAssets(), getEventManager(), headerText, backPageId);
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

    protected VerticalGroup buildRoot() {
        VerticalGroup root = new VerticalGroup();
        root.top();
        root.pad(getPadding());
        root.space(getSpace());
        return root;
    }

    protected abstract void build();

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

    private class Header extends PixelLogicUILevelGroup {

        private PixelLogicUIColoredSurface background;

        private Label label;

        private String text;

        private BackButton backButton;

        public Header(PixelLogicAssets assets, PixelLogicEventManager eventManager, String text, final PixelLogicUIPageId backPageId) {
            super(assets, eventManager);
            this.text = text;

            this.background = new PixelLogicUIColoredSurface(assets);
            this.background.setColor(PixelLogicUIConstants.PIXEL_BLOCKED_COLOR);
            this.addActor(this.background);

            this.backButton = new BackButton(assets.getIcon(3));
            this.backButton.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
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

        private class BackButton extends Actor {

            private Sprite sprite;

            public BackButton(Sprite sprite) {
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
