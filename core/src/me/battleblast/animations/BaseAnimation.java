package me.battleblast.animations;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public abstract class BaseAnimation {
    public abstract boolean isOver(); 

    public abstract void draw(SpriteBatch batch);

    public abstract void dispose();
}
