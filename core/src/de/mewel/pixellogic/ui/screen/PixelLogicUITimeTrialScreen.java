package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.List;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicLevelMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialHighscoreStore;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIHorizontalLine;
import de.mewel.pixellogic.ui.screen.event.PixelLogicScreenChangeEvent;

import static de.mewel.pixellogic.asset.PixelLogicAssets.GAME_FONT_SIZE;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.LINE_COMPLETE_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_LIGHT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private VerticalGroup root;

    private AssetStore assetStore;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        //PixelLogicTimeTrialHighscoreStore.clear(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialNormalOptions().id);
        //PixelLogicTimeTrialHighscoreStore.clear(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions().id);

        buildGUI();
    }

    protected void buildGUI() {
        this.assetStore = new AssetStore(getAssets(), getEventManager(), getProperties());

        Gdx.app.log("screen", "build gui");

        // labels
        Label descriptionLabel = assetStore.getSmallLabel("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", TEXT_COLOR);
        descriptionLabel.setWrap(true);

        // combine to VerticalGroup
        this.root = new VerticalGroup();
        this.root.setFillParent(true);
        this.root.center();
        this.root.bottom();
        this.root.reverse();
        this.root.pad(getPadding());
        this.root.space(getPadding() * 2.5f);

        Container<Label> labelContainer = new Container<Label>(descriptionLabel);
        labelContainer.width(getComponentWidth());
        this.root.addActor(labelContainer);
        this.root.addActor(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialNormalOptions(), assetStore));
        this.root.addActor(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions(), assetStore));

        getStage().addActor(this.root);
        this.root.layout();
    }

    public static int getPadding() {
        return Gdx.graphics.getHeight() / 24;
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
    public void activate(PixelLogicUIScreenProperties properties) {
        super.activate(properties);
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

    private static class TimeTrialModeUI extends Container<VerticalGroup> {

        private AssetStore assetStore;

        public TimeTrialModeUI(final PixelLogicTimeTrialModeOptions options, AssetStore assetStore) {
            super(new VerticalGroup());
            this.assetStore = assetStore;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().reverse();
            getActor().space(getPadding());

            Label normalHighscoreLabel = assetStore.getSmallLabel("highscore", TEXT_LIGHT_COLOR);
            Label normalTimeLabel = assetStore.getSmallLabel("time", TEXT_LIGHT_COLOR);

            PixelLogicUIButton button = new PixelLogicUIButton(assetStore.getAssets(), assetStore.getEventManager(), options.name);
            button.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    startTimeTrial(options);
                }
            });
            button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(button);

            Table highscoreTable = new Table();
            highscoreTable.setFillParent(true);
            //highscoreTable.setDebug(true);

            List<PixelLogicTimeTrialHighscoreStore.Highscore> highscoreList = PixelLogicTimeTrialHighscoreStore.list(options.id);
            if (!highscoreList.isEmpty()) {
                for (int i = highscoreList.size() - 1; i >= 0; i--) {
                    Integer rank = assetStore.getProperties().getInt("rank");
                    Color color = rank == null || rank == -1 || rank != i ? TEXT_COLOR : LINE_COMPLETE_COLOR;
                    PixelLogicTimeTrialHighscoreStore.Highscore highscore = highscoreList.get(i);
                    Label highscoreDate = assetStore.getSmallLabel(PixelLogicUIUtil.formatDate(highscore.date), color);
                    Label highscoreTime = assetStore.getSmallLabel(PixelLogicUIUtil.formatMilliseconds(highscore.time), color);
                    highscoreTable.add(highscoreDate).growX().left();
                    highscoreTable.add(highscoreTime).right();
                    highscoreTable.row();
                }
            } else {
                highscoreTable.add(assetStore.getSmallLabel("no games played", TEXT_COLOR)).center();
                highscoreTable.row();
            }

            Container<Table> highscoreContainer = new Container<Table>(highscoreTable);
            highscoreContainer.width(getComponentWidth());

            PixelLogicUIHorizontalLine line = new PixelLogicUIHorizontalLine(assetStore.getAssets(), assetStore.getEventManager());
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            line.setColor(TEXT_LIGHT_COLOR);
            highscoreTable.add(line).colspan(2).expand();

            highscoreTable.row();

            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();

            getActor().addActor(highscoreContainer);
            getActor().pack();
        }

        private void startTimeTrial(PixelLogicTimeTrialModeOptions options) {
            PixelLogicLevelMode mode = new PixelLogicTimeTrialMode(options);
            mode.setup(assetStore.getAssets(), assetStore.getEventManager());
            mode.run();
            PixelLogicUIScreenProperties data = new PixelLogicUIScreenProperties();
            data.put("screenId", "level");
            data.put("options", options);
            assetStore.getEventManager().fire(new PixelLogicScreenChangeEvent(this, data));
        }

    }

    private static class AssetStore {

        private PixelLogicAssets assets;

        private PixelLogicEventManager eventManager;

        private PixelLogicUIScreenProperties properties;

        private BitmapFont normalFont, smallFont;

        private AssetStore(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUIScreenProperties properties) {
            this.assets = assets;
            this.eventManager = eventManager;
            this.properties = properties;

            this.normalFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight());
            this.smallFont = getAssets().getGameFont(PixelLogicUIUtil.getTextHeight() - GAME_FONT_SIZE);
        }

        public PixelLogicAssets getAssets() {
            return assets;
        }

        public PixelLogicEventManager getEventManager() {
            return eventManager;
        }

        public PixelLogicUIScreenProperties getProperties() {
            return properties;
        }

        public Label getNormalLabel(String text, Color color) {
            Label.LabelStyle style = new Label.LabelStyle(this.normalFont, color);
            return new Label(text, style);
        }

        public Label getSmallLabel(String text, Color color) {
            Label.LabelStyle style = new Label.LabelStyle(this.smallFont, color);
            return new Label(text, style);
        }

    }

}
