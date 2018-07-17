package me.battleblast;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import me.battleblast.screens.MainMenuScreen;


public class BattleBlast extends Game {
    public static final int MAP_WIDTH = 20; // 20 tiles
    public static final int MAP_HEIGHT = 20; // 20 tiles
    public static final int TILE_WIDTH = 32; // 32 pixels

    public static AssetManager assets;

    public SpriteBatch batch;
    public Music music;
    
    public void create() {
        loadAssets();
        playMusic();
        batch = new SpriteBatch();

        this.setScreen(new MainMenuScreen(this));
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
        assets.load("kenney_tank_images/tank_blue_64x64.png", Texture.class);
        assets.load("kenney_tank_images/tank_dark_64x64.png", Texture.class);
        assets.load("kenney_tank_images/bulletDark2_8x16.png", Texture.class);
        assets.load("kenney_tank_images/crateWood_32x32.png", Texture.class);
        assets.load("snd_music_victorytheme_0.ogg", Music.class);
        assets.setLoader(TiledMap.class, new TmxMapLoader());
        assets.load("tanks.tmx", TiledMap.class);
        assets.setLoader(ParticleEffect.class, new ParticleEffectLoader(new InternalFileHandleResolver()));
        assets.load("effects/sparks.p", ParticleEffect.class);
        assets.setLoader(Skin.class, new SkinLoader(new InternalFileHandleResolver()));
        assets.load("ui/star-soldier/skin/star-soldier-ui.json", Skin.class);
        assets.finishLoading();
    }

    private void playMusic() {
        music = assets.get("snd_music_victorytheme_0.ogg", Music.class);
        music.setLooping(true);
        music.play();
    }
}
