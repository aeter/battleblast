package me.battleblast.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import me.battleblast.BattleBlast;


public class Wall {
    public boolean isBreakable = false;
    private Sprite sprite;
    private Rectangle bounds;

    public Wall(Rectangle bounds) {
        this.bounds = bounds;
    }

    public static Wall getBreakableInstance(Rectangle bounds) {
        Wall wall = new Wall(bounds);
        Sprite sprite = new Sprite(BattleBlast.getAtlas().findRegion("crateWood_32x32"));
        sprite.setBounds(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
        wall.setSprite(sprite);
        wall.isBreakable = true;
        return wall;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Sprite getSprite() {
        return sprite;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
    }

    public void draw(SpriteBatch sb) {
        sprite.draw(sb);
    }
}
