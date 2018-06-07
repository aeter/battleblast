package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class Bullet {
    private static final int MOVE_SPEED = 400;
    private static final int SPAWN_DISTANCE_FROM_TANK = 5;
    private Sprite sprite;

    public Bullet(float x, float y, float rotation) {
        sprite = new Sprite(BattleBlast.getAssetManager().get("kenney_topdownTanksRedux/PNG/Retina/bulletDark1.png", Texture.class));
        sprite.setX(x);
        sprite.setY(y);
        sprite.setRotation(rotation);
        if (rotation == 0) sprite.setY(y - SPAWN_DISTANCE_FROM_TANK);
        if (rotation == 90) sprite.setX(x + SPAWN_DISTANCE_FROM_TANK);
        if (rotation == 180) sprite.setY(y + SPAWN_DISTANCE_FROM_TANK);
        if (rotation == 270) sprite.setX(x - SPAWN_DISTANCE_FROM_TANK);
    }

    public void move() {
        if (sprite.getRotation() == 0)
            sprite.setY(sprite.getY() - MOVE_SPEED * Gdx.graphics.getDeltaTime());
        if (sprite.getRotation() == 90)
            sprite.setX(sprite.getX() + MOVE_SPEED * Gdx.graphics.getDeltaTime());
        if (sprite.getRotation() == 180)
            sprite.setY(sprite.getY() + MOVE_SPEED * Gdx.graphics.getDeltaTime());
        if (sprite.getRotation() == 270)
            sprite.setX(sprite.getX() - MOVE_SPEED * Gdx.graphics.getDeltaTime());
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }
}
