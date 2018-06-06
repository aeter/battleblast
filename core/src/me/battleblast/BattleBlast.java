package me.battleblast;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;


public class BattleBlast extends ApplicationAdapter {
    private TiledMap map;
    private AssetManager manager;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private PlayerTank player;
    private Sprite enemy;
    private SpriteBatch batch;

    @Override
    public void create () {
        manager = new AssetManager();
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class);
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("tanks.tmx", TiledMap.class);
        manager.finishLoading();
        map = manager.get("tanks.tmx", TiledMap.class);
        enemy = new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class));

        player = new PlayerTank();
        player.setSprite(new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class)));

        renderer = new OrthogonalTiledMapRenderer(map);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch = new SpriteBatch();
    }

    @Override
    public void render () {
        handleInput(); 

        // TODO - moveWorld();

        handleCollisions();

        // draw
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

    private void handleInput() {
        if(Gdx.input.isKeyPressed(Keys.LEFT)) player.moveLeft();
        if(Gdx.input.isKeyPressed(Keys.RIGHT)) player.moveRight();
        if(Gdx.input.isKeyPressed(Keys.UP)) player.moveUp();
        if(Gdx.input.isKeyPressed(Keys.DOWN)) player.moveDown();
    }

    private void handleCollisions() {
        MapObjects stabiles = map.getLayers().get("collidable").getObjects();
        for (MapObject stabile: stabiles) {
            Rectangle rectangle = ((RectangleMapObject) stabile).getRectangle();
            if (rectangle.overlaps(player.sprite.getBoundingRectangle())) {
                player.onCollisionWithStabile();
            }
        }
    }
}
