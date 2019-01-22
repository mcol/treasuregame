package mcol.treasuregame.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mcol.treasuregame.TreasureGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1280;
        config.height = 720;
        config.title = TreasureGame.TITLE;
        config.addIcon("icon32.png", FileType.Internal);
        config.foregroundFPS = 60;
        config.backgroundFPS = 20;
        new LwjglApplication(new TreasureGame(), config);
	}
}
