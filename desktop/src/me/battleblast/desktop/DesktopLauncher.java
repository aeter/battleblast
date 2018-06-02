package me.battleblast.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.battleblast.BattleBlast;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 640; // 20 tiles on map, each 32 pixels wide
        config.height = 640; // 20 tiles on map, each 32 pixels wide
		new LwjglApplication(new BattleBlast(), config);
	}
}
