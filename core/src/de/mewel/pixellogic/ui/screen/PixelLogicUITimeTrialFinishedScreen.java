package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;
import de.mewel.pixellogic.ui.PixelLogicUIScreen;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUITimeTrialFinishedScreen extends PixelLogicUIScreen {

    private static Color BACKGROUND_COLOR = Color.valueOf("#FAFAFA");

    private Stage stage;

    private Label resultLabel;

    public PixelLogicUITimeTrialFinishedScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        this.stage = new Stage();

        BitmapFont labelFont = getAssets().getGameFont(PixelLogicUIUtil.getBaseHeight() * 2);
        Label.LabelStyle style = new Label.LabelStyle(labelFont, Color.WHITE);
        this.resultLabel = new Label("00:00", style);
        this.stage.addActor(this.resultLabel);
    }

    @Override
    public void activate() {
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(BACKGROUND_COLOR.r, BACKGROUND_COLOR.g, BACKGROUND_COLOR.b, BACKGROUND_COLOR.a);
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int i, int i1) {

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

    @Override
    public void dispose() {

    }
}
