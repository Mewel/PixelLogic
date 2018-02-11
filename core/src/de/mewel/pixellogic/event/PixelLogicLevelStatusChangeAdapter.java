package de.mewel.pixellogic.event;

import de.mewel.pixellogic.ui.PixelLogicLevelStatus;

public class PixelLogicLevelStatusChangeAdapter {

    private PixelLogicListener listener;

    public void bind(final PixelLogicLevelStatusChangeListener changeListener) {
        this.listener = new PixelLogicListener() {
            @Override
            public void handle(PixelLogicEvent event) {
                if(event instanceof PixelLogicLevelStatusChangeEvent) {
                    PixelLogicLevelStatusChangeEvent changeEvent = (PixelLogicLevelStatusChangeEvent) event;
                    PixelLogicLevelStatus levelStatus = changeEvent.getStatus();
                    if(PixelLogicLevelStatus.loaded.equals(levelStatus)) {
                        changeListener.onLevelLoad(changeEvent);
                    } else if(PixelLogicLevelStatus.solved.equals(levelStatus)) {
                        changeListener.onLevelSolved(changeEvent);
                    } else if(PixelLogicLevelStatus.beforeDestroyed.equals(levelStatus)) {
                        changeListener.onLevelBeforeDestroyed(changeEvent);
                    } else if(PixelLogicLevelStatus.destroyed.equals(levelStatus)) {
                        changeListener.onLevelDestroyed(changeEvent);
                    }
                    changeListener.onLevelChange(changeEvent);
                }
            }
        };
        PixelLogicEventManager.instance().listen(this.listener);
    }

    public void unbind() {
        PixelLogicEventManager.instance().remove(this.listener);
    }

}
