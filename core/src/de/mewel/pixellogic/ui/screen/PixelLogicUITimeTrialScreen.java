package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private Table table;

    private Label descriptionLabel, normalLabel, hardLabel;

    private PixelLogicUIButton playNormalButton, playHardButton;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        buildGUI();
    }

    protected void buildGUI() {
        BitmapFont normalFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
        Label.LabelStyle normalStyle = new Label.LabelStyle(normalFont, TEXT_COLOR);

        BitmapFont smallFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight() / 2);
        Label.LabelStyle smallStyle = new Label.LabelStyle(smallFont, TEXT_COLOR);

        int screenWidth = Gdx.graphics.getWidth();
        int padding = screenWidth / 18;

        // labels
        this.descriptionLabel = new Label("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", normalStyle);
        this.descriptionLabel.setWrap(true);

        this.normalLabel = new Label("NORMAL MODE", normalStyle);
        this.hardLabel = new Label("HARD MODE", normalStyle);

        // buttons
        this.playNormalButton = new PixelLogicUIButton(getAssets(), getEventManager(), "PLAY");
        this.playNormalButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });

        this.playHardButton = new PixelLogicUIButton(getAssets(), getEventManager(), "PLAY");
        this.playHardButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {

            }
        });
        int buttonWidth = getButtonWidth();
        int buttonHeight = getButtonHeight();
        this.playNormalButton.setSize(buttonWidth, buttonHeight);
        this.playHardButton.setSize(buttonWidth, buttonHeight);

        // highscore
        Label normalHighscoreLabel = new Label("highscore", smallStyle);

        // combine to table
        this.table = new Table();
        this.table.setFillParent(true);
        this.table.setDebug(true);

        this.table.add(this.playHardButton).padBottom(padding).expand().bottom();
        this.table.row();
        this.table.add(this.hardLabel).padBottom(padding);

        // ShapeRenderer

        this.table.row();
        this.table.add(normalHighscoreLabel).padBottom(padding / 2);
        this.table.row();
        this.table.add(this.playNormalButton).padBottom(padding / 2);
        this.table.row();
        this.table.add(this.normalLabel).padBottom(padding);

        this.table.row();
        this.table.add(this.descriptionLabel).growX();

        this.table.pad(padding);
        getStage().addActor(this.table);
    }

    protected void cleanGUI() {
        getStage().getRoot().removeActor(this.table);
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

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.cleanGUI();
        this.buildGUI();
    }

    private int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private int getButtonWidth() {
        return getButtonHeight() * 4;
    }

}
