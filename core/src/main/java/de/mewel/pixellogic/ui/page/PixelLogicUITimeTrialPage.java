package de.mewel.pixellogic.ui.page;

import static de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions.Mode;
import static de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialEasyOptions;
import static de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialEpicOptions;
import static de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions;
import static de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialInsaneOptions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialHighscoreStore;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;
import de.mewel.pixellogic.ui.component.PixelLogicUIContainer;

public class PixelLogicUITimeTrialPage extends PixelLogicUIBasePage {

    private List<TimeTrialModeUI> modes;

    private Container<Label> labelContainer;

    public PixelLogicUITimeTrialPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.timeTrial, global.getAssets().translate("play.timeTrial.title"), PixelLogicUIPageId.play);
    }

    @Override
    protected void build() {
        this.modes = new ArrayList<>();
        PixelLogicAssets assets = getAssets();
        this.modes.add(new TimeTrialModeUI(getGlobal(), new PixelLogicTimeTrialEasyOptions(assets.translate("timeTrial.easyMode")), this));
        this.modes.add(new TimeTrialModeUI(getGlobal(), new PixelLogicTimeTrialHardcoreOptions(assets.translate("timeTrial.hardMode")), this));
        this.modes.add(new TimeTrialModeUI(getGlobal(), new PixelLogicTimeTrialInsaneOptions(assets.translate("timeTrial.insaneMode")), this));
        this.modes.add(new TimeTrialModeUI(getGlobal(), new PixelLogicTimeTrialEpicOptions(assets.translate("timeTrial.epicMode")), this));

        /*for(TimeTrialModeUI mode : this.modes) {
            PixelLogicTimeTrialHighscoreStore.clear(mode.options.id.toString());
        }*/
        this.buildModes();
    }

    protected void buildModes() {
        Label descriptionLabel = getDescriptionLabel();
        this.labelContainer = new Container<>(descriptionLabel);
        getPageRoot().addActor(this.labelContainer);
        for (TimeTrialModeUI mode : this.modes) {
            getPageRoot().addActor(mode);
        }
    }

    private Label getDescriptionLabel() {
        Label descriptionLabel = this.getLabel(getAssets().translate("play.timeTrial.description"));
        descriptionLabel.setWrap(true);
        return descriptionLabel;
    }

    @Override
    public void activate(PixelLogicUIPageProperties properties) {
        super.activate(properties);
        for (TimeTrialModeUI mode : this.modes) {
            mode.activate();
        }
        fadeIn(null);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        this.labelContainer.width(getComponentWidth());
        this.labelContainer.getActor().setStyle(getLabelStyle());
        for (TimeTrialModeUI mode : this.modes) {
            mode.resize();
        }
    }

    public Label getLabel(String text) {
        Label.LabelStyle style = getLabelStyle();
        return new Label(text, style);
    }

    private Label.LabelStyle getLabelStyle() {
        BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 0);
        return new Label.LabelStyle(font, Color.WHITE);
    }

    private class TimeTrialModeUI extends PixelLogicUIContainer<VerticalGroup> {

        private final PixelLogicTimeTrialModeOptions options;

        private final PixelLogicUITimeTrialPage page;

        private final PixelLogicUIButton button;

        private final Table highscoreTable;

        private final Container<Table> highscoreContainer;

        public TimeTrialModeUI(PixelLogicGlobal global, final PixelLogicTimeTrialModeOptions options, PixelLogicUITimeTrialPage page) {
            super(global, new VerticalGroup());

            this.page = page;
            this.options = options;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            this.button = new PixelLogicUIButton(getGlobal(), options.name) {
                @Override
                public void handleClick() {
                    startTimeTrial(options);
                }
            };
            this.button.setSize(getButtonWidth(), getButtonHeight());
            getActor().addActor(this.button);

            this.highscoreTable = new Table();
            this.highscoreTable.setFillParent(true);
            updateHighscoreTable();

            this.highscoreContainer = new Container<>(this.highscoreTable);

            getActor().addActor(this.highscoreContainer);
            getActor().pack();
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2f;
            this.pad(padding, 0, padding, padding * 2);
            this.highscoreContainer.width(getComponentWidth());
            updateHighscoreTable();
            this.button.setSize(getButtonWidth(), getButtonHeight());
        }

        public void updateHighscoreTable() {
            this.highscoreTable.clearChildren();
            List<PixelLogicTimeTrialHighscoreStore.Highscore> highscoreList = PixelLogicTimeTrialHighscoreStore.list(options.id.name());

            String highscoreText = getAssets().translate("timeTrial.highscore");
            String timeText = getAssets().translate("timeTrial.time");
            String noGamesText = getAssets().translate("timeTrial.noGames");

            // header
            Label normalHighscoreLabel = page.getLabel("[TEXT_LIGHT_COLOR]" + highscoreText);
            Label normalTimeLabel = page.getLabel("[TEXT_LIGHT_COLOR]" + timeText);
            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();
            highscoreTable.row();

            // line
            PixelLogicUIColoredSurface line = new PixelLogicUIColoredSurface(page.getGlobal());
            line.setColor(new Color(getGlobal().getStyle().getTextSecondaryColor()));
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            highscoreTable.add(line).colspan(2).expand();
            highscoreTable.row();

            // score
            if (!highscoreList.isEmpty()) {
                final Integer rank = page.getProperties().getInt("rank");
                final Mode mode = page.getProperties().get("mode");
                boolean lastRankInvalid = rank == null || rank == -1 || mode == null || !mode.equals(options.id);
                for (int i = 0; i < highscoreList.size(); i++) {
                    String color = lastRankInvalid || rank != i ? "[TEXT_COLOR]" : "[MAIN_COLOR]";
                    PixelLogicTimeTrialHighscoreStore.Highscore highscore = highscoreList.get(i);
                    Label highscoreDate = page.getLabel(color + PixelLogicUIUtil.formatDate(highscore.date));
                    Label highscoreTime = page.getLabel(color + PixelLogicUIUtil.formatMilliseconds(highscore.time));
                    highscoreTable.add(highscoreDate).growX().left();
                    highscoreTable.add(highscoreTime).right();
                    highscoreTable.row();
                }
            } else {
                highscoreTable.add(page.getLabel("[TEXT_COLOR]" + noGamesText)).colspan(2).center();
                highscoreTable.row();
            }
        }

        private void startTimeTrial(final PixelLogicTimeTrialModeOptions options) {
            final PixelLogicTimeTrialMode mode = new PixelLogicTimeTrialMode(options);
            mode.setup(page.getGlobal());

            final PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
            data.put("options", options);
            data.put("mode", options.id);
            data.put("menuBackId", PixelLogicUIPageId.timeTrial);
            page.getAppScreen().setPage(PixelLogicUIPageId.level, data, () -> {
                mode.activate();
                mode.run();
            });
        }

        public void activate() {
            this.button.unblock();
        }
    }

}
