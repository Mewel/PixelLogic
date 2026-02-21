package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public abstract class PixelLogicTimeTrialHighscoreStore {

    private static final int MAX_ENTRIES = 5;

    /**
     * Adds the time to the highscore store.
     *
     * @param mode the mode which was played
     * @param time the time reached
     * @return the new highscore position (starting at 0 for the first place) or -1 if its not a new score
     */
    public static int add(String mode, Long time) {
        List<Highscore> highscoreList = list(mode);
        Highscore newHighscore = new Highscore(time);
        highscoreList.add(newHighscore);
        highscoreList.sort(Comparator.comparingLong(h -> h.time));
        highscoreList = highscoreList.subList(0, Math.min(highscoreList.size(), 5));
        int rank = highscoreList.indexOf(newHighscore);
        if (rank >= 0) {
            store(mode, highscoreList);
        }
        return rank;
    }

    public static List<Highscore> list(String mode) {
        List<Highscore> highscoreList = new ArrayList<>();
        Preferences preferences = Gdx.app.getPreferences("pixellogic_highscore_" + mode);
        for (int i = 1; i <= MAX_ENTRIES; i++) {
            String highscoreAsString = preferences.getString(String.valueOf(i), null);
            Highscore highscore = Highscore.of(highscoreAsString);
            if (highscore == null) {
                continue;
            }
            highscoreList.add(highscore);
        }
        return highscoreList;
    }

    public static void store(String mode, List<Highscore> highscoreList) {
        Preferences preferences = Gdx.app.getPreferences("pixellogic_highscore_" + mode);
        for (int i = 0; i < highscoreList.size(); i++) {
            Highscore highscore = highscoreList.get(i);
            preferences.putString(String.valueOf(i + 1), highscore.toString());
        }
        preferences.flush();
    }

    public static void clear(String mode) {
        Preferences preferences = Gdx.app.getPreferences("pixellogic_highscore_" + mode);
        preferences.clear();
        preferences.flush();
    }

    public static class Highscore {

        public long time;

        public long date;

        Highscore() {
        }

        public Highscore(long time) {
            this.time = time;
            this.date = new Date().getTime();
        }

        @Override
        public String toString() {
            return time + "|" + date;
        }

        static Highscore of(String string) {
            if (string == null) {
                return null;
            }
            Highscore highscore = new Highscore();
            String[] parts = string.split("\\|");
            highscore.time = Long.parseLong(parts[0]);
            highscore.date = Long.parseLong(parts[1]);
            return highscore;
        }

    }

}
