package de.mewel.pixellogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicLevelValidator;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicLevelManager levelManager = new PixelLogicLevelManager();
        levelManager.setup();
        PixelLogicLevelCollection levelCollection = levelManager.getCollection("rpgitems");

        PixelLogicLevelValidator validator = new PixelLogicLevelValidator();

        // level 1
        PixelLogicLevel level1 = PixelLogicLevelLoader.load(levelCollection, 4);
        Gdx.app.log("validate level 1", "level is " + (validator.validate(level1.getLevelData()) ? "valid" : "invalid"));

        // level 2
        // PixelLogicLevel level2 = PixelLogicLevelLoader.load(levelCollection, 1);
        // Gdx.app.log("validate level 2", "level is " + (validator.validate(level2.getLevelData()) ? "valid" : "invalid"));

        PixelLogicLevelScreen screen = new PixelLogicLevelScreen();
        screen.load(level1);
        //screen.load(level2);

        // valid sword,

        this.setScreen(screen);
    }

}
