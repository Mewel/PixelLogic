package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIMessageModal extends PixelLogicUIModal {

    private String message;

    public PixelLogicUIMessageModal(String message, PixelLogicGlobal global, Stage stage) {
        super(global, stage.getRoot());
        this.message = message;
        buildContent();
    }

    private void buildContent() {
        BitmapFont gameFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        Label.LabelStyle style = new Label.LabelStyle(gameFont, Color.WHITE);
        Label label = new Label(this.message, style);
        getContent().add(label);
    }

}
