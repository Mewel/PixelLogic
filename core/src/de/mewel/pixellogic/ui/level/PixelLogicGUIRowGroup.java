package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.event.PixelLogicLevelChangeEvent;
import de.mewel.pixellogic.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicGUIUtil;

public class PixelLogicGUIRowGroup extends PixelLogicUILevelGroup {

    private PixelLogicLevel level = null;

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
        this.updateChildrenBounds();
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
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateChildrenBounds();
    }

    protected void updateChildrenBounds() {
        if(this.level == null) {
            return;
        }
        PixelLogicGUILevelResolution resolution = PixelLogicGUILevelResolutionManager.instance().get(level);
        int scale = PixelLogicGUIUtil.getInfoSizeFactor(level);
        for (int i = 0; i < level.getRows(); i++) {
            Actor actor = this.getChildren().get(i);
            float y = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(getX(), y, (resolution.getGamePixelSize()) * scale, (resolution.getGamePixelSize()));
        }
    }

}
