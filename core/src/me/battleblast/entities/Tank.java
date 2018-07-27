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

    public void move(Direction direction) {
        previousX = sprite.getX();
        previousY = sprite.getY();

        if (reachedMidTile() && direction == null) return;

        if (reachedMidTile()) {
            this.direction = direction;
        }

        float distance = MOVE_SPEED * Gdx.graphics.getDeltaTime();
        if (this.direction == Direction.UP) moveUp(distance);
        if (this.direction == Direction.DOWN) moveDown(distance);
        if (this.direction == Direction.LEFT) moveLeft(distance);
        if (this.direction == Direction.RIGHT) moveRight(distance);
    }

    public boolean reachedMidTile() {
        if ((direction == Direction.UP || direction == Direction.DOWN) && sprite.getY() % 16 == 0) {
            return true;
        }
        if ((direction == Direction.LEFT || direction == Direction.RIGHT) && sprite.getX() % 16 == 0) {
            return true;
        }
        return false;
    }

    private void moveLeft(float distance) {
        sprite.setRotation(270);
        float screenBorderLeft = 0f;
        boolean overPassingSixteenthTile = sprite.getX() % 16 < (sprite.getX() - distance) % 16;
        if (sprite.getX() - distance < screenBorderLeft) {
            sprite.setX(screenBorderLeft);
        } else if (overPassingSixteenthTile && !reachedMidTile()) {
            sprite.setX(sprite.getX() - (sprite.getX() % 16));
        } else {
            sprite.setX(sprite.getX() - distance);
        }
    }

    private void moveRight(float distance) {
        sprite.setRotation(90);
        float screenBorderRight = Gdx.graphics.getWidth() - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH;
        boolean overPassingSixteenthTile =  sprite.getX() % 16 > (sprite.getX() + distance) % 16;
        if (sprite.getX() + distance > screenBorderRight) {
            sprite.setX(screenBorderRight);
        } else if (overPassingSixteenthTile) {
            sprite.setX(sprite.getX() + distance - ((sprite.getX() + distance) % 16));
        } else {
            sprite.setX(sprite.getX() + distance);
        }
    }

    private void moveUp(float distance) {
        sprite.setRotation(180);
        float screenBorderTop = Gdx.graphics.getHeight() - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH;
        boolean overpassingSixteenthTile = sprite.getY() % 16 > (sprite.getY() + distance) % 16;
        if (sprite.getY() + distance > screenBorderTop) {
            sprite.setY(screenBorderTop);
        } else if (overpassingSixteenthTile) {
            sprite.setY(sprite.getY() + distance - ((sprite.getY() + distance) % 16));
        } else {
            sprite.setY(sprite.getY() + distance);
        }
    }

    private void moveDown(float distance) {
        sprite.setRotation(0);
        float screenBorderDown = 0f;
        boolean overpassingSixteenthTile = sprite.getY() % 16 < (sprite.getY() - distance) % 16;
        if (sprite.getY() - distance < screenBorderDown) {
            sprite.setY(screenBorderDown);
        } else if (overpassingSixteenthTile && !reachedMidTile()) {
            sprite.setY(sprite.getY() - (sprite.getY() % 16));
        } else {
            sprite.setY(sprite.getY() - distance);
        }
    }

    public void shoot() {
        // TODO - see if it's possible to use object pooling  for Bullets,
        // https://github.com/libgdx/libgdx/wiki/Memory-management
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

    private void stepBack() {
        sprite.setX(previousX);
        sprite.setY(previousY);
    }
}
