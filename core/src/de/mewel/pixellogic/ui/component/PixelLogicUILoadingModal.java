package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUILoadingModal extends PixelLogicUIModal {

    private String message;

    public PixelLogicUILoadingModal(String message, PixelLogicAssets assets, PixelLogicEventManager eventManager, Stage stage) {
        super(assets, eventManager, stage.getRoot());
        this.message = message;
        buildContent();
    }

    protected void buildContent() {
        BitmapFont gameFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        Label.LabelStyle style = new Label.LabelStyle(gameFont, Color.WHITE);
        Label label = new Label(this.message, style);
        getContent().add(label);
    }

}
