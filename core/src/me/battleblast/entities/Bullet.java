package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import me.battleblast.BattleBlast;


public class Bullet {
    public boolean markedForRemoval = false;
    private static final int MOVE_SPEED = 300; // approx. pixels per second
    private Sprite sprite;

    public Bullet() {
        this.sprite = new Sprite(BattleBlast.getAtlas().findRegion("bulletDark2_8x16"));
    }

    public Bullet positionedInFrontOf(Sprite tankSprite) {
        float bulletSpawnX = 0;
        float bulletSpawnY = 0;
        if (tankSprite.getRotation() == 0) {
            bulletSpawnX = tankSprite.getX() + tankSprite.getWidth() / 2 - sprite.getWidth() / 2;
            bulletSpawnY = tankSprite.getY();
        }
        if (tankSprite.getRotation() == 90) {
            bulletSpawnX = tankSprite.getX() + tankSprite.getWidth();
            bulletSpawnY = tankSprite.getY() + tankSprite.getHeight() / 2 - sprite.getHeight() / 2;
        }
        if (tankSprite.getRotation() == 180) {
            bulletSpawnX = tankSprite.getX() + tankSprite.getWidth() / 2 - sprite.getWidth() / 2;
            bulletSpawnY = tankSprite.getY() + tankSprite.getHeight();
        }
        if (tankSprite.getRotation() == 270) {
            bulletSpawnX = tankSprite.getX();
            bulletSpawnY = tankSprite.getY() + tankSprite.getHeight() / 2 - sprite.getHeight() / 2;
        }
        sprite.setX(bulletSpawnX);
        sprite.setY(bulletSpawnY);
        sprite.setRotation(tankSprite.getRotation());
        return this;
    }

    public void move(float deltaTime) {
        if (sprite.getRotation() == 0)
            sprite.setY(sprite.getY() - MOVE_SPEED * deltaTime);
        if (sprite.getRotation() == 90)
            sprite.setX(sprite.getX() + MOVE_SPEED * deltaTime);
        if (sprite.getRotation() == 180)
            sprite.setY(sprite.getY() + MOVE_SPEED * deltaTime);
        if (sprite.getRotation() == 270)
            sprite.setX(sprite.getX() - MOVE_SPEED * deltaTime);
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }

    public Rectangle getBounds() {
        return sprite.getBoundingRectangle();
    }

    public boolean isOutOfScreen() {
        return (sprite.getX() < 0 || sprite.getX() > Gdx.graphics.getWidth()
             || sprite.getY() < 0 || sprite.getY() > Gdx.graphics.getHeight());
    }
}
