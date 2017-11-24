package de.mewel.pixellogic.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.mewel.pixellogic.PixelLogicGame;
import de.mewel.pixellogic.gui.PixelLogicGUIConstants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Pixellogic";
		config.useGL30 = true;
		//int size = (PixelLogicGUIConstants.PIXEL_SPACE_COMBINED * 18) - PixelLogicGUIConstants.PIXEL_SPACE;
		config.width = 960;
		config.height = 960;
		new LwjglApplication(new PixelLogicGame(), config);
	}
}
