package de.mewel.pixellogic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import de.mewel.pixellogic.PixelLogicGame;

public class DesktopLauncher {

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Pixellogic";
        config.useGL30 = true;
        config.width = 480;
        config.height = 800;
        new LwjglApplication(new PixelLogicGame(), config);

        //new LwjglApplication(new PicturesSpike(), config);
    }

}
