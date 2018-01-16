package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;
import de.mewel.pixellogic.ui.screen.PixelLogicLevelScreen;

public class PixelLogicGUILevelMenu extends PixelLogicUIModal {

    private PixelLogicLevelScreen screen;

    private PixelLogicUIButton resetLevelButton, continueButton;

    public PixelLogicGUILevelMenu(PixelLogicLevelScreen screen) {
        super(screen.getStage().getRoot());
        this.screen = screen;
    }

    @Override
    protected void buildContent(Table content) {
        // reset level
        this.resetLevelButton = new PixelLogicUIButton("reset level");
        this.resetLevelButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicGUILevelMenu.this.close();
                PixelLogicGUILevelMenu.this.screen.resetLevel();
            }
        });

        // continue
        this.continueButton = new PixelLogicUIButton("continue");
        this.continueButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                PixelLogicGUILevelMenu.this.close();
            }
        });

        this.updateButtonSize();

        // add to contet
        int padding = PixelLogicGUILevelResolutionManager.instance().getBaseHeight() / 2;
        content.add(this.continueButton).padBottom(padding);
        content.row();
        content.add(this.resetLevelButton).padBottom(padding);

    }

    private int getButtonHeight() {
        return PixelLogicGUILevelResolutionManager.instance().getBaseHeight();
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

        this.resetLevelButton.setSize(buttonWidth, buttonHeight);
        this.continueButton.setSize(buttonWidth, buttonHeight);
    }

}
