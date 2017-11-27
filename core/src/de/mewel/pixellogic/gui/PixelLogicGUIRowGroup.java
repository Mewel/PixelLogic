package de.mewel.pixellogic.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.model.PixelLogicLevel;

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
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), level);
        for (int i = 0; i < level.getRows(); i++) {
            Actor actor = this.getChildren().get(i);
            float y = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(getX(), y, (resolution.getGamePixelSize()) * 2, (resolution.getGamePixelSize()));
        }
        if (!fadeOutStarted && level.isSolved()) {
            fadeOutStarted = true;
            this.addAction(Actions.fadeOut(0.4f));
        }
    }

}
