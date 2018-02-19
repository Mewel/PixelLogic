package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;
import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.screen.PixelLogicUILevelScreen;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicUILevelMenu extends PixelLogicUIModal {

    private PixelLogicUILevelScreen screen;

    private PixelLogicUIButton solveLevelButton, resetLevelButton, continueButton;

    public PixelLogicUILevelMenu(PixelLogicAssets assets, PixelLogicEventManager eventManager, PixelLogicUILevelScreen screen) {
        super(assets, eventManager, screen.getStage().getRoot());
        this.screen = screen;
    }

    @Override
    protected void buildContent(Table content) {

        // solve level
        this.solveLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "solve level");
        this.solveLevelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUILevelMenu.this.close();
                PixelLogicUtil.solveLevel(PixelLogicUILevelMenu.this.screen.getLevel());
            }
        });

        // reset level
        this.resetLevelButton = new PixelLogicUIButton(getAssets(), getEventManager(), "reset level");
        this.resetLevelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUILevelMenu.this.close();
                PixelLogicUILevelMenu.this.screen.resetLevel();
            }
        });

        // continue
        this.continueButton = new PixelLogicUIButton(getAssets(), getEventManager(), "continue");
        this.continueButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicUILevelMenu.this.close();
            }
        });

        this.updateButtonSize();

        // add to contet
        int padding = PixelLogicUIUtil.getBaseHeight() / 2;
        content.add(this.continueButton).padBottom(padding);
        content.row();
        content.add(this.resetLevelButton).padBottom(padding);
        content.row();
        content.add(this.solveLevelButton).padBottom(padding);

    }

    private int getButtonHeight() {
        return PixelLogicUIUtil.getBaseHeight();
    }

    private int getButtonWidth() {
        return getButtonHeight() * 4;
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
    }

}
