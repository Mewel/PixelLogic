package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;
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
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.GRID_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_LIGHT_COLOR;

public class PixelLogicUITimeTrialScreen extends PixelLogicUIScreen {

    private AssetStore assetStore;

    private List<TimeTrialModeUI> modes;

    public PixelLogicUITimeTrialScreen(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.assetStore = new AssetStore(getAssets(), getEventManager(), getProperties());
        this.modes = new ArrayList<TimeTrialModeUI>();
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialNormalOptions(), assetStore));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions(), assetStore));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialInsaneOptions(), assetStore));

        /*for(TimeTrialModeUI mode : this.modes) {
            PixelLogicTimeTrialHighscoreStore.clear(mode.options.id);
        }*/
        this.buildGUI();
    }

    protected void buildGUI() {
        // labels
        Label descriptionLabel = assetStore.getSmallLabel("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", TEXT_COLOR);
        descriptionLabel.setWrap(true);

        // combine to VerticalGroup
        VerticalGroup root = new VerticalGroup();
        root.center();
        root.bottom();
        root.reverse();
        root.pad(getPadding());
        root.space(getPadding() * 2.5f);

        Container<Label> labelContainer = new Container<Label>(descriptionLabel);
        labelContainer.width(getComponentWidth());
        root.addActor(labelContainer);
        for(TimeTrialModeUI mode : this.modes) {
            root.addActor(mode);
        }
        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setClamp(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFillParent(true);

        getStage().addActor(scrollPane);
        root.layout();

        scrollPane.layout();
        scrollPane.scrollTo(0, 0, 0, 0);
        scrollPane.updateVisualScroll();
    }

    public static int getPadding() {
        return Gdx.graphics.getHeight() / 24;
    }

    public static int getComponentWidth() {
        return Gdx.graphics.getWidth() - 2 * getPadding();
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
        for(TimeTrialModeUI mode : this.modes) {
            mode.updateHighscoreTable(properties);
        }
    }

    @Override
    public void deactivate(Runnable after) {
        super.deactivate(after);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    private static int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private static int getButtonWidth() {
        return getButtonHeight() * 4;
    }

    private static class TimeTrialModeUI extends Container<VerticalGroup> {

        private PixelLogicTimeTrialModeOptions options;

        private AssetStore assetStore;

        private Table highscoreTable;

        public TimeTrialModeUI(final PixelLogicTimeTrialModeOptions options, AssetStore assetStore) {
            super(new VerticalGroup());
            this.assetStore = assetStore;
            this.options = options;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().reverse();
            getActor().space(getPadding());

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

            this.highscoreTable = new Table();
            this.highscoreTable.setFillParent(true);
            updateHighscoreTable(assetStore.getProperties());

            Container<Table> highscoreContainer = new Container<Table>(this.highscoreTable);
            highscoreContainer.width(getComponentWidth());

            getActor().addActor(highscoreContainer);
            getActor().pack();
        }

        public void updateHighscoreTable(PixelLogicUIScreenProperties properties) {
            this.highscoreTable.clearChildren();
            List<PixelLogicTimeTrialHighscoreStore.Highscore> highscoreList = PixelLogicTimeTrialHighscoreStore.list(options.id);
            if (!highscoreList.isEmpty()) {
                final Integer rank = properties.getInt("rank");
                final String mode = properties.getString("mode");
                boolean lastRankInvalid = rank == null || rank == -1 || mode == null || !mode.equals(options.id);
                for (int i = highscoreList.size() - 1; i >= 0; i--) {
                    Color color = lastRankInvalid || rank != i ? TEXT_COLOR : GRID_COLOR;
                    PixelLogicTimeTrialHighscoreStore.Highscore highscore = highscoreList.get(i);
                    Label highscoreDate = assetStore.getSmallLabel(PixelLogicUIUtil.formatDate(highscore.date), color);
                    Label highscoreTime = assetStore.getSmallLabel(PixelLogicUIUtil.formatMilliseconds(highscore.time), color);
                    highscoreTable.add(highscoreDate).growX().left();
                    highscoreTable.add(highscoreTime).right();
                    highscoreTable.row();
                }
            } else {
                highscoreTable.add(assetStore.getSmallLabel("no games played", TEXT_COLOR)).colspan(2).center();
                highscoreTable.row();
            }
            PixelLogicUIHorizontalLine line = new PixelLogicUIHorizontalLine(assetStore.getAssets(), assetStore.getEventManager());
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            line.setColor(TEXT_LIGHT_COLOR);
            highscoreTable.add(line).colspan(2).expand();
            highscoreTable.row();

            Label normalHighscoreLabel = assetStore.getSmallLabel("highscore", TEXT_LIGHT_COLOR);
            Label normalTimeLabel = assetStore.getSmallLabel("time", TEXT_LIGHT_COLOR);
            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();
        }

        private void startTimeTrial(PixelLogicTimeTrialModeOptions options) {
            PixelLogicLevelMode mode = new PixelLogicTimeTrialMode(options);
            mode.setup(assetStore.getAssets(), assetStore.getEventManager());
            mode.run();
            PixelLogicUIScreenProperties data = new PixelLogicUIScreenProperties();
            data.put("screenId", "level");
            data.put("options", options);
            data.put("menu_back_id", "timeTrial");
            assetStore.getEventManager().fire(new PixelLogicScreenChangeEvent(this, data));
        }

        public PixelLogicTimeTrialModeOptions getOptions() {
            return options;
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
