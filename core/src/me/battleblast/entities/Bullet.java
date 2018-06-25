package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Bullet {
    private static final int MOVE_SPEED = 400;
    private Sprite sprite;

    public Bullet(float spawnX, float spawnY, float rotation, Sprite sprite) {
        this.sprite = sprite;
        sprite.setX(spawnX);
        sprite.setY(spawnY);
        sprite.setRotation(rotation);
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

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public Sprite getSprite() {
        return sprite;
    }

    public boolean isOutOfScreen() {
        return (sprite.getX() < 0 || sprite.getX() > Gdx.graphics.getWidth()
                || sprite.getY() < 0 || sprite.getY() > Gdx.graphics.getHeight());
    }
}
