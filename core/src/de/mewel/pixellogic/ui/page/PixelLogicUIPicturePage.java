package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.Align;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicPictureMode;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButtonListener;
import de.mewel.pixellogic.ui.misc.PixelLogicUIPicture;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BLOCK_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUIPicturePage extends PixelLogicUIBasePage {

    private List<PictureModeContainer> pictureModeContainers;

    public PixelLogicUIPicturePage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.picture, "Art Mode", PixelLogicUIPageId.moreLevels);
    }

    @Override
    protected void build() {
        this.pictureModeContainers = new ArrayList<PictureModeContainer>();

        // da vinvi
        PixelLogicLevelCollection davinciCollection = getAssets().getLevelCollection("pictures/davinci");
        PixelLogicPictureMode daVinciMode = new PixelLogicPictureMode(davinciCollection);
        daVinciMode.setup(getGlobal());
        PictureModeContainer daVinciContainer = new PictureModeContainer(daVinciMode);
        this.pictureModeContainers.add(daVinciContainer);
        getPageRoot().addActor(daVinciContainer);

        // add listeners
        for (final PictureModeContainer container : this.pictureModeContainers) {
            if (container.mode.isSolved()) {
                continue;
            }
            container.addListener(new PixelLogicUIButtonListener() {
                @Override
                public void onClick() {
                    container.mode.activate();
                    container.mode.run();
                    PixelLogicUIPageProperties pageProperties = new PixelLogicUIPageProperties();
                    pageProperties.put("solvedAnimation", "picture");
                    pageProperties.put("pictureCollection", container.mode.getCollection());
                    pageProperties.put("menuBackId", PixelLogicUIPageId.picture);
                    getAppScreen().setPage(PixelLogicUIPageId.level, pageProperties);
                }
            });
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.updateSize();
    }

    protected void updateSize() {
        getPageRoot().setWidth(this.getWidth());
        getPageRoot().pad(0);
        getPageRoot().padTop(getPadding() / 4);
        getPageRoot().padBottom(getPadding() / 4);
        getPageRoot().space(getSpace() / 8);

        for (PictureModeContainer pictureModeContainer : this.pictureModeContainers) {
            pictureModeContainer.resize((int) this.getWidth(), (int) this.getHeight());
        }

        getPageRoot().invalidate();
        getPageRoot().setHeight(getPageRoot().getPrefHeight());
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        for (PictureModeContainer pictureModeContainer : this.pictureModeContainers) {
            pictureModeContainer.updatImage();
            pictureModeContainer.updateLabel();
        }
        updateSize();
        fadeIn(null);
    }

    private class PictureModeContainer extends Container<VerticalGroup> {

        private PixelLogicUIPicture picture;

        private PixelLogicPictureMode mode;

        private Container<Label> labelContainer;

        PictureModeContainer(PixelLogicPictureMode mode) {
            super(new VerticalGroup());
            this.mode = mode;
            this.picture = new PixelLogicUIPicture(getAssets(), getEventManager(), mode.getCollection());

            getActor().setFillParent(true);
            getActor().center();

            getActor().addActor(this.picture);

            Label descriptionLabel = this.getLabel(getLabelText(), TEXT_COLOR);
            descriptionLabel.setWrap(true);
            descriptionLabel.setAlignment(Align.center);
            this.labelContainer = new Container<Label>(descriptionLabel);
            getActor().addActor(this.labelContainer);
            this.updateLabel();

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        private String getLabelText() {
            return isSolved() ? this.mode.getCollection().getName() : "???";
        }

        private void updateLabel() {
            this.labelContainer.getActor().setText(getLabelText());
        }

        private boolean isSolved() {
            return mode.isSolved();
        }

        public void resize(int width, int height) {
            int padding = getPadding() * 2;

            float size = Math.min(width, height / 1.75f) - padding;
            getActor().pad(padding / 2);
            getActor().space(padding / 4);

            this.picture.setSize(size, size);
            this.labelContainer.width(getComponentWidth());
            this.labelContainer.getActor().setStyle(getLabelStyle(TEXT_COLOR));

            this.width(width);
        }

        public Label getLabel(String text, Color color) {
            Label.LabelStyle style = getLabelStyle(color);
            return new Label(text, style);
        }

        private Label.LabelStyle getLabelStyle(Color color) {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 2);
            return new Label.LabelStyle(labelFont, color);
        }

        public void updatImage() {
            this.picture.update(mode.getLevelIndex(), -1, 150);
        }

    }

}
