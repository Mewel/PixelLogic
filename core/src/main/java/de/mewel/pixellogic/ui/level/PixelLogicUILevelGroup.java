package de.mewel.pixellogic.ui.level;

import de.mewel.pixellogic.PixelLogicGlobal;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelChangeListener;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;

public abstract class PixelLogicUILevelGroup extends PixelLogicUIGroup implements PixelLogicLevelChangeListener {

    private final PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicUILevelGroup(PixelLogicGlobal global) {
        super(global);
        this.changeAdapter = new PixelLogicLevelChangeAdapter(getEventManager());
        this.changeAdapter.bind(this);
    }

    public void updateLevelResolution(PixelLogicUILevelResolution resolution) {
    }

    @Override
    public void clear() {
        this.changeAdapter.unbind();
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

    @Override
    public void onBoardChange(PixelLogicBoardChangedEvent event) {
    }

}
