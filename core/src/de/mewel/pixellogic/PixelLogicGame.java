package de.mewel.pixellogic;

import com.badlogic.gdx.Game;

public class PixelLogicGame extends Game {

    @Override
    public void create() {
        this.setScreen(new PixelLogicGameScreen());
    }

}
