package de.mewel.pixellogic.ui.page;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.ArrayList;
import java.util.List;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialHighscoreStore;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialMode;
import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIColoredSurface;

import static de.mewel.pixellogic.ui.PixelLogicUIConstants.BLOCK_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.GRID_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_COLOR;
import static de.mewel.pixellogic.ui.PixelLogicUIConstants.TEXT_LIGHT_COLOR;

public class PixelLogicUITimeTrialPage extends PixelLogicUIBasePage {

    private List<TimeTrialModeUI> modes;

    private Container<Label> labelContainer;

    public PixelLogicUITimeTrialPage(PixelLogicGlobal global) {
        super(global, PixelLogicUIPageId.timeTrial, "Time trial", PixelLogicUIPageId.mainMenu);
    }

    @Override
    protected void build() {
        this.modes = new ArrayList<TimeTrialModeUI>();
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialEasyOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialHardcoreOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialInsaneOptions(), this));
        this.modes.add(new TimeTrialModeUI(new PixelLogicTimeTrialModeOptions.PixelLogicTimeTrialEpicOptions(), this));

        /*for(TimeTrialModeUI mode : this.modes) {
            PixelLogicTimeTrialHighscoreStore.clear(mode.options.id.toString());
        }*/
        this.buildModes();
    }

    protected void buildModes() {
        Label descriptionLabel = getDescriptionLabel();
        this.labelContainer = new Container<Label>(descriptionLabel);
        getPageRoot().addActor(this.labelContainer);
        for (TimeTrialModeUI mode : this.modes) {
            getPageRoot().addActor(mode);
        }
    }

    private Label getDescriptionLabel() {
        Label descriptionLabel = this.getLabel("Play infinite auto generated levels against the " +
                "clock and try to beat your highscore.", TEXT_COLOR);
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
        this.labelContainer.getActor().setStyle(getLabelStyle(TEXT_COLOR));
        for (TimeTrialModeUI mode : this.modes) {
            mode.resize();
        }
    }

    public Label getLabel(String text, Color color) {
        Label.LabelStyle style = getLabelStyle(color);
        return new Label(text, style);
    }

    private Label.LabelStyle getLabelStyle(Color color) {
        BitmapFont font = PixelLogicUIUtil.getAppFont(getAssets(), 0);
        return new Label.LabelStyle(font, color);
    }

    private class TimeTrialModeUI extends Container<VerticalGroup> {

        private PixelLogicTimeTrialModeOptions options;

        private PixelLogicUITimeTrialPage page;

        private PixelLogicUIButton button;

        private Table highscoreTable;

        private Container<Table> highscoreContainer;

        public TimeTrialModeUI(final PixelLogicTimeTrialModeOptions options, PixelLogicUITimeTrialPage page) {
            super(new VerticalGroup());

            this.page = page;
            this.options = options;

            getActor().setFillParent(true);
            getActor().center();
            getActor().bottom();
            getActor().space(getPadding());

            this.button = new PixelLogicUIButton(page.getAssets(), page.getEventManager(), options.name) {
                @Override
                public void onClick() {
                    if(block()) {
                        return;
                    }
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

            Texture whiteTexture = PixelLogicUIUtil.getTexture(BLOCK_COLOR);
            Sprite s = new Sprite(whiteTexture);
            this.setBackground(new SpriteDrawable(s));
        }

        public void resize() {
            this.width(getComponentWidth());
            float padding = getPadding() / 2;
            this.pad(padding, 0, padding, padding * 2);
            this.highscoreContainer.width(getComponentWidth());
            updateHighscoreTable();
            this.button.setSize(getButtonWidth(), getButtonHeight());
        }

        public void updateHighscoreTable() {
            this.highscoreTable.clearChildren();
            List<PixelLogicTimeTrialHighscoreStore.Highscore> highscoreList = PixelLogicTimeTrialHighscoreStore.list(options.id.name());

            // header
            Label normalHighscoreLabel = page.getLabel("highscore", TEXT_LIGHT_COLOR);
            Label normalTimeLabel = page.getLabel("time", TEXT_LIGHT_COLOR);
            highscoreTable.add(normalHighscoreLabel).growX().left();
            highscoreTable.add(normalTimeLabel).right();
            highscoreTable.row();

            // line
            PixelLogicUIColoredSurface line = new PixelLogicUIColoredSurface(page.getAssets());
            line.setColor(TEXT_LIGHT_COLOR);
            line.setWidth(getComponentWidth());
            line.setHeight(1);
            highscoreTable.add(line).colspan(2).expand();
            highscoreTable.row();

            // score
            if (!highscoreList.isEmpty()) {
                final Integer rank = page.getProperties().getInt("rank");
                final PixelLogicTimeTrialModeOptions.Mode mode = page.getProperties().get("mode");
                boolean lastRankInvalid = rank == null || rank == -1 || mode == null || !mode.equals(options.id);
                for (int i = 0; i < highscoreList.size(); i++) {
                    Color color = lastRankInvalid || rank != i ? TEXT_COLOR : GRID_COLOR;
                    PixelLogicTimeTrialHighscoreStore.Highscore highscore = highscoreList.get(i);
                    Label highscoreDate = page.getLabel(PixelLogicUIUtil.formatDate(highscore.date), color);
                    Label highscoreTime = page.getLabel(PixelLogicUIUtil.formatMilliseconds(highscore.time), color);
                    highscoreTable.add(highscoreDate).growX().left();
                    highscoreTable.add(highscoreTime).right();
                    highscoreTable.row();
                }
            } else {
                highscoreTable.add(page.getLabel("no games played", TEXT_COLOR)).colspan(2).center();
                highscoreTable.row();
            }
        }

        private void startTimeTrial(final PixelLogicTimeTrialModeOptions options) {
            final PixelLogicTimeTrialMode mode = new PixelLogicTimeTrialMode(options);
            mode.setup(page.getGlobal());
            mode.run();

            final PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
            data.put("options", options);
            data.put("mode", options.id);
            data.put("menuBackId", PixelLogicUIPageId.timeTrial);
            page.getAppScreen().setPage(PixelLogicUIPageId.level, data);
        }

        public void activate() {
            this.button.unblock();
        }
    }

}
