package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.PixelLogicConstants;
import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevelMenu extends PixelLogicUIModal {

    protected PixelLogicUILevelPage page;

    protected PixelLogicUIButton solveLevelButton, resetLevelButton, continueButton, backButton;

    protected PixelLogicUIPageId backButtonPageId;

    public PixelLogicUILevelMenu(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUILevelPage page) {
        super(assets, eventManager, page.getStage().getRoot());
        this.page = page;
        this.backButtonPageId = null;
        buildContent();
    }

    protected void buildContent() {

        // solve level
        this.solveLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "solve level") {
            @Override
            public void handleClick() {
                close();
                PixelLogicUtil.solveLevel(PixelLogicUILevelMenu.this.page.getLevel());
                PixelLogicUILevelMenu.this.page.getLevelUI().getBoard().updateFromLevelPixels();
            }
        };

        // reset level
        this.resetLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "reset level") {
            @Override
            public void handleClick() {
                close();
                page.resetLevel();
            }
        };

        // continue
        this.continueButton = new PixelLogicUIButton(getAssets(), getEventManager(), "continue") {
            @Override
            public void handleClick() {
                close();
            }
        };

        // back
        this.backButton = new PixelLogicUIButton(getAssets(), getEventManager(), "back to menu") {
            @Override
            public void handleClick() {
                close();
                back();
            }
        };

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
            getContent().add(this.solveLevelButton).padBottom(padding);
            getContent().row();
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
    protected void afterClose() {
        getEventManager().fire(new PixelLogicUserEvent(this, PixelLogicUserEvent.Type.LEVEL_MENU_CLOSED));
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateButtonSize();
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

}
