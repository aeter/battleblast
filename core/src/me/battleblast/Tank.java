package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tank {
    private Sprite sprite;
    private static final float MOVE_SPEED = 200f;
    private float previousX = 0f;
    private float previousY = 0f;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void moveLeft() {
        sprite.setRotation(270);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() - MOVE_SPEED * Gdx.graphics.getDeltaTime();
        if (movement < 0) movement = 0;
        sprite.setX(movement);
    }

    public void moveRight() {
        sprite.setRotation(90);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() + MOVE_SPEED * Gdx.graphics.getDeltaTime();
        float endOfScreen = Gdx.graphics.getWidth() - sprite.getWidth();
        if (movement > endOfScreen) movement = endOfScreen;
        sprite.setX(movement);
    }

    public void moveUp() {
        sprite.setRotation(180);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() + MOVE_SPEED * Gdx.graphics.getDeltaTime();
        float endOfScreen = Gdx.graphics.getHeight() - sprite.getHeight();
        if (movement > endOfScreen) movement = endOfScreen;
        sprite.setY(movement);
    }

    public void moveDown() {
        sprite.setRotation(0);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() - MOVE_SPEED * Gdx.graphics.getDeltaTime();
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
