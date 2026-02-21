package de.mewel.pixellogic.ui.component;

import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND;
import static de.mewel.pixellogic.PixelLogicConstants.BUTTON_SOUND_VOLUME;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.asset.PixelLogicAudio;
import de.mewel.pixellogic.event.PixelLogicStyleChangedEvent;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.style.PixelLogicUIStyle;

public class PixelLogicUISettings extends PixelLogicUIGroup {

    private final PixelLogicUIColoredSurface background;

    private final Table container;

    private final Label musicLabel;
    private final Label soundLabel;
    private final Label autoBlockLabel;
    private final Label darkModeLabel;

    private final PixelLogicUISwitchSpriteButton musicButton;
    private final PixelLogicUISwitchSpriteButton soundButton;
    private final PixelLogicUISwitchSpriteButton autoBlockButton;
    private final PixelLogicUISwitchSpriteButton darkModeButton;

    private final PixelLogicUISprite closeButton;

    private boolean shown;

    public PixelLogicUISettings(PixelLogicGlobal global) {
        super(global);
        this.shown = false;
        this.container = new Table();
        this.container.setFillParent(true);

        this.background = new PixelLogicUIColoredSurface(global);
        this.addActor(this.background);

        this.musicLabel = new Label("Music", getLabelStyle());
        this.soundLabel = new Label("Sound", getLabelStyle());
        this.autoBlockLabel = new Label("Auto-Block", getLabelStyle());
        this.darkModeLabel = new Label("Dark Mode", getLabelStyle());

        this.musicButton = new PixelLogicUISwitchSpriteButton(global, 8, 9);
        this.soundButton = new PixelLogicUISwitchSpriteButton(global, 8, 9);
        this.autoBlockButton = new PixelLogicUISwitchSpriteButton(global, 11, 12);
        this.darkModeButton = new PixelLogicUISwitchSpriteButton(global, 11, 12);
        this.closeButton = new PixelLogicUISprite(global, 3);
        this.closeButton.getSprite().rotate90(false);
        this.styleChanged(global.getStyle());

        this.container.add(this.musicLabel).expandX().left();
        this.container.add(this.musicButton).top();
        this.container.row();
        this.container.add(this.soundLabel).expandX().left();
        this.container.add(this.soundButton).top();
        this.container.row();
        this.container.add(this.autoBlockLabel).expandX().left();
        this.container.add(this.autoBlockButton).top();
        this.container.row();
        this.container.add(this.darkModeLabel).expandX().left();
        this.container.add(this.darkModeButton).top();
        this.container.row();
        this.container.add(this.closeButton).center().colspan(2);
        this.addActor(this.container);

        this.update();

        // input
        this.closeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
                close();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.musicButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicAudio audio = getAudio();
                if (audio.isMusicMuted()) {
                    audio.unmuteMusic();
                    audio.playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
                    if (PixelLogicUIPageId.mainMenu.equals(getGlobal().getAppScreen().getCurrentPage().getPageId())) {
                        audio.playMenuMusic();
                    } else {
                        audio.playLevelMusic();
                    }
                } else {
                    audio.muteMusic();
                }
                musicButton.switchButton();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.soundButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicAudio audio = getAudio();
                if (audio.isSoundMuted()) {
                    audio.unmuteSound();
                } else {
                    audio.muteSound();
                }
                soundButton.switchButton();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.autoBlockButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                getPreferences().putBoolean("autoBlock", !isAutoBlockEnabled()).flush();
                autoBlockButton.switchButton();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        this.darkModeButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUIStyle style = getGlobal().getStyleController().switchStyle();
                darkModeButton.switchButton();
                getEventManager().fire(new PixelLogicStyleChangedEvent(PixelLogicUISettings.this, style));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    protected void update() {
        PixelLogicAudio audio = getGlobal().getAudio();
        this.musicButton.setActive(!audio.isMusicMuted());
        this.soundButton.setActive(!audio.isSoundMuted());
        this.autoBlockButton.setActive(!isAutoBlockEnabled());
        this.darkModeButton.setActive(!getGlobal().getStyle().getName().equals("dark"));
    }

    protected boolean isAutoBlockEnabled() {
        return getPreferences().getBoolean("autoBlock", false);
    }

    @Override
    public void styleChanged(PixelLogicUIStyle style) {
        super.styleChanged(style);
        Color textColor = style.getTextColor();
        this.musicButton.setColor(textColor);
        this.soundButton.setColor(textColor);
        this.autoBlockButton.setColor(textColor);
        this.darkModeButton.setColor(textColor);
        this.closeButton.setColor(textColor);

        this.background.setColor(getGlobal().getStyle().getBlockColor());
        this.background.getColor().a = .8f;

        Color fontColor = getLabelStyle().fontColor;
        this.musicLabel.getStyle().fontColor = fontColor;
        this.soundLabel.getStyle().fontColor = fontColor;
        this.autoBlockLabel.getStyle().fontColor = fontColor;
        this.darkModeLabel.getStyle().fontColor = fontColor;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        int space = getSpace();
        int padding = getPadding();
        int iconSize = PixelLogicUIUtil.getIconBaseHeight();
        this.container.pad(padding / 2f, padding, padding / 6f, padding / 2f);
        this.container.getCell(this.musicLabel).spaceBottom(space);
        this.container.getCell(this.soundLabel).spaceBottom(space);
        this.container.getCell(this.autoBlockLabel).spaceBottom(space);
        this.container.getCell(this.darkModeLabel).spaceBottom(space);
        if (this.background != null) {
            this.background.setSize(this.getWidth(), this.getHeight());
        }
        this.musicButton.setSize(iconSize, iconSize);
        this.soundButton.setSize(iconSize, iconSize);
        this.autoBlockButton.setSize(iconSize, iconSize);
        this.darkModeButton.setSize(iconSize, iconSize);
        this.closeButton.setSize(iconSize, iconSize);

        this.musicButton.pad(iconSize / 3);
        this.soundButton.pad(iconSize / 3);
        this.autoBlockButton.pad(iconSize / 3);
        this.darkModeButton.pad(iconSize / 3);
        this.closeButton.pad(iconSize / 3);

        float prefHeight = this.container.getPrefHeight();
        if (this.getHeight() != prefHeight) {
            this.setHeight(prefHeight);
            this.sizeChanged();
        }
    }

    private Label.LabelStyle getLabelStyle() {
        BitmapFont labelFont = PixelLogicUIUtil.getMainFont(getAssets());
        return new Label.LabelStyle(labelFont, getGlobal().getStyle().getTextColor());
    }

    private int getPadding() {
        return PixelLogicUIUtil.getBaseHeight() / 3;
    }

    private int getSpace() {
        return getPadding() / 3;
    }

    private Preferences getPreferences() {
        return Gdx.app.getPreferences("pixellogic_settings");
    }

    public void show(Group parent) {
        this.update();
        getAudio().playSound(BUTTON_SOUND, BUTTON_SOUND_VOLUME);
        parent.addActor(this);
        this.setPosition(0, -this.getHeight());
        this.addAction(Actions.moveBy(0, this.getHeight(), .2f));
        this.shown = true;
    }

    public void close() {
        addAction(Actions.moveBy(0, -getHeight(), .2f));
        shown = false;
    }

    public boolean isShown() {
        return shown;
    }

}
