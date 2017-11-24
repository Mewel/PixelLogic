package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;

import static de.mewel.pixellogic.gui.PixelLogicGUIConstants.PIXEL_SCALE;
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
            this.addActor(part);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (int i = 0; i < level.getRows(); i++) {
            Actor actor = this.getChildren().get(i);
            float y = PIXEL_SPACE_COMBINED * PIXEL_SCALE * i;
            actor.setBounds(getX(), y, (PIXEL_SIZE * PIXEL_SCALE) * 2, (PIXEL_SIZE * PIXEL_SCALE));
        }
        if (!fadeOutStarted && level.isSolved()) {
            fadeOutStarted = true;
            this.addAction(Actions.fadeOut(0.4f));
        }
    }

}
