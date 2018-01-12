package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import de.mewel.pixellogic.ui.component.PixelLogicUIButton;
import de.mewel.pixellogic.ui.component.PixelLogicUIModal;

public class PixelLogicGUILevelMenu extends PixelLogicUIModal {

    public PixelLogicGUILevelMenu(Group parent) {
        super(parent);
    }

    @Override
    protected void buildContent(Table content) {
        content.setDebug(true);

        PixelLogicUIButton continueButton = new PixelLogicUIButton("Continue");
        content.setFillParent(true);
        content.add(continueButton);
    }

}
