package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import me.battleblast.BattleBlast;
import me.battleblast.screens.GameScreen;


public class Tank {
    protected static final float MOVE_SPEED = 100f; // ~pixels per second, depends on frame rate.
    protected static final long ONE_MILLISECOND = 1000000; // in nanoseconds

    protected Sprite sprite;
    protected float previousX = 0f;
    protected float previousY = 0f;
    private long lastShootTime;
    private Rectangle bounds = new Rectangle(0, 0, 0, 0);

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public Sprite getSprite() {
        return sprite;
    }

    // TODO - when turning somewhere (changing rotation with the current
    // movement), if x or y is within 1-2 pixels of beginning of the tile, set
    // to beginning of the tile. Otherwise it's very frustrating with 4-5 tries
    // to get exactly to the 0th pixel of a position...
    public void moveLeft() {
        sprite.setRotation(270);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() - MOVE_SPEED * Gdx.graphics.getDeltaTime();
        if (movement < 0) movement = 0;
        sprite.setX(tiled(movement));
    }

    public void moveRight() {
        sprite.setRotation(90);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getX() + MOVE_SPEED * Gdx.graphics.getDeltaTime();
        float endOfScreen = Gdx.graphics.getWidth() - sprite.getWidth();
        if (movement > endOfScreen) movement = endOfScreen;
        sprite.setX(tiled(movement));
    }

    public void moveUp() {
        sprite.setRotation(180);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() + MOVE_SPEED * Gdx.graphics.getDeltaTime();
        float endOfScreen = Gdx.graphics.getHeight() - sprite.getHeight();
        if (movement > endOfScreen) movement = endOfScreen;
        sprite.setY(tiled(movement));
    }

    public void moveDown() {
        sprite.setRotation(0);
        previousX = sprite.getX();
        previousY = sprite.getY();
        float movement = sprite.getY() - MOVE_SPEED * Gdx.graphics.getDeltaTime();
        if (movement < 0) movement = 0;
        sprite.setY(tiled(movement));
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

    /*
     *  If the tank had moved too fast and missed the start or the end of the tile by
     *  a few pixels, place the tank to the start/end of the tile (smoother movement)
     */
    private float tiled(float movement) {
        int pixels_tolerance = 1;
        if (movement % BattleBlast.TILE_WIDTH < pixels_tolerance) {
            return movement - movement % BattleBlast.TILE_WIDTH;
        }
        if (movement % BattleBlast.TILE_WIDTH > BattleBlast.TILE_WIDTH - pixels_tolerance) {
            return (movement - movement % BattleBlast.TILE_WIDTH) + BattleBlast.TILE_WIDTH;
        }
        return movement;
    }
}
