package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUIRowGroup extends PixelLogicUILevelGroup {

    private PixelLogicLevel level = null;

    public PixelLogicUIRowGroup(PixelLogicGlobal global) {
        super(global);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        for (int i = 0; i < level.getRows(); i++) {
            PixelLogicUIRowInfo part = new PixelLogicUIRowInfo(getGlobal(), level, i);
            this.addActor(part);
        }
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.level = null;
    }

    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
        for (int i = 0; i < this.getChildren().size; i++) {
            Actor actor = this.getChildren().get(i);
            float y = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(getX(), y, getWidth(), resolution.getGamePixelSize());
            if (actor instanceof PixelLogicUIRowInfo) {
                ((PixelLogicUIRowInfo) actor).updateLevelResolution(resolution);
            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getChildren().size; i++) {
            Actor actor = this.getChildren().get(i);
            if (actor instanceof PixelLogicUIRowInfo) {
                ((PixelLogicUIRowInfo) actor).reset();
            }
        }
    }

}
