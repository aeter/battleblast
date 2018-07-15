package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

import me.battleblast.BattleBlast;
import me.battleblast.screens.GameScreen;


public class Tank {
    private static final float MOVE_SPEED = 128; // ~pixels per second, depends on frame rate.
    private static final long ONE_MILLISECOND = 1000000; // in nanoseconds
    private static final long NEXT_BULLET_SPAWN_TIME = 300 * ONE_MILLISECOND;

    protected Sprite sprite;
    protected float previousX = 0f;
    protected float previousY = 0f;
    private long lastShootTime;

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
        if (TimeUtils.nanoTime() - lastShootTime > NEXT_BULLET_SPAWN_TIME) {
            GameScreen.ALL_BULLETS.add(new Bullet().positionedInFrontOf(sprite));
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
        return sprite.getBoundingRectangle();
    }

    private void stepBack() {
        sprite.setX(previousX);
        sprite.setY(previousY);
    }

    /*
     *  tiles are 32 * 32 pixels. if the tank has moved too fast
     *  and missed the 32th tile start by a few pixels, position the tank
     *  to the start of the tile.
     *  The enemy tank AI may make some decisions when it sees it's at the
     *  beginning of some tile (like, change direction, etc.)
     */
    // TODO - also undercompensate (depending on movement direction),
    // not just overcompensate. -> pixels_tolerance = 29, etc.
    // TODO - movement % 32
    private float tiled(float movement) {
        int pixels_tolerance = 3;
        if (movement % BattleBlast.TILE_WIDTH < pixels_tolerance) {
            return movement - movement % 16;
        }
        return movement;
    }
}
