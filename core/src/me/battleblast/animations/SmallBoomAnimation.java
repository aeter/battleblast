package me.battleblast.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import me.battleblast.BattleBlast;


public class SmallBoomAnimation extends BaseAnimation {
    private float x;
    private float y;
    private float elapsedTime = 0f;
    private float ANIMATION_FRAME_DURATION = 1/60f;
    private Animation<TextureRegion> animation;
    private static Array<TextureAtlas.AtlasRegion> animationRegions;

    public SmallBoomAnimation(float x, float y) {
        this.x = x;
        this.y = y;
        animation = new Animation<TextureRegion>(ANIMATION_FRAME_DURATION, SmallBoomAnimation.cachedAnimationRegions());
    }

    /*
     * The documentation of TextureAtlas.findRegions() says:
     * ```
     * This method uses string comparison...so the result should be cached
     * rather than calling this method multiple times
     * ```
     * So this is why we do the whole `private static` acrobatics - for
     * caching it once and forever (because we make MANY instances of this class)
     */
    private static Array<AtlasRegion> cachedAnimationRegions() {
        if (SmallBoomAnimation.animationRegions == null) {
            SmallBoomAnimation.animationRegions = BattleBlast.getAtlas().findRegions("boomSmall");
        }
        return SmallBoomAnimation.animationRegions;
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
