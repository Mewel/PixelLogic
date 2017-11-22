package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

public class PixelLogicGUIRowGroup extends Group {

    private PixelLogicLevel level;

    private boolean fadeOutStarted;

    public PixelLogicGUIRowGroup(PixelLogicLevel level) {
        this.level = level;
        this.fadeOutStarted = false;
        for (int i = 0; i < level.getRows(); i++) {
            PixelLogicGUIRowInfo part = new PixelLogicGUIRowInfo(level, i);
            float y = getX() + (PIXEL_SPACE_COMBINED * i);
            part.setBounds(getX(), y, PIXEL_SIZE * 2, PIXEL_SIZE);
            this.addActor(part);
        }
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        if (!fadeOutStarted && level.isSolved()) {
            fadeOutStarted = true;
            this.addAction(Actions.fadeOut(1f));
        }
    }


}
