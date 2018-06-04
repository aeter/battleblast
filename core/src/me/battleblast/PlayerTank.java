package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerTank {
    private Sprite sprite;
    private float moveSpeed = 200;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void moveLeft() {
        sprite.setRotation(270);
        float movement = sprite.getX() - moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement > 0) {
            sprite.setX(movement);
        } else {
            sprite.setX(0);
        }
    }

    public void moveRight() {
        sprite.setRotation(90);
        float movement = sprite.getX() + moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement < Gdx.graphics.getWidth() - sprite.getWidth()) {
            sprite.setX(movement);
        } else {
            sprite.setX(Gdx.graphics.getWidth() - sprite.getWidth());
        }
    }

    public void moveUp() {
        sprite.setRotation(180);
        float movement = sprite.getY() + moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement < Gdx.graphics.getHeight() - sprite.getHeight()) {
            sprite.setY(movement);
        } else {
            sprite.setY(Gdx.graphics.getHeight() - sprite.getHeight());
        }
    }

    public void moveDown() {
        sprite.setRotation(0);
        float movement = sprite.getY() - moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement > 0) {
            sprite.setY(movement);
        } else {
            sprite.setY(0);
        }
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }
}
