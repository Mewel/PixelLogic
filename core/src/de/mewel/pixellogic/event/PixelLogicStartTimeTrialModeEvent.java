package de.mewel.pixellogic.event;

import de.mewel.pixellogic.mode.PixelLogicTimeTrialModeOptions;
import de.mewel.pixellogic.ui.screen.PixelLogicUITimeTrialScreen;

public class PixelLogicStartTimeTrialModeEvent extends PixelLogicEvent {

    private PixelLogicTimeTrialModeOptions options;

    public PixelLogicStartTimeTrialModeEvent(PixelLogicUITimeTrialScreen source, PixelLogicTimeTrialModeOptions options) {
        super(source);
        this.options = options;
    }

    public PixelLogicTimeTrialModeOptions getOptions() {
        return options;
    }

}
