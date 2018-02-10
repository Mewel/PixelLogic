package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;

public abstract class PixelLogicUILevelGroup extends Group implements PixelLogicLevelChangeListener {

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicUILevelGroup() {
        this.changeAdapter = new PixelLogicLevelChangeAdapter();
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
    public void onLevelChange(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelLoad(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelSolved(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelBeforeDestroyed(PixelLogicLevelChangeEvent event) {
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelChangeEvent event) {
    }

}
