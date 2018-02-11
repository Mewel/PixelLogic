package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.event.PixelLogicLevelStatusChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelStatusChangeListener;

public abstract class PixelLogicUILevelGroup extends Group implements PixelLogicLevelStatusChangeListener {

    private PixelLogicLevelStatusChangeAdapter changeAdapter;

    public PixelLogicUILevelGroup() {
        this.changeAdapter = new PixelLogicLevelStatusChangeAdapter();
        this.changeAdapter.bind(this);
    }

    @Override
    public void clear() {
        this.changeAdapter.unbind();
        for (Actor actor : this.getChildren()) {
            actor.clear();
        }
        super.clear();
    }

    @Override
    public void onLevelChange(PixelLogicLevelStatusChangeEvent event) {
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
    }

    @Override
    public void onLevelBeforeDestroyed(PixelLogicLevelStatusChangeEvent event) {
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event) {
    }

}
