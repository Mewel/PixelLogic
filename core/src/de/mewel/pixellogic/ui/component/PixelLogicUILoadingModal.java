package de.mewel.pixellogic.ui.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUILoadingModal extends PixelLogicUIModal {

    private String message;

    public PixelLogicUILoadingModal(String message, PixelLogicAssets assets, PixelLogicEventManager eventManager, Stage stage) {
        super(assets, eventManager, stage.getRoot());
        this.message = message;
    }

    @Override
    protected void buildContent(Table content) {
        BitmapFont gameFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
        Label.LabelStyle style = new Label.LabelStyle(gameFont, Color.WHITE);
        content.add(new Label(message, style));
    }

}
