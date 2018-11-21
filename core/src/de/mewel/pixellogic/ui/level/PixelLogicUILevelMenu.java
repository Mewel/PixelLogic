package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.page.PixelLogicUILevelPage;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageId;
import de.mewel.pixellogic.ui.page.PixelLogicUIPageProperties;
import de.mewel.pixellogic.ui.page.event.PixelLogicUIPageChangeEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevelMenu extends PixelLogicUIModal {

    private PixelLogicUILevelPage screen;

    private PixelLogicUIButton solveLevelButton, resetLevelButton, continueButton, backButton;

    private PixelLogicUIPageId backButtonScreenId;

    public PixelLogicUILevelMenu(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUILevelPage screen) {
        super(assets, eventManager, screen.getStage().getRoot());
        this.screen = screen;
        this.backButtonScreenId = null;
        buildContent();
    }

    protected void buildContent() {

        // solve level
        this.solveLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "solve level") {
            @Override
            public void onClick() {
                if(block()) {
                    return;
                }
                close();
                PixelLogicUtil.solveLevel(PixelLogicUILevelMenu.this.screen.getLevel());
                PixelLogicUILevelMenu.this.screen.getLevelUI().getBoard().updateFromLevelPixels();
            }
        };

        // reset level
        this.resetLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "reset level") {
            @Override
            public void onClick() {
                if(block()) {
                    return;
                }
                close();
                screen.resetLevel();
            }
        };

        // continue
        this.continueButton = new PixelLogicUIButton(getAssets(), getEventManager(), "continue") {
            @Override
            public void onClick() {
                if(block()) {
                    return;
                }
                close();
            }
        };

        // back
        this.backButton = new PixelLogicUIButton(getAssets(), getEventManager(), "back to menu") {
            @Override
            public void onClick() {
                if(block()) {
                    return;
                }
                close();
                back();
            }
        };

    }

    @Override
    public void show() {
        super.show();
        if(this.solveLevelButton != null) {
            this.solveLevelButton.unblock();
        }
        if(this.resetLevelButton != null) {
            this.resetLevelButton.unblock();
        }
        if(this.continueButton != null) {
            this.continueButton.unblock();
        }
        if(this.backButton != null) {
            this.backButton.unblock();
        }
    }

    public void activate(PixelLogicUIPageProperties properties) {
        int padding = PixelLogicUIUtil.getBaseHeight() / 2;

        getContent().add(this.solveLevelButton).padBottom(padding);
        getContent().row();
        getContent().add(this.resetLevelButton).padBottom(padding);
        getContent().row();

        this.backButtonScreenId = properties.get("menuBackId");
        if (this.backButtonScreenId != null) {
            getContent().add(this.backButton).padBottom(padding);
            getContent().row();
        }
        getContent().add(this.continueButton).padTop(padding);

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

    private void back() {
        if (backButtonScreenId == null) {
            Gdx.app.log("level menu", "pushing back button without screen id, this should never happen!");
            return;
        }
        PixelLogicUIPageProperties data = new PixelLogicUIPageProperties();
        data.put("pageId", backButtonScreenId);
        data.put("backButton", true);
        getEventManager().fire(new PixelLogicUIPageChangeEvent(PixelLogicUILevelMenu.this, data));
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

        this.solveLevelButton.setSize(buttonWidth, buttonHeight);
        this.resetLevelButton.setSize(buttonWidth, buttonHeight);
        this.continueButton.setSize(buttonWidth, buttonHeight);
        this.backButton.setSize(buttonWidth, buttonHeight);
    }

}
