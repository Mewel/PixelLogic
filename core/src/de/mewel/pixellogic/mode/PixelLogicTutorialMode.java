package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;

import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.level.PixelLogicUILevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUITutorialLevelPage;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;

public class PixelLogicTutorialMode extends PixelLogicLevelMode {

    private int status;

    private List<PixelLogicLevel> levels;

    private MessageBox messageBox;

    @Override
    public void setup(PixelLogicGlobal global) {
        super.setup(global);
        this.levels = PixelLogicLevelLoader.load(getAssets().getLevelCollection("tutorial"));
        this.messageBox = new MessageBox();
    }

    @Override
    public void run() {
        this.status = 0;
        runLevel(this.levels.get(0));
    }

    private PixelLogicUITutorialLevelPage getPage() {
        return (PixelLogicUITutorialLevelPage) getAppScreen().getPage(PixelLogicUIPageId.tutorialLevel);
    }

    @Override
    public void handle(PixelLogicEvent event) {
        super.handle(event);
        PixelLogicUITutorialLevelPage levelPage = getPage();

        if (event instanceof PixelLogicLevelStatusChangeEvent) {
            PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
            if (PixelLogicLevelStatus.playable.equals(changeEvent.getStatus()) && status == 0) {
                levelPage.alignLevel(0, .2f);
                PixelLogicUILevel levelUI = levelPage.getLevelUI();
                levelUI.setEnabled(false);

                setMessage("{COLOR=GREEN}Hello,{WAIT} world!"
                        + "{COLOR=ORANGE}{SLOWER} Did you know orange is my favorite color?");

                //levelPage.getLevelUI().getColumnGroup().addAction(Actions.fadeOut(.2f));
            }
        }
    }

    public void setMessage(String text) {
        this.messageBox.setText(text);
        updateMessageBox();
    }

    public void updateMessageBox() {
        if (!this.messageBox.hasParent()) {
            getPage().getRoot().addActor(this.messageBox);
        }
        PixelLogicUILevel levelUI = getPage().getLevelUI();
        //int x = levelUI.getX();
        //int y = levelUI.getHeight();

        this.messageBox.setX(levelUI.getX());
        this.messageBox.setWidth(levelUI.getWidth());
        this.messageBox.setHeight(this.messageBox.getPrefHeight());
        int y = (int) this.messageBox.getHeight() + (int) levelUI.getY() / 2;
        this.messageBox.setY(y);

        this.messageBox.setDebug(true);
        //this.messageBox.getLabel().setDebug(true);

        Gdx.app.log("label heght", this.messageBox.labelContainer.getPrefHeight() + "");

        //this.messageBox.setBounds(levelUI.getX(), 200, levelUI.getWidth(), 200);
    }


    public class MessageBox extends Container<HorizontalGroup> {

        private Container<Label> labelContainer;

        private TypingLabel label;

        public MessageBox() {
            super(new HorizontalGroup());
            createLabel("");

            getActor().setFillParent(true);
            getActor().addActor(this.labelContainer);
            getActor().pack();
        }

        private void createLabel(String text) {
            BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            this.label = new TypingLabel(text,
                    new Label.LabelStyle(font, Color.WHITE));
            this.label.setWrap(true);
            if (this.labelContainer == null) {
                this.labelContainer = new Container<Label>(this.label);
            } else {
                this.labelContainer.setActor(this.label);
            }
        }

        @Override
        protected void sizeChanged() {
            super.sizeChanged();
            this.labelContainer.width(getWidth());
        }

        public void setText(String text) {
            this.label.remove();
            createLabel(text);
        }

        public TypingLabel getLabel() {
            return label;
        }

        @Override
        public float getPrefHeight() {
            return label.getPrefHeight() * 4 + getPadBottom() + getPadTop();
        }

    }

}
