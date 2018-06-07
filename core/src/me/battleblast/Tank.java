package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.TimeUtils;

public class Tank {
    private static final float MOVE_SPEED = 200f;
    private static final long ONE_MILLISECOND = 1000000; // in nanoseconds

    private Sprite sprite;
    private float previousX = 0f;
    private float previousY = 0f;
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

    public void shoot() {
        float bulletSpawnX = 0;
        float bulletSpawnY = 0;
        Sprite bulletSprite = new Sprite(BattleBlast.getAssetManager().get("kenney_topdownTanksRedux/PNG/Retina/bulletDark1.png", Texture.class));
        if (sprite.getRotation() == 0) {
            bulletSpawnX = sprite.getX() + sprite.getWidth() / 2 - bulletSprite.getWidth() / 2;
            bulletSpawnY = sprite.getY();
        }
        if (sprite.getRotation() == 90) {
            bulletSpawnX = sprite.getX() + sprite.getWidth();
            bulletSpawnY = sprite.getY() + sprite.getHeight() / 2 - bulletSprite.getHeight() / 2;
        }
        if (sprite.getRotation() == 180) {
            bulletSpawnX = sprite.getX() + sprite.getWidth() / 2 - bulletSprite.getWidth() / 2;
            bulletSpawnY = sprite.getY() + sprite.getHeight();
        }
        if (sprite.getRotation() == 270) {
            bulletSpawnX = sprite.getX();
            bulletSpawnY = sprite.getY() + sprite.getHeight() / 2 - bulletSprite.getHeight() / 2;
        }

        if (TimeUtils.nanoTime() - lastShootTime > 100 * ONE_MILLISECOND) {
            BattleBlast.ALL_BULLETS.add(new Bullet(bulletSpawnX, bulletSpawnY, sprite.getRotation(), bulletSprite));
            lastShootTime = TimeUtils.nanoTime();
        }
    }

    public void onCollisionWithEnemy() {
        stepBack();
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
