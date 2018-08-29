package de.mewel.pixellogic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.mewel.pixellogic.CharactersLevelTest;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Pixellogic";
        config.useGL30 = true;
        config.width = 360;
        config.height = 590;
        // new LwjglApplication(new PixelLogicGame(), config);

        new LwjglApplication(new CharactersLevelTest(), config);
    }

}
