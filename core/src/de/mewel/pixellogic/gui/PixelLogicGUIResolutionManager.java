package de.mewel.pixellogic.gui;

import java.util.ArrayList;
import java.util.List;

public class PixelLogicGUIResolutionManager {

    private static PixelLogicGUIResolutionManager INSTANCE;

    private List<PixelLogicGUIResolution> resolutions;

    private PixelLogicGUIResolutionManager() {
        this.resolutions = new ArrayList<PixelLogicGUIResolution>();
    }

    public static PixelLogicGUIResolutionManager instance() {
        if (INSTANCE == null) {
            INSTANCE = new PixelLogicGUIResolutionManager();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    private void init() {
        int fontScale = 2;
        // 320
        resolutions.add(new PixelLogicGUIResolution(320, 21, 1, 8 * fontScale));
        // 360
        resolutions.add(new PixelLogicGUIResolution(360, 24, 1, 8 * fontScale));
        // 480
        resolutions.add(new PixelLogicGUIResolution(480, 32, 2, 16 * fontScale));
        // 540
        resolutions.add(new PixelLogicGUIResolution(540, 36, 2, 16 * fontScale));
        // 640
        resolutions.add(new PixelLogicGUIResolution(640, 42, 3, 16 * fontScale));
        // 720
        resolutions.add(new PixelLogicGUIResolution(720, 48, 3, 24 * fontScale));
        // 960
        resolutions.add(new PixelLogicGUIResolution(960, 64, 4, 32 * fontScale));
        // 1080
        resolutions.add(new PixelLogicGUIResolution(1080, 72, 4, 32 * fontScale));
        // 1440
        resolutions.add(new PixelLogicGUIResolution(1440, 96, 6, 48 * fontScale));
    }

    public PixelLogicGUIResolution get(int screenWidth, int screenHeight) {
        int min = Math.min(screenWidth, screenHeight);
        PixelLogicGUIResolution resolution;
        for (PixelLogicGUIResolution rs : resolutions) {

        }
    }

}
