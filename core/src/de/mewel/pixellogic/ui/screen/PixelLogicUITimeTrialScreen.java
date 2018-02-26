package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private Table table;

    private Label descriptionLabel, normalLabel, hardLabel;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        BitmapFont baseFont = getAssets().getGameFont(PixelLogicUIUtil.getBaseHeight() / 2);
        Label.LabelStyle style = new Label.LabelStyle(baseFont, TEXT_COLOR);

        this.descriptionLabel = new Label("Play infinite computer generated levels against the " +
                "clock and try to beat your own highscore.", style);
        this.descriptionLabel.setWrap(true);
        this.descriptionLabel.setWidth(200);

        this.normalLabel = new Label("NORMAL", style);
        this.hardLabel = new Label("NORMAL", style);

        this.table = new Table();

        this.table.addActor(this.descriptionLabel);
        this.table.row();
        this.table.addActor(this.normalLabel);
        this.table.row();
        this.table.addActor(this.hardLabel);

        getStage().addActor(this.table);
    }

    @Override
    public void activate(PixelLogicUIScreenData data) {

    }

    @Override
    public void show() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
