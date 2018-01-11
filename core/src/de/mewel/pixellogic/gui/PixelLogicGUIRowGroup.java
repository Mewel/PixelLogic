package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIRowGroup extends Group implements PixelLogicLevelChangeListener {

    private PixelLogicLevel level;

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicGUIRowGroup() {
        this.level = null;
        this.changeAdapter = new PixelLogicLevelChangeAdapter();
        this.changeAdapter.bind(this);
    }

    @Override
    public void onLevelChange(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelLoad(PixelLogicLevelChangeEvent event) {
        this.level = event.getLevel();
        for (int i = 0; i < level.getRows(); i++) {
            PixelLogicGUIRowInfo part = new PixelLogicGUIRowInfo(level, i);
            this.addActor(part);
        }
    }

    @Override
    public void onLevelSolved(PixelLogicLevelChangeEvent event) {
        this.addAction(Actions.fadeOut(0.4f));
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelChangeEvent event) {
        this.level = null;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        for (int i = 0; i < level.getRows(); i++) {
            Actor actor = this.getChildren().get(i);
            float y = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(getX(), y, (resolution.getGamePixelSize()) * 2, (resolution.getGamePixelSize()));
        }
    }

    @Override
    public void clear() {
        super.clear();
        this.changeAdapter.unbind();
    }

}
