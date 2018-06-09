package me.battleblast;

import com.badlogic.gdx.Game;

public class BattleBlast extends Game {
    public void create() {
        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
    }
}
