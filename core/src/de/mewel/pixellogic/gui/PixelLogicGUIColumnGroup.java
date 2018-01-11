package de.mewel.pixellogic.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUIColumnGroup extends Group implements PixelLogicLevelChangeListener {

    private PixelLogicLevel level;

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicGUIColumnGroup() {
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
        for (int i = 0; i < level.getColumns(); i++) {
            PixelLogicGUIColumnInfo part = new PixelLogicGUIColumnInfo(level, i);
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
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        for (int i = 0; i < level.getColumns(); i++) {
            Actor actor = this.getChildren().get(i);
            float x = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(x, getY(), (resolution.getGamePixelSize()), (resolution.getGamePixelSize()) * 2);
        }
        super.act(delta);
    }

    @Override
    public void clear() {
        super.clear();
        this.changeAdapter.unbind();
    }

}
