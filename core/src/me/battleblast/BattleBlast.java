package me.battleblast;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;


public class BattleBlast extends Game {
    public static AssetManager assets;

    public SpriteBatch batch;
    
    public void create() {
        loadAssets();
        batch = new SpriteBatch();
        this.setScreen(new GameScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        batch.dispose();
        assets.dispose();
    }

    private void loadAssets() {
        assets = new AssetManager();
        assets.load("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class);
        assets.load("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class);
        assets.load("kenney_topdownTanksRedux/PNG/Retina/bulletDark1.png", Texture.class);
        assets.setLoader(TiledMap.class, new TmxMapLoader());
        assets.load("tanks.tmx", TiledMap.class);
        assets.setLoader(ParticleEffect.class, new ParticleEffectLoader(new InternalFileHandleResolver()));
        assets.load("effects/sparks.p", ParticleEffect.class);
        assets.finishLoading();
    }
}
