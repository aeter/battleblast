package me.battleblast;

import com.badlogic.gdx.Gdx;

public class EnemyTank extends Tank {
    public void move() {
        int random_int = (int) (Math.random() * 10);
        boolean atTileStart = sprite.getX() % 32 == 0 || sprite.getY() % 32 == 0;
        Gdx.app.log("%d", String.format("%d", random_int));
        if (random_int % 9 == 0) {
            changeDirection();
        } else {
            keepDirection();
        }
    }

    private void changeDirection() {
        int random_int = (int) (Math.random() * 10);
        if (random_int < 2) {
            moveLeft();
        } else if (random_int < 4) {
            moveRight();
        } else if (random_int < 6) {
            moveUp();
        } else {
            moveDown();
        }
    }

    private void keepDirection() {
        if (previousX < sprite.getX()) {
            moveRight();
        } else if (previousY < sprite.getY()) { 
            moveUp();
        } else if (previousX > sprite.getX()) {
            moveLeft();
        } else {
            moveDown();
        }
    }
}
