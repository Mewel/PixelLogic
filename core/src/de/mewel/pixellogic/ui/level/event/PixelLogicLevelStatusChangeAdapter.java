package de.mewel.pixellogic.ui.level.event;

import de.mewel.pixellogic.event.PixelLogicEvent;
import de.mewel.pixellogic.event.PixelLogicEventManager;
import de.mewel.pixellogic.event.PixelLogicListener;
import de.mewel.pixellogic.model.PixelLogicLevelStatus;

public class PixelLogicLevelStatusChangeAdapter {

    private PixelLogicEventManager eventManager;

    private PixelLogicListener listener;

    public PixelLogicLevelStatusChangeAdapter(PixelLogicEventManager eventManager) {
        this.eventManager = eventManager;
    }

    public void bind(final PixelLogicLevelStatusChangeListener changeListener) {
        this.listener = new PixelLogicListener() {
            @Override
            public void handle(PixelLogicEvent event) {
                if (event instanceof PixelLogicLevelStatusChangeEvent) {
                    PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
                    PixelLogicLevelStatus levelStatus = changeEvent.getStatus();
                    if (PixelLogicLevelStatus.loaded.equals(levelStatus)) {
                        changeListener.onLevelLoad(changeEvent);
                    } else if (PixelLogicLevelStatus.solved.equals(levelStatus)) {
                        changeListener.onLevelSolved(changeEvent);
                    } else if (PixelLogicLevelStatus.beforeDestroyed.equals(levelStatus)) {
                        changeListener.onLevelBeforeDestroyed(changeEvent);
                    } else if (PixelLogicLevelStatus.destroyed.equals(levelStatus)) {
                        changeListener.onLevelDestroyed(changeEvent);
                    }
                    changeListener.onLevelChange(changeEvent);
                }
            }
        };
        this.eventManager.listen(this.listener);
    }

    public void unbind() {
        this.eventManager.remove(this.listener);
    }

}
