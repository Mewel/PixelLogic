package de.mewel.pixellogic.gui;

import com.badlogic.gdx.math.MathUtils;

import de.mewel.pixellogic.model.PixelLogicLevel;

public class PixelLogicGUILevelResolutionManager {

    private static PixelLogicGUILevelResolutionManager INSTANCE;

    // private List<PixelLogicGUIResolution> resolutions;

    private PixelLogicGUILevelResolutionManager() {
        //   this.resolutions = new ArrayList<PixelLogicGUIResolution>();
    }

    public static PixelLogicGUILevelResolutionManager instance() {
        if (INSTANCE == null) {
            INSTANCE = new PixelLogicGUILevelResolutionManager();
            INSTANCE.init();
        }
        return INSTANCE;
    }

    private void init() {
        /*int fontScale = 2;
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
        resolutions.add(new PixelLogicGUIResolution(1440, 96, 6, 48 * fontScale));*/
    }

    public PixelLogicGUILevelResolution get(int width, int height, PixelLogicLevel level) {
        int columns = level.getColumns() + 2;
        int rows = level.getRows() + 2;

        float pixelPerColumn = MathUtils.floor((float) width / (float) columns);
        float pixelPerRow = MathUtils.floor((float) height / (float) rows);



        float columnSpace = MathUtils.floor(pixelPerColumn / 17);
        float rowSpace = MathUtils.floor(pixelPerRow / 17);

        PixelLogicGUILevelResolution bestResolution = null;
        /*for (PixelLogicGUIResolution resolution : resolutions) {
            if (bestResolution == null) {
                bestResolution = resolution;
                continue;
            }
            if (resolution.getResolution() <= min && resolution.getResolution() > bestResolution.getResolution()) {
                bestResolution = resolution;
            }
        }*/
        return bestResolution;
    }

    public void dispose() {
        /*for (PixelLogicGUIResolution resolution : resolutions) {
            resolution.dispose();
        }*/
    }

}
