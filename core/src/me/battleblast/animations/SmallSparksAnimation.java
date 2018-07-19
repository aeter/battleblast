package me.battleblast.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.battleblast.BattleBlast;


public class SmallSparksAnimation extends BaseAnimation {
    private float x;
    private float y;
    private ParticleEffect animation;

    public SmallSparksAnimation(float x, float y) {
        this.x = x;
        this.y = y;
        animation = new ParticleEffect(BattleBlast.assets.get("effects/sparks.p", ParticleEffect.class));
        animation.getEmitters().first().setPosition(x, y);
        animation.scaleEffect(0.3f);
        animation.start();
    }

    @Override
    public boolean isOver() {
        return animation.isComplete();
    }

    @Override
    public void draw(SpriteBatch batch) {
        animation.update(Gdx.graphics.getDeltaTime());
        animation.draw(batch);
    }
}
