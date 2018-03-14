package de.mewel.pixellogic.mode;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class PixelLogicTimeTrialHighscoreStore {

    private static final int MAX_ENTRIES = 5;

    /**
     * Adds the time to the highscore store.
     *
     * @param mode the mode which was played
     * @param time the time reached
     * @return the new highscore position (starting at 1 for the first place) or -1 if its not a new score
     */
    public static int add(String mode, Long time) {
        List<Highscore> highscoreList = list(mode);
        for (int i = 0; i < highscoreList.size(); i++) {
            Highscore highscore = highscoreList.get(i);
            if (highscore.time > time) {
                Highscore newHighscore = new Highscore(time);
                List<Highscore> newHighscoreList = new ArrayList<Highscore>();
                for (int j = 0; i < highscoreList.size(); j++) {
                    if (i == j) {
                        highscoreList.add(newHighscore);
                        continue;
                    }
                    newHighscoreList.add(highscoreList.get(i < j ? j : j - 1));
                }
                store(mode, newHighscoreList);
                return i + 1;
            }
        }
        if(highscoreList.size() < MAX_ENTRIES) {
            highscoreList.add(new Highscore(time));
            store(mode, highscoreList);
            return highscoreList.size();
        }
        Gdx.app.log("add", list(mode).toString());
        return -1;
    }

    public static List<Highscore> list(String mode) {
        List<Highscore> highscoreList = new ArrayList<Highscore>();
        Preferences preferences = Gdx.app.getPreferences("highscore_" + mode);
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
        Preferences preferences = Gdx.app.getPreferences("highscore_" + mode);
        for (int i = 0; i < highscoreList.size(); i++) {
            Highscore highscore = highscoreList.get(i);
            preferences.putString(String.valueOf(i + 1), highscore.toString());
        }
        preferences.flush();
    }

    public static void clear(String mode) {
        Preferences preferences = Gdx.app.getPreferences("highscore_" + mode);
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
            return String.valueOf(time) + "|" + String.valueOf(date);
        }

        static Highscore of(String string) {
            if (string == null) {
                return null;
            }
            Highscore highscore = new Highscore();
            String[] parts = string.split("|");
            highscore.time = Long.valueOf(parts[0]);
            highscore.date = Long.valueOf(parts[2]);
            return highscore;
        }

    }

}
