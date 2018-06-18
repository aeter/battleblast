package me.battleblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;

public class Tank {
    private static final int MOVE_SPEED = 4; // pixels
    private static final long ONE_MILLISECOND = 1000000; // in nanoseconds
    private static final long NEXT_BULLET_SPAWN_TIME = 300 * ONE_MILLISECOND;
    private static final float MOVE_ANIMATION_UPDATE_TIME = 20 * ONE_MILLISECOND;

    private Sprite sprite;
    private float previousX = 0f;
    private float previousY = 0f;
    private long lastShootTime = 0l;
    private long lastMoveTime = 0l;
    private ParticleEffect shootEffect;

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
        if (isTimeToMove()) {
            sprite.setX(sprite.getX() - MOVE_SPEED);
            if (sprite.getX() <= 0)
                sprite.setX(0);
            lastMoveTime = TimeUtils.nanoTime();
        }
    }

    public void moveRight() {
        sprite.setRotation(90);
        previousX = sprite.getX();
        previousY = sprite.getY();
        if (isTimeToMove()) {
            sprite.setX(sprite.getX() + MOVE_SPEED);
            if (sprite.getX() + sprite.getWidth() >= Gdx.graphics.getWidth()) 
                sprite.setX(Gdx.graphics.getWidth() - sprite.getWidth());
            lastMoveTime = TimeUtils.nanoTime();
        }
    }

    public void moveUp() {
        sprite.setRotation(180);
        previousX = sprite.getX();
        previousY = sprite.getY();
        if (isTimeToMove()) {
            sprite.setY(sprite.getY() + MOVE_SPEED);
            if (sprite.getY() + sprite.getHeight() >= Gdx.graphics.getHeight())
                sprite.setY(Gdx.graphics.getHeight() - sprite.getHeight());
            lastMoveTime = TimeUtils.nanoTime();
        }
    }

    public void moveDown() {
        sprite.setRotation(0);
        previousX = sprite.getX();
        previousY = sprite.getY();
        if (isTimeToMove()) {
            sprite.setY(sprite.getY() - MOVE_SPEED);
            if (sprite.getY() <= 0)
                sprite.setY(0);
            lastMoveTime = TimeUtils.nanoTime();
        }
    }

    public void shoot() {
        float bulletSpawnX = 0;
        float bulletSpawnY = 0;
        Sprite bulletSprite = new Sprite(BattleBlast.assets.get("kenney_topdownTanksRedux/PNG/Retina/bulletDark1.png", Texture.class));
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

        if (TimeUtils.nanoTime() - lastShootTime > NEXT_BULLET_SPAWN_TIME) {
            GameScreen.ALL_BULLETS.add(new Bullet(bulletSpawnX, bulletSpawnY, sprite.getRotation(), bulletSprite));
            makeShootEffect(bulletSpawnX, bulletSpawnY);
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
        if (shootEffect != null) {
            shootEffect.update(Gdx.graphics.getDeltaTime());
            shootEffect.draw(sb);
        }
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    private void makeShootEffect(float x, float y) {
        shootEffect = new ParticleEffect(BattleBlast.assets.get("effects/sparks.p", ParticleEffect.class));
        shootEffect.getEmitters().first().setPosition(x, y);
        shootEffect.scaleEffect(0.3f);
        shootEffect.start();
    }

    private void stepBack() {
        sprite.setX(previousX);
        sprite.setY(previousY);
    }

    private boolean isTimeToMove() {
        return TimeUtils.nanoTime() - lastMoveTime > MOVE_ANIMATION_UPDATE_TIME;
    }
}
