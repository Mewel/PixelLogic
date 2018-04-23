package de.mewel.pixellogic.mode;

public class PixelLogicTimeTrialModeOptions {

    public enum Modes {
        time_trial_normal, time_trial_hardcore, time_trial_insane, time_trial_epic
    }

    public String id;

    public String name;

    public int[] levelSize;

    public int[] levelSizeXY;

    public int[] levelSizeOffset;

    public float[] levelMinDifficulty;

    public float[] levelMaxDifficulty;

    public static final class PixelLogicTimeTrialNormalOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialNormalOptions() {
            this.id = Modes.time_trial_normal.name();
            this.name = "NORMAL MODE";
            this.levelSize = new int[]{4, 5, 6, 7};
            this.levelSizeOffset = new int[]{0, 1, 1, 2};
            this.levelMinDifficulty = new float[]{1.5f, 1.7f, 1.9f, 2.1f};
            this.levelMaxDifficulty = new float[]{2f, 2.2f, 2.4f, 2.6f};
        }

    }

    public static final class PixelLogicTimeTrialHardcoreOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialHardcoreOptions() {
            this.id = Modes.time_trial_hardcore.name();
            this.name = "HARD MODE";
            this.levelSize = new int[]{6, 7, 8};
            this.levelSizeOffset = new int[]{0, 1, 2};
            this.levelMinDifficulty = new float[]{2.5f, 2.8f, 3.1f};
            this.levelMaxDifficulty = new float[]{3.5f, 3.8f, 4.1f};
        }

    }

    public static final class PixelLogicTimeTrialInsaneOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialInsaneOptions() {
            this.id = Modes.time_trial_insane.name();
            this.name = "INSANE MODE";
            this.levelSize = new int[]{9, 10};
            this.levelSizeOffset = new int[]{2, 2};
            this.levelMinDifficulty = new float[]{3.5f, 4f};
            this.levelMaxDifficulty = new float[]{-1, -1};
        }

    }

    public static final class PixelLogicTimeTrialEpicOptions extends PixelLogicTimeTrialModeOptions {

        public PixelLogicTimeTrialEpicOptions() {
            this.id = Modes.time_trial_epic.name();
            this.name = "EPIC MODE";
            this.levelSize = new int[]{0};
            this.levelSizeXY = new int[]{12, 16};
            this.levelSizeOffset = new int[]{0};
            this.levelMinDifficulty = new float[]{2.5f};
            this.levelMaxDifficulty = new float[]{5.5f};
        }

    }

}
