package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.screen.event.PixelLogicStartTimeTrialModeEvent;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIHorizontalLine;

import static de.mewel.pixellogic.asset.PixelLogicAssets.GAME_FONT_SIZE;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private VerticalGroup root;

    private AssetStore assetStore;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        buildGUI();
    }

    protected void buildGUI() {
        this.assetStore = new AssetStore(getAssets(), getEventManager());

        // labels
        Label descriptionLabel = new Label("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", assetStore.getSmallLabelStyle());
        descriptionLabel.setWrap(true);

        // combine to VerticalGroup
        this.root = new VerticalGroup();
        this.root.setFillParent(true);
        this.root.center();
        this.root.bottom();
        this.root.reverse();
        this.root.pad(getPadding());
        this.root.space(getPadding() * 2);

        Container<Label> labelContainer = new Container<Label>(descriptionLabel);
        labelContainer.width(getComponentWidth());
        this.root.addActor(labelContainer);
        this.root.addActor(new Mode("NORMAL MODE", assetStore, new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getEventManager().fire(new PixelLogicStartTimeTrialModeEvent(PixelLogicUITimeTrialScreen.this,
                        new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialNormalOptions()));
            }
        }));
        this.root.addActor(new Mode("HARD MODE", assetStore, new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                getEventManager().fire(new PixelLogicStartTimeTrialModeEvent(PixelLogicUITimeTrialScreen.this,
                        new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions()));
            }
        }));

        getStage().addActor(this.root);
        this.root.layout();
    }

    public static int getPadding() {
        return Gdx.graphics.getWidth() / 18;
    }

    public static int getComponentWidth() {
        return Gdx.graphics.getWidth() - 2 * getPadding();
    }

    protected void cleanGUI() {
        getStage().getRoot().removeActor(this.root);
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

    private static int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private static int getButtonWidth() {
        return getButtonHeight() * 4;
    }

    private static class Mode extends Container<VerticalGroup> {

        public Mode(String name, AssetStore assetStore, InputListener inputListener) {
            super(new VerticalGroup());

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().reverse();
            getActor().space(getPadding());

            Label normalHighscoreLabel = new Label("highscore", assetStore.getSmallLabelStyle());
            Label normalTimeLabel = new Label("time", assetStore.getSmallLabelStyle());
            Label header = new Label(name, assetStore.getNormalLabelStyle());

            PixelLogicUIButton button = new PixelLogicUIButton(assetStore.getAssets(), assetStore.getEventManager(), "PLAY");
            button.addListener(inputListener);
            button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(header);
            getActor().addActor(button);

            Table highscoreTable = new Table();
            highscoreTable.setFillParent(true);
            //highscoreTable.setDebug(true);

            Container<Table> highscoreContainer = new Container<Table>(highscoreTable);
            highscoreContainer.width(getComponentWidth());

            PixelLogicUIHorizontalLine line = new PixelLogicUIHorizontalLine(assetStore.getAssets(), assetStore.getEventManager());
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            line.setColor(TEXT_COLOR);
            highscoreTable.add(line).colspan(2).expand();

            highscoreTable.row();

            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();

            getActor().addActor(highscoreContainer);
            getActor().pack();
        }

    }


    private static class AssetStore {

        private PixelLogicAssets assets;

        private PixelLogicEventManager eventManager;

        private Label.LabelStyle normalLabelStyle, smallLabelStyle;

        private AssetStore(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
            this.assets = assets;
            this.eventManager = eventManager;

            BitmapFont normalFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
            this.normalLabelStyle = new Label.LabelStyle(normalFont, TEXT_COLOR);

            BitmapFont smallFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight() - GAME_FONT_SIZE);
            this.smallLabelStyle = new Label.LabelStyle(smallFont, TEXT_COLOR);
        }

        public PixelLogicAssets getAssets() {
            return assets;
        }

        public PixelLogicEventManager getEventManager() {
            return eventManager;
        }

        public Label.LabelStyle getNormalLabelStyle() {
            return normalLabelStyle;
        }

        public Label.LabelStyle getSmallLabelStyle() {
            return smallLabelStyle;
        }

    }

}
