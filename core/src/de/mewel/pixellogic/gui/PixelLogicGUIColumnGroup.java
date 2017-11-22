package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SIZE;
import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SPACE_COMBINED;

public class PixelLogicGUIColumnGroup extends Group {

    private PixelLogicLevel level;

    private boolean fadeOutStarted;

    public PixelLogicGUIColumnGroup(PixelLogicLevel level) {
        this.level = level;
        this.fadeOutStarted = false;
        for (int i = 0; i < level.getColumns(); i++) {
            PixelLogicGUIColumnInfo part = new PixelLogicGUIColumnInfo(level, i);
            float x = getX() + (PIXEL_SPACE_COMBINED * i);
            part.setBounds(x, getY(), PIXEL_SIZE, PIXEL_SIZE * 2);
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
