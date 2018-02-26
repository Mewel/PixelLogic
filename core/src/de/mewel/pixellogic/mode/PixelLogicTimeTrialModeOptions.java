package de.mewel.pixellogic.mode;

public class PixelLogicTimeTrialModeOptions {

    public String id;

    public int[] levelSize;

    public int[] levelSizeOffset;

    public int[] levelMinDifficulty;

    public int[] levelMaxDifficulty;

    public static final class PixelLogicTimeTrialNormalOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialNormalOptions() {
            this.id = "time_trial_normal";
            this.levelSize = new int[]{4, 5, 6, 7};
            this.levelSizeOffset = new int[]{0, 1, 1, 2};
            this.levelMinDifficulty = new int[]{3, 4, 4, 4};
            this.levelMaxDifficulty = new int[]{4, 5, 5, 5};
        }

    }

    public static final class PixelLogicTimeTrialHardcoreOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialHardcoreOptions() {
            this.id = "time_trial_hardcore";
            this.levelSize = new int[]{6, 7, 8};
            this.levelSizeOffset = new int[]{0, 1, 2};
            this.levelMinDifficulty = new int[]{6, 7, 7};
            this.levelMaxDifficulty = new int[]{-1, -1, -1};
        }

    }

}
