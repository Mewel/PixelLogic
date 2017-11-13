package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicLevelManager levelManager = new PixelLogicLevelManager();
        levelManager.setup();
        PixelLogicLevelCollection levelCollection = levelManager.getCollection("rpgitems");

        PixelLogicLevel level = PixelLogicLevelLoader.load(levelCollection, 0);

        PixelLogicGameScreen screen = new PixelLogicGameScreen();
        screen.loadLevel(level);

        this.setScreen(screen);
    }

}
