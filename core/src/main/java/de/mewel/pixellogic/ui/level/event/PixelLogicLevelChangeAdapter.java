package de.mewel.pixellogic.ui.level.event;

import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;

public class PixelLogicLevelChangeAdapter {

    private final PixelLogicEventManager eventManager;

    private PixelLogicListener listener;

    public PixelLogicLevelChangeAdapter(PixelLogicEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void bind(final PixelLogicLevelChangeListener changeListener) {
        this.listener = event -> {
            if (event instanceof PixelLogicLevelStatusChangeEvent) {
                PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
                PixelLogicLevelStatus levelStatus = changeEvent.getStatus();
                if (PixelLogicLevelStatus.LOADED.equals(levelStatus)) {
                    changeListener.onLevelLoad(changeEvent);
                } else if (PixelLogicLevelStatus.SOLVED.equals(levelStatus)) {
                    changeListener.onLevelSolved(changeEvent);
                } else if (PixelLogicLevelStatus.BEFORE_DESTROYED.equals(levelStatus)) {
                    changeListener.onLevelBeforeDestroyed(changeEvent);
                } else if (PixelLogicLevelStatus.DESTROYED.equals(levelStatus)) {
                    changeListener.onLevelDestroyed(changeEvent);
                }
                changeListener.onLevelChange(changeEvent);
            }
            if (event instanceof PixelLogicBoardChangedEvent) {
                PixelLogicBoardChangedEvent changedEvent = (PixelLogicBoardChangedEvent) event;
                changeListener.onBoardChange(changedEvent);
            }
        };
        this.eventManager.listen(this.listener);
    }

    public void unbind() {
        this.eventManager.remove(this.listener);
    }

}
