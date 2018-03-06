package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private Table table;

    private Label descriptionLabel, normalLabel, hardLabel;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        BitmapFont baseFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
        Label.LabelStyle style = new Label.LabelStyle(baseFont, TEXT_COLOR);

        int screenWidth = Gdx.graphics.getWidth();
        int padding = screenWidth / 18;

        this.descriptionLabel = new Label("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", style);
        this.descriptionLabel.setWrap(true);

        this.normalLabel = new Label("NORMAL", style);
        this.hardLabel = new Label("HARD", style);

        this.table = new Table();
        this.table.setFillParent(true);
        this.table.setDebug(true);

        this.table.add(this.hardLabel).expand().bottom().padBottom(padding);
        this.table.row();
        this.table.add(this.normalLabel).padBottom(padding);
        this.table.row();
        this.table.add(this.descriptionLabel).growX();

        this.table.pad(padding);
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
