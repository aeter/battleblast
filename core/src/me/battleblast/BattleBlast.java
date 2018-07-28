package me.battleblast;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import me.battleblast.screens.MainMenuScreen;


public class BattleBlast extends Game {
    public static final int MAP_WIDTH = 20; // 20 tiles
    public static final int MAP_HEIGHT = 20; // 20 tiles
    public static final int TILE_WIDTH = 32; // 32 pixels
    public static final float INITIAL_MUSIC_VOLUME = 0.1f;

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

    public static TextureAtlas getAtlas() {
        return assets.get("tanks.atlas", TextureAtlas.class);
    }

    private void loadAssets() {
        assets = new AssetManager();
        assets.load("tanks.atlas", TextureAtlas.class);
        assets.load("boom_sounds_pack_dklon/boom5.wav", Sound.class);
        assets.load("boom_sounds_pack_dklon/boom9.wav", Sound.class);
        assets.load("snd_music_victorytheme_0.ogg", Music.class);
        assets.load("snd_music2.ogg", Music.class);
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
        music.setVolume(INITIAL_MUSIC_VOLUME);
        music.play();
    }
}
