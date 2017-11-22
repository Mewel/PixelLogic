package de.mewel.pixellogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicLevelLoader;
import de.mewel.pixellogic.util.PixelLogicLevelValidator;
import de.mewel.pixellogic.util.PixelLogicUtil;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        PixelLogicLevelManager levelManager = new PixelLogicLevelManager();
        levelManager.setup();
        PixelLogicLevelCollection levelCollection = levelManager.getCollection("rpgitems");

        PixelLogicLevelValidator validator = new PixelLogicLevelValidator();

        PixelLogicLevel level = PixelLogicLevelLoader.load(levelCollection, "white ring");
        Gdx.app.log("validate level 1", "level is " + (validator.validate(level.getLevelData()) ? "valid" : "invalid"));

        PixelLogicLevelScreen screen = new PixelLogicLevelScreen();
        //screen.load(new PixelLogicLevel(PixelLogicUtil.sampleLevel()));
        screen.load(level);

        // valid sword,

        this.setScreen(screen);
    }

}
