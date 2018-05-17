package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;

public class PixelLogicUISecretLevelIntroModal extends PixelLogicUIModal {

    public PixelLogicUISecretLevelIntroModal(PixelLogicAssets assets, PixelLogicEventManager eventManager, Stage stage) {
        super(assets, eventManager, stage.getRoot());
        buildContent();
    }

    private void buildContent() {
        float pad = Gdx.graphics.getWidth() / 40f;

        BitmapFont headerFont = PixelLogicUIUtil.getAppFont(getAssets(), 4);
        Label header = new Label("SECRET LEVEL", new Label.LabelStyle(headerFont, Color.WHITE));

        BitmapFont descriptionFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
        Label description = new Label("description description description description description", new Label.LabelStyle(descriptionFont, Color.WHITE));
        description.setWrap(true);

        description.setDebug(true);
        getContent().setDebug(true);

        getContent().pad(pad);
        getContent().add(header);
        getContent().row();
        getContent().add(description);

        PixelLogicUIUtil.fixLabelHeight(description, Gdx.graphics.getWidth() - pad * 2);
    }

}
