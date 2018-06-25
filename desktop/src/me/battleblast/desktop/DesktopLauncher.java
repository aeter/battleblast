package me.battleblast.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import me.battleblast.BattleBlast;


public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = BattleBlast.MAP_WIDTH * BattleBlast.TILE_WIDTH; 
        config.height = BattleBlast.MAP_HEIGHT * BattleBlast.TILE_WIDTH;
        new LwjglApplication(new BattleBlast(), config);
    }
}
