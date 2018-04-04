package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicUserEvent;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;
import de.mewel.pixellogic.ui.screen.PixelLogicUIScreenProperties;
import de.mewel.pixellogic.ui.screen.event.PixelLogicScreenChangeEvent;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevelMenu extends PixelLogicUIModal {

    private PixelLogicUILevelScreen screen;

    private PixelLogicUIButton solveLevelButton, resetLevelButton, continueButton, backButton;

    private String backButtonScreenId;

    public PixelLogicUILevelMenu(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUILevelScreen screen) {
        super(assets, eventManager, screen.getStage().getRoot());
        this.screen = screen;
        this.backButtonScreenId = null;
        buildContent();
    }

    protected void buildContent() {

        // solve level
        this.solveLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "solve level");
        this.solveLevelButton.addListener(new MenuCloseListener() {
            @Override
            void afterClose(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUtil.solveLevel(PixelLogicUILevelMenu.this.screen.getLevel());
            }
        });

        // reset level
        this.resetLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "reset level");
        this.resetLevelButton.addListener(new MenuCloseListener() {
            @Override
            void afterClose(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUILevelMenu.this.screen.resetLevel();
            }
        });

        // continue
        this.continueButton = new PixelLogicUIButton(getAssets(), getEventManager(), "continue");
        this.continueButton.addListener(new MenuCloseListener());

        // back
        this.backButton = new PixelLogicUIButton(getAssets(), getEventManager(), "back to menu");
        this.backButton.addListener(new MenuCloseListener() {
            @Override
            public void afterClose(InputEvent event, float x, float y, int pointer, int button) {
                if (backButtonScreenId == null) {
                    Gdx.app.log("level menu", "pushing back button without screen id, this should never happen!");
                    return;
                }
                PixelLogicUIScreenProperties data = new PixelLogicUIScreenProperties();
                data.put("screenId", backButtonScreenId);
                data.put("backButton", true);
                getEventManager().fire(new PixelLogicScreenChangeEvent(PixelLogicUILevelMenu.this, data));
            }
        });
    }

    public void activate(PixelLogicUIScreenProperties properties) {
        int padding = PixelLogicUIUtil.getBaseHeight() / 2;
        getContent().add(this.continueButton).padBottom(padding * 2);
        getContent().row();

        this.backButtonScreenId = properties.getString("menu_back_id");
        if (this.backButtonScreenId != null) {
            getContent().add(this.backButton).padBottom(padding);
            getContent().row();
        }

        getContent().add(this.resetLevelButton).padBottom(padding);
        getContent().row();
        getContent().add(this.solveLevelButton).padBottom(padding);

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

    private class MenuCloseListener extends InputListener {

        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            PixelLogicUILevelMenu.this.close();
            afterClose(event, x, y, pointer, button);
        }

        void afterClose(InputEvent event, float x, float y, int pointer, int button) {
        }

    }

}
