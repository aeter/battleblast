package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class PlayerTank {
    public Sprite sprite;
    private float moveSpeed = 200;
    private float previousX = 0;
    private float previousY = 0;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void moveLeft() {
        sprite.setRotation(270);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() - moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement < 0) movement = 0;
        sprite.setX(movement);
    }

    public void moveRight() {
        sprite.setRotation(90);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() + moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement > Gdx.graphics.getWidth() - sprite.getWidth()) {
            movement = Gdx.graphics.getWidth() - sprite.getWidth();
        }
        sprite.setX(movement);
    }

    public void moveUp() {
        sprite.setRotation(180);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() + moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement > Gdx.graphics.getHeight() - sprite.getHeight()) {
            movement = Gdx.graphics.getHeight() - sprite.getHeight();
        }
        sprite.setY(movement);
    }

    public void moveDown() {
        sprite.setRotation(0);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() - moveSpeed * Gdx.graphics.getDeltaTime();
        if (movement < 0) movement = 0;
        sprite.setY(movement);
    }

    public void onCollisionWithStabile() {
        stepBack();
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }

    private void stepBack() {
        sprite.setX(previousX);
        sprite.setY(previousY);
    }
}
