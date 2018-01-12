package de.mewel.pixellogic.ui.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class PixelLogicScreenManager {

    private Game game;

    private PixelLogicLevelScreen levelScreen;

    private PixelLogicScreenManager() {
        this.game = null;
        this.levelScreen = null;
    }

    public static PixelLogicScreenManager instance() {
        return InstanceHolder.INSTANCE;
    }

    public void setup(Game game) {
        this.game = game;
        this.levelScreen = new PixelLogicLevelScreen();
    }

    public PixelLogicLevelScreen getLevelScreen() {
        return this.levelScreen;
    }

    public void set(Screen screen) {
        this.game.setScreen(screen);
    }

    private static class InstanceHolder {

        static final PixelLogicScreenManager INSTANCE = new PixelLogicScreenManager();

    }

}
