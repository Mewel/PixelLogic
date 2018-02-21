package de.mewel.pixellogic.ui.level;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.ui.PixelLogicUIUtil;

public class PixelLogicUIRowGroup extends PixelLogicUILevelGroup {

    private PixelLogicLevel level = null;

    public PixelLogicUIRowGroup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
    }

    @Override
    public void onLevelLoad(PixelLogicLevelStatusChangeEvent event) {
        this.level = event.getLevel();
        for (int i = 0; i < level.getRows(); i++) {
            PixelLogicUIRowInfo part = new PixelLogicUIRowInfo(getAssets(), getEventManager(), level, i);
            this.addActor(part);
        }
        this.updateChildrenBounds();
    }

    @Override
    public void onLevelSolved(PixelLogicLevelStatusChangeEvent event) {
        this.addAction(Actions.fadeOut(0.4f));
    }

    @Override
    public void onLevelDestroyed(PixelLogicLevelStatusChangeEvent event) {
        this.level = null;
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        this.updateChildrenBounds();
    }

    protected void updateChildrenBounds() {
        if (this.level == null) {
            return;
        }
        PixelLogicUILevelResolution resolution = PixelLogicUIUtil.get(level);
        int scale = PixelLogicUIUtil.getInfoSizeFactor(level);
        for (int i = 0; i < level.getRows(); i++) {
            Actor actor = this.getChildren().get(i);
            float y = resolution.getGamePixelSizeCombined() * i;
            actor.setBounds(getX(), y, (resolution.getGamePixelSize()) * scale, (resolution.getGamePixelSize()));
        }
    }

}