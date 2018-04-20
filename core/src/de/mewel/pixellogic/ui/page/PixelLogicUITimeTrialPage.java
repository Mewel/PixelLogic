package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialHighscoreStore;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.GRID_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_LIGHT_COLOR;

public class PixelLogicUITimeTrialPage extends PixelLogicUIPage {

    private List<TimeTrialModeUI> modes;

    private Container<Label> labelContainer;

    public PixelLogicUITimeTrialPage(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);

        this.modes = new ArrayList<TimeTrialModeUI>();
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialNormalOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialInsaneOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialEpicOptions(), this));

        /*for(TimeTrialModeUI mode : this.modes) {
            PixelLogicTimeTrialHighscoreStore.clear(mode.options.id);
        }*/
        this.buildGUI();
    }

    protected void buildGUI() {
        // labels
        Label descriptionLabel = getDescriptionLabel();

        // combine to VerticalGroup
        VerticalGroup root = new VerticalGroup();
        root.center();
        root.bottom();
        root.pad(getPadding());
        root.space(getPadding() * 2.5f);

        this.labelContainer = new Container<Label>(descriptionLabel);
        root.addActor(this.labelContainer);
        for (TimeTrialModeUI mode : this.modes) {
            root.addActor(mode);
        }
        ScrollPane scrollPane = new ScrollPane(root);
        getStage().addActor(scrollPane);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setClamp(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setFillParent(true);
    }

    private Label getDescriptionLabel() {
        Label descriptionLabel = this.getLabel("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", TEXT_COLOR);
        descriptionLabel.setWrap(true);
        return descriptionLabel;
    }

    public static int getPadding() {
        return Gdx.graphics.getHeight() / 24;
    }

    public static int getComponentWidth() {
        return Gdx.graphics.getWidth() - 2 * getPadding();
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        getStage().getRoot().getColor().a = 0f;
        getStage().addAction(Actions.fadeIn(.2f));
    }

    @Override
    public void deactivate(Runnable after) {
        Action action = Actions.sequence(Actions.fadeOut(.2f), Actions.run(after));
        this.getStage().addAction(action);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.labelContainer.width(getComponentWidth());
        this.labelContainer.getActor().setStyle(getLabelStyle(TEXT_COLOR));
        for (TimeTrialModeUI mode : this.modes) {
            mode.resize();
        }
    }

    private static int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private static int getButtonWidth() {
        return getButtonHeight() * 4;
    }

    public Label getLabel(String text, Color color) {
        Label.LabelStyle style = getLabelStyle(color);
        return new Label(text, style);
    }

    private Label.LabelStyle getLabelStyle(Color color) {
        BitmapFont font = PixelLogicUIUtil.getSmallAppFont(getAssets());
        return new Label.LabelStyle(font, color);
    }

    private static class TimeTrialModeUI extends Container<VerticalGroup> {

        private PixelLogicTimeTrialModeOptions options;

        private PixelLogicUITimeTrialPage screen;

        PixelLogicUIButton button;

        private Table highscoreTable;

        private Container<Table> highscoreContainer;

        public TimeTrialModeUI(final PixelLogicTimeTrialModeOptions options, PixelLogicUITimeTrialPage screen) {
            super(new VerticalGroup());
            this.screen = screen;
            this.options = options;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            this.button = new PixelLogicUIButton(screen.getAssets(), screen.getEventManager(), options.name) {
                @Override
                public void onClick() {
                    startTimeTrial(options);
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            this.highscoreTable = new Table();
            this.highscoreTable.setFillParent(true);
            updateHighscoreTable();

            this.highscoreContainer = new Container<Table>(this.highscoreTable);

            getActor().addActor(this.highscoreContainer);
            getActor().pack();
        }

        public void resize() {
            this.width(getComponentWidth());
            this.highscoreContainer.width(getComponentWidth());
            updateHighscoreTable();
            this.button.setSize(getButtonWidth(), getButtonHeight());
        }

        public void updateHighscoreTable() {
            this.highscoreTable.clearChildren();
            List<PixelLogicTimeTrialHighscoreStore.Highscore> highscoreList = PixelLogicTimeTrialHighscoreStore.list(options.id);

            // header
            Label normalHighscoreLabel = screen.getLabel("highscore", TEXT_LIGHT_COLOR);
            Label normalTimeLabel = screen.getLabel("time", TEXT_LIGHT_COLOR);
            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();
            highscoreTable.row();

            // line
            PixelLogicUIColoredSurface line = new PixelLogicUIColoredSurface(screen.getAssets(), screen.getEventManager());
            line.setColor(TEXT_LIGHT_COLOR);
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            highscoreTable.add(line).colspan(2).expand();
            highscoreTable.row();

            // score
            if (!highscoreList.isEmpty()) {
                final Integer rank = screen.getProperties().getInt("rank");
                final String mode = screen.getProperties().getString("mode");
                boolean lastRankInvalid = rank == null || rank == -1 || mode == null || !mode.equals(options.id);
                for (int i = 0; i < highscoreList.size(); i++) {
                    Color color = lastRankInvalid || rank != i ? TEXT_COLOR : GRID_COLOR;
                    PixelLogicTimeTrialHighscoreStore.Highscore highscore = highscoreList.get(i);
                    Label highscoreDate = screen.getLabel(PixelLogicUIUtil.formatDate(highscore.date), color);
                    Label highscoreTime = screen.getLabel(PixelLogicUIUtil.formatMilliseconds(highscore.time), color);
                    highscoreTable.add(highscoreDate).growX().left();
                    highscoreTable.add(highscoreTime).right();
                    highscoreTable.row();
                }
            } else {
                highscoreTable.add(screen.getLabel("no games played", TEXT_COLOR)).colspan(2).center();
                highscoreTable.row();
            }
        }

        private void startTimeTrial(final PixelLogicTimeTrialModeOptions options) {
            final PixelLogicTimeTrialMode mode = new PixelLogicTimeTrialMode(options);
            mode.setup(screen.getAssets(), screen.getEventManager());
            mode.run();
        }

    }

}
