package me.battleblast.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class TinyBoomAnimation extends BaseAnimation {
    private float x;
    private float y;
    private float elapsedTime = 0f;
    private float ANIMATION_FRAME_DURATION = 1/90f;
    private Animation<TextureRegion> animation;

    public TinyBoomAnimation(float x, float y) {
        this.x = x;
        this.y = y;
        animation = new Animation<TextureRegion>(ANIMATION_FRAME_DURATION, AtlasRegionsCache.getTinyBoomRegions());
    }

    @Override
    public boolean isOver() {
        return animation.isAnimationFinished(elapsedTime);
    }

    @Override
    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(elapsedTime), x, y);
    }
}
