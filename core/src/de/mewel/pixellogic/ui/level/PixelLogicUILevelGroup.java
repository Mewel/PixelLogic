package de.mewel.pixellogic.ui.level;

import de.mewel.pixellogic.asset.PixelLogicAssets;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.ui.PixelLogicUIGroup;
import de.mewel.pixellogic.ui.level.event.PixelLogicBoardChangedEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelChangeAdapter;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelStatusChangeEvent;
import de.mewel.pixellogic.ui.level.event.PixelLogicLevelChangeListener;

public abstract class PixelLogicUILevelGroup extends PixelLogicUIGroup implements PixelLogicLevelChangeListener {

    private PixelLogicLevelChangeAdapter changeAdapter;

    public PixelLogicUILevelGroup(PixelLogicAssets assets, PixelLogicEventManager eventManager) {
        super(assets, eventManager);
        this.changeAdapter = new PixelLogicLevelChangeAdapter(eventManager);
        this.changeAdapter.bind(this);
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
