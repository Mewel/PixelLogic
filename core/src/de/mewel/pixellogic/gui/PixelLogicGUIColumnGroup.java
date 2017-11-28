package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIColumnGroup extends Group {

    private PixelLogicLevel level;

    private boolean fadeOutStarted;

    public PixelLogicGUIColumnGroup(PixelLogicLevel level) {
        this.level = level;
        this.fadeOutStarted = false;
        for (int i = 0; i < level.getColumns(); i++) {
            PixelLogicGUIColumnInfo part = new PixelLogicGUIColumnInfo(level, i);
            this.addActor(part);
        }
    }

    @Override
    public void act(float delta) {
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        for (int i = 0; i < level.getColumns(); i++) {
            Actor actor = this.getChildren().get(i);
            float x = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(x, getY(), (resolution.getGamePixelSize()), (resolution.getGamePixelSize()) * 2);
        }
        super.act(delta);
        if (!fadeOutStarted && level.isSolved()) {
            fadeOutStarted = true;
            this.addAction(Actions.fadeOut(0.4f));
        }
    }

}
