package me.battleblast;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;


public class BattleBlast extends ApplicationAdapter {
    private TiledMap map;
    private AssetManager manager;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private Sprite player;
    private Sprite enemy;
    private SpriteBatch batch;

    @Override
    public void create () {
        manager = new AssetManager();
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_blue.png", Texture.class);
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_dark.png", Texture.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("tanks.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("tanks.tmx", TiledMap.class);
        player = new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_blue.png", Texture.class));
        enemy = new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_dark.png", Texture.class));
        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();

        GameInputHandler inputHandler = new GameInputHandler();
        inputHandler.setPlayer(player);
        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void render () {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.begin();
        player.draw(batch);
        enemy.setPosition(Gdx.graphics.getWidth() - enemy.getWidth(), Gdx.graphics.getHeight() - enemy.getHeight());
        enemy.draw(batch);
        batch.end();
    }

    @Override
    public void dispose () {
        batch.dispose();
        manager.dispose();
    }
}
