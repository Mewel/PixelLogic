package de.mewel.pixellogic.gui;

public class PixelLogicGUIResolutionManager {

    private static PixelLogicGUIResolutionManager INSTANCE;

    private PixelLogicGUIResolutionManager() {

    }

    public static PixelLogicGUIResolutionManager instance() {
        if(INSTANCE == null) {
            INSTANCE = new PixelLogicGUIResolutionManager();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    private void init() {

    }

}
