package me.battleblast.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class SmallBoomAnimation {
    private float x;
    private float y;
    private float elapsedTime = 0f;
    private TextureAtlas boomAtlas;
    private float ANIMATION_FRAME_DURATION = 1/30f;
    private Animation<TextureRegion> animation;

    public SmallBoomAnimation(float x, float y) {
        this.x = x;
        this.y = y;
        boomAtlas = new TextureAtlas(Gdx.files.internal("kenney_booms/booms2.atlas"));
        animation = new Animation<TextureRegion>(ANIMATION_FRAME_DURATION, boomAtlas.getRegions());
    }

    public boolean isOver() {
        return animation.isAnimationFinished(elapsedTime);
    }

    public void draw(SpriteBatch batch) {
        elapsedTime += Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(elapsedTime), x, y);
    }

    // TODO - see how to use the assetManager to avoid this disposal
    public void dispose() {
        boomAtlas.dispose();
    }
}
