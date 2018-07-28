package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import me.battleblast.BattleBlast;
import me.battleblast.screens.GameScreen;


public class Tank {
    public enum Direction { UP, DOWN, LEFT, RIGHT };
    protected static final float MOVE_SPEED = 100f; // ~pixels per second, depends on frame rate.
    protected static final long ONE_MILLISECOND = 1000000; // in nanoseconds
    protected Sprite sprite;
    protected float previousX = 0f;
    protected float previousY = 0f;
    private long lastShootTime;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);
    private Direction direction = Direction.LEFT;

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void continueMovingUntilMidTile() {
        move(null);
    }

    /*
     * Moving algorithm: if a key is released, keep moving until reaching the
     * middle of (or the begginning of) the tile. So not every key press changes
     * the direction - we need to finish the current movement first.
     */
    public void move(Direction direction) {
        previousX = sprite.getX();
        previousY = sprite.getY();

        if (reachedMidTile() && direction == null) {
            return;
        }

        if (reachedMidTile()) {
            this.direction = direction;
        }

        float distance = MOVE_SPEED * Gdx.graphics.getDeltaTime();
        if (this.direction == Direction.UP) {
            moveUp(distance);
        }
        if (this.direction == Direction.DOWN) {
            moveDown(distance);
        }
        if (this.direction == Direction.LEFT) {
            moveLeft(distance);
        }
        if (this.direction == Direction.RIGHT) {
            moveRight(distance);
        }
    }

    public boolean reachedMidTile() {
        if ((direction == Direction.UP || direction == Direction.DOWN) && sprite.getY() % halfTile() == 0) {
            return true;
        }
        if ((direction == Direction.LEFT || direction == Direction.RIGHT) && sprite.getX() % halfTile() == 0) {
            return true;
        }
        return false;
    }

    public void shoot() {
        if (TimeUtils.nanoTime() - lastShootTime > nextBulletSpawnTime()) {
            Bullet bullet = new Bullet().positionedInFrontOf(sprite);
            if (this instanceof PlayerTank) {
                bullet.isPlayerBullet = true;
            }
            GameScreen.ALL_BULLETS.add(bullet);
            lastShootTime = TimeUtils.nanoTime();
        }
    }

    public void onCollisionWithEnemy() {
        stepBack();
    }

    public void onCollisionWithObstacle() {
        stepBack();
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }

    public Rectangle getBounds() {
        // to reduce garbage collection, we reuse the same rectangle
        bounds.set(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        return bounds;
    }

    protected float nextBulletSpawnTime() {
        return 300 * ONE_MILLISECOND;
    }

    private void moveLeft(float distance) {
        sprite.setRotation(270);
        float screenBorderLeft = 0f;
        float passingDistance = sprite.getX() % halfTile();
        boolean passingHalfTile = sprite.getX() % halfTile() < (sprite.getX() - distance) % halfTile();
        if (sprite.getX() - distance < screenBorderLeft) {
            sprite.setX(screenBorderLeft);
        } else if (passingHalfTile && !reachedMidTile()) {
            sprite.setX(sprite.getX() - passingDistance);
        } else {
            sprite.setX(sprite.getX() - distance);
        }
    }

    private void moveRight(float distance) {
        sprite.setRotation(90);
        float screenBorderRight = Gdx.graphics.getWidth() - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH;
        float passingDistance = (sprite.getX() + distance) % halfTile();
        boolean passingHalfTile =  sprite.getX() % halfTile() > passingDistance;
        if (sprite.getX() + distance > screenBorderRight) {
            sprite.setX(screenBorderRight);
        } else if (passingHalfTile) {
            sprite.setX(sprite.getX() + distance - passingDistance);
        } else {
            sprite.setX(sprite.getX() + distance);
        }
    }

    private void moveUp(float distance) {
        sprite.setRotation(180);
        float screenBorderTop = Gdx.graphics.getHeight() - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH;
        float passingDistance = (sprite.getY() + distance) % halfTile();
        boolean passingHalfTile = sprite.getY() % halfTile() > passingDistance;
        if (sprite.getY() + distance > screenBorderTop) {
            sprite.setY(screenBorderTop);
        } else if (passingHalfTile) {
            sprite.setY(sprite.getY() + distance - passingDistance);
        } else {
            sprite.setY(sprite.getY() + distance);
        }
    }

    private void moveDown(float distance) {
        sprite.setRotation(0);
        float screenBorderDown = 0f;
        float passingDistance = sprite.getY() % halfTile();
        boolean passingHalfTile = sprite.getY() % halfTile() < (sprite.getY() - distance) % halfTile();
        if (sprite.getY() - distance < screenBorderDown) {
            sprite.setY(screenBorderDown);
        } else if (passingHalfTile && !reachedMidTile()) {
            sprite.setY(sprite.getY() - passingDistance);
        } else {
            sprite.setY(sprite.getY() - distance);
        }
    }

    private void stepBack() {
        sprite.setX(previousX);
        sprite.setY(previousY);
    }

    private int halfTile() {
        return BattleBlast.TILE_WIDTH / 2;
    }
}
