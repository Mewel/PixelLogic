package de.mewel.pixellogic.event;

import de.mewel.pixellogic.gui.screen.PixelLogicLevelStatus;

public class PixelLogicLevelChangeAdapter {

    private PixelLogicListener listener;

    public void bind(final PixelLogicLevelChangeListener changeListener) {
        this.listener = new PixelLogicListener() {
            @Override
            public void handle(PixelLogicEvent event) {
                if(event instanceof PixelLogicLevelChangeEvent) {
                    PixelLogicLevelChangeEvent changeEvent = (PixelLogicLevelChangeEvent) event;
                    PixelLogicLevelStatus levelStatus = changeEvent.getStatus();
                    if(PixelLogicLevelStatus.loaded.equals(levelStatus)) {
                        changeListener.onLevelLoad(changeEvent);
                    } else if(PixelLogicLevelStatus.solved.equals(levelStatus)) {
                        changeListener.onLevelSolved(changeEvent);
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
