package de.mewel.pixellogic.mode;

public class PixelLogicTimeTrialModeOptions {

    public String id;

    public String name;

    public int[] levelSize;

    public int[] levelSizeOffset;

    public int[] levelMinDifficulty;

    public int[] levelMaxDifficulty;

    public static final class PixelLogicTimeTrialNormalOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialNormalOptions() {
            this.id = "time_trial_normal";
            this.name = "NORMAL MODE";
            this.levelSize = new int[]{4, 5, 6, 7};
            this.levelSizeOffset = new int[]{0, 1, 1, 2};
            this.levelMinDifficulty = new int[]{3, 4, 4, 4};
            this.levelMaxDifficulty = new int[]{4, 5, 5, 5};
        }

    }

    public static final class PixelLogicTimeTrialHardcoreOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialHardcoreOptions() {
            this.id = "time_trial_hardcore";
            this.name = "HARD MODE";
            this.levelSize = new int[]{6, 7, 8};
            this.levelSizeOffset = new int[]{0, 1, 2};
            this.levelMinDifficulty = new int[]{6, 7, 7};
            this.levelMaxDifficulty = new int[]{8, 9, 9};
        }

    }

    public static final class PixelLogicTimeTrialInsaneOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialInsaneOptions() {
            this.id = "time_trial_insane";
            this.name = "INSANE MODE";
            this.levelSize = new int[]{9, 10};
            this.levelSizeOffset = new int[]{2, 2};
            this.levelMinDifficulty = new int[]{8, 9};
            this.levelMaxDifficulty = new int[]{-1, -1, -1};
        }

    }


}
