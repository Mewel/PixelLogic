package de.mewel.pixellogic;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import java.util.List;

import de.mewel.pixellogic.model.PixelLogicLevel;
import de.mewel.pixellogic.model.PixelLogicLevelCollection;
import de.mewel.pixellogic.util.PixelLogicBruteForceSolver;
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

        PixelLogicLevel level = PixelLogicLevelLoader.load(levelCollection, "shield");
        Gdx.app.log("validate level 1", "level is " + (validator.validate(level.getLevelData()) ? "valid" : "invalid"));

        List<List<Integer>> rowData = PixelLogicUtil.getRowData(level.getLevelData());
        List<List<Integer>> colData = PixelLogicUtil.getColumnData(level.getLevelData());
        Boolean[][] solvedLevel = new PixelLogicBruteForceSolver().solve(rowData, colData);
        level.setPixels(solvedLevel);

        PixelLogicLevelScreen screen = new PixelLogicLevelScreen();
        //screen.load(PixelLogicUtil.sampleImageLevel());
        screen.load(level);


        // valid sword,

        this.setScreen(screen);
    }

}
