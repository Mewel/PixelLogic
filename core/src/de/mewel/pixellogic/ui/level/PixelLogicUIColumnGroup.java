package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public class PixelLogicUIColumnGroup extends PixelLogicUILevelGroup {

    private PixelLogicLevel level = null;

    public PixelLogicUIColumnGroup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        for (int i = 0; i < level.getColumns(); i++) {
            PixelLogicUIColumnInfo part = new PixelLogicUIColumnInfo(getAssets(), getEventManager(), level, i);
            this.addActor(part);
        }
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.level = null;
    }

    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
        if (this.level == null) {
            return;
        }
        for (int i = 0; i < this.getChildren().size; i++) {
            Actor actor = this.getChildren().get(i);
            float x = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(x, 0, resolution.getGamePixelSize(), getHeight());
            if(actor instanceof PixelLogicUIColumnInfo) {
                ((PixelLogicUIColumnInfo) actor).updateLevelResolution(resolution);
            }
        }
    }

    public void reset() {
        for (int i = 0; i < this.getChildren().size; i++) {
            Actor actor = this.getChildren().get(i);
            if (actor instanceof PixelLogicUIColumnInfo) {
                ((PixelLogicUIColumnInfo) actor).reset();
            }
        }
    }

}
