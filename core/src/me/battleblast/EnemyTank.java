package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;

public class EnemyTank extends Tank {
    private long lastDecision = TimeUtils.millis();
    private static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    public void move() {
        if (TimeUtils.millis() - lastDecision > 2 * ONE_SECOND_IN_MILLISECONDS) {
            changeDirection();
            lastDecision = TimeUtils.millis();
        } else if (atTileStart() && random() == 9) { 
            changeDirection();
        } else {
            keepDirection();
        }

        if (random() == 2) shoot();
    }

    private void changeDirection() {
        int random_int = random();
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

    private int random() {
        return (int) (Math.random() * 10);
    }

    private boolean atTileStart() {
        return sprite.getX() % 32 == 0 || sprite.getY() % 32 == 0;
    }

}
