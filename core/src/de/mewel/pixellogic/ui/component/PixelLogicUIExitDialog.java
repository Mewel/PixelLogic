package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIExitDialog extends PixelLogicUIModal {

    private Label question;

    private PixelLogicUIButton closeButton, cancelButton;

    public PixelLogicUIExitDialog(PixelLogicGlobal global, Group parent) {
        super(global, parent);
        buildContent();
    }

    private void buildContent() {
        BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        Label.LabelStyle style = new Label.LabelStyle(labelFont, new Color(Color.WHITE));

        this.question = new Label(getAssets().translate("exit.label"), style);

        this.closeButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("close")) {
            @Override
            public void handleClick() {
                Gdx.app.exit();
            }
        };

        this.cancelButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("cancel")) {
            @Override
            public void handleClick() {
                PixelLogicUIExitDialog.this.close();
            }
        };

        getContent().add(question).colspan(2).expandX();
        getContent().row();
        getContent().add(cancelButton);
        getContent().add(closeButton);
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        int buttonHeight = PixelLogicUIUtil.getBaseHeight();
        int buttonWidth = (int) (getWidth() / 2.5f);

        this.cancelButton.setSize(buttonWidth, buttonHeight);
        this.closeButton.setSize(buttonWidth, buttonHeight);
        getContent().getCell(question).pad(getWidth() / 10);
    }

}
