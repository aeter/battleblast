package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class GameInputHandler extends InputAdapter {
    private Sprite player;

    public void setPlayer(Sprite player) {
        this.player = player;
    }

    @Override
    public boolean keyUp(int keycode) {
        float movement = 0;
        switch(keycode) {
            case Keys.LEFT:
                player.setRotation(270);
                movement = player.getX() - 1000 * Gdx.graphics.getDeltaTime();
                if (movement > 0) {
                    player.setX(movement);
                } else {
                    player.setX(0);
                }
                break;
            case Keys.RIGHT:
                player.setRotation(90);
                movement = player.getX() + 1000 * Gdx.graphics.getDeltaTime();
                if (movement < Gdx.graphics.getWidth() - player.getWidth()) {
                    player.setX(movement);
                } else {
                    player.setX(Gdx.graphics.getWidth() - player.getWidth());
                }
                break;
            case Keys.UP:
                player.setRotation(180);
                movement = player.getY() + 1000 * Gdx.graphics.getDeltaTime();
                if (movement < Gdx.graphics.getHeight() - player.getHeight()) {
                    player.setY(movement);
                } else {
                    player.setY(Gdx.graphics.getHeight() - player.getHeight());
                }
                break;
            case Keys.DOWN:
                player.setRotation(0);
                movement = player.getY() - 1000 * Gdx.graphics.getDeltaTime();
                if (movement > 0) {
                    player.setY(movement);
                } else {
                    player.setY(0);
                }
                break;
        }
        return false;
    }
}
