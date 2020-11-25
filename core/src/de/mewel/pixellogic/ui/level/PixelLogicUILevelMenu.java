package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.component.PixelLogicUISettings;
import de.mewel.pixellogic.ui.component.PixelLogicUISprite;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevelMenu extends PixelLogicUIModal {

    protected PixelLogicUILevelPage page;

    protected PixelLogicUIButton solveLevelButton, resetLevelButton, continueButton, backButton;

    protected PixelLogicUIPageId backButtonPageId;

    private Label statusLabel;

    private PixelLogicUISprite settingsButton;

    private PixelLogicUISettings settings;

    public PixelLogicUILevelMenu(PixelLogicGlobal global, PixelLogicUILevelPage page) {
        super(global, page.getStage().getRoot());
        this.page = page;
        this.backButtonPageId = null;
        this.statusLabel = null;
        buildContent();
    }

    protected void buildContent() {

        // solve level
        this.solveLevelButton = new PixelLogicUIButton(getGlobal(), "solve level") {
            @Override
            public void handleClick() {
                close();
                PixelLogicUtil.solveLevel(PixelLogicUILevelMenu.this.page.getLevel());
                PixelLogicUILevelMenu.this.page.getLevelUI().getBoard().updateFromLevelPixels();
            }
        };

        // reset level
        this.resetLevelButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("level.menu.reset")) {
            @Override
            public void handleClick() {
                close();
                page.resetLevel();
            }
        };

        // continue
        this.continueButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("level.menu.continue")) {
            @Override
            public void handleClick() {
                close();
            }
        };

        // back
        this.backButton = new PixelLogicUIButton(getGlobal(), getAssets().translate("level.menu.back")) {
            @Override
            public void handleClick() {
                close();
                back();
            }
        };

        // settings
        this.settingsButton = new PixelLogicUISprite(getGlobal(), 10);
        this.settingsButton.setColor(Color.WHITE);
        this.settingsButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                settings.show(getStage().getRoot());
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        // settings
        this.settings = new PixelLogicUISettings(getGlobal());
    }

    @Override
    public void show() {
        super.show();
        if (this.solveLevelButton != null) {
            this.solveLevelButton.unblock();
        }
        if (this.resetLevelButton != null) {
            this.resetLevelButton.unblock();
        }
        if (this.continueButton != null) {
            this.continueButton.unblock();
        }
        if (this.backButton != null) {
            this.backButton.unblock();
        }
    }

    public void activate(PixelLogicUIPageProperties properties) {
        int padding = PixelLogicUIUtil.getBaseHeight() / 2;

        if (this.solveLevelButton != null) {
             // getContent().add(this.solveLevelButton).padBottom(padding);
             // getContent().row();
        }
        if (this.resetLevelButton != null) {
            getContent().add(this.resetLevelButton).padBottom(padding);
            getContent().row();
        }
        this.backButtonPageId = properties.get("menuBackId");
        if (this.backButtonPageId != null) {
            getContent().add(this.backButton).padBottom(padding);
            getContent().row();
        }
        if (this.continueButton != null) {
            getContent().add(this.continueButton).padTop(padding);
        }
        if (this.settingsButton != null) {
            this.addActor(settingsButton);
        }
        this.updateButtonSize();
    }

    public void deactivate() {
        getContent().clearChildren();
    }

    private int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private int getButtonWidth() {
        return getButtonHeight() * 4;
    }

    protected void back() {
        if (backButtonPageId == null) {
            Gdx.app.log("level menu", "pushing back button without page id, this should never happen!");
            return;
        }
        this.page.getAppScreen().setPage(backButtonPageId);
    }

    @Override
    public void close() {
        if (this.settings != null) {
            this.settings.remove();
        }
        super.close();
    }

    @Override
    protected void afterClose() {
        getEventManager().fire(new PixelLogicUserEvent(this, PixelLogicUserEvent.Type.LEVEL_MENU_CLOSED));
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateButtonSize();
        this.updateStatusLabelPosition();
        if (settingsButton != null) {
            int iconSize = PixelLogicUIUtil.getIconBaseHeight();
            this.settingsButton.setSize(iconSize, iconSize);
            this.settingsButton.pad(iconSize);
            this.settingsButton.setPosition(getWidth() - this.settingsButton.getWidth(), 0);
        }
        if (settings != null) {
            this.settings.setWidth(getWidth());
        }
    }

    public void setStatusText(String text) {
        if (this.statusLabel == null) {
            BitmapFont labelFont = PixelLogicUIUtil.getAppFont(getAssets(), 1);
            Label.LabelStyle style = new Label.LabelStyle(labelFont, new Color(Color.WHITE));
            this.statusLabel = new Label(text, style);
            this.addActor(this.statusLabel);
        } else {
            this.statusLabel.setText(text);
        }
        this.updateStatusLabelPosition();
    }

    public void clearStatusText() {
        if (this.statusLabel != null) {
            this.statusLabel.remove();
        }
        this.statusLabel = null;
    }

    private void updateButtonSize() {
        int buttonWidth = getButtonWidth();
        int buttonHeight = getButtonHeight();
        if (this.solveLevelButton != null) {
            this.solveLevelButton.setSize(buttonWidth, buttonHeight);
        }
        if (this.resetLevelButton != null) {
            this.resetLevelButton.setSize(buttonWidth, buttonHeight);
        }
        if (this.continueButton != null) {
            this.continueButton.setSize(buttonWidth, buttonHeight);
        }
        if (this.backButton != null) {
            this.backButton.setSize(buttonWidth, buttonHeight);
        }
    }

    private void updateStatusLabelPosition() {
        if (this.statusLabel != null) {
            int x = (int) (getWidth() / 2 - this.statusLabel.getWidth() / 2);
            int y = (int) (this.getHeight() - (this.getHeight() / 20) - this.statusLabel.getHeight());
            this.statusLabel.setPosition(x, y);
        }
    }

}
