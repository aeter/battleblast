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
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class BattleBlast extends ApplicationAdapter {
    public static Array<Bullet> ALL_BULLETS = new Array<Bullet>();
    private static AssetManager manager;

    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private PlayerTank player;
    private EnemyTank enemy;
    private SpriteBatch batch;

    public static AssetManager getAssetManager() {
        return manager;
    }

    @Override
    public void create() {
        loadAssets();
        setupMap();
        setupCamera();
        spawnEnemy();
        spawnPlayer();
        batch = new SpriteBatch();
    }

    @Override
    public void render() {
        handleInput(); 
        moveWorld();
        handleCollisions();
        draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        manager.dispose();
    }

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Keys.UP)) {
            player.moveUp();
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            player.moveDown();
        } else if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            player.shoot();
        }
    }

    private void moveWorld() {
        enemy.move();
        for (Bullet bullet: ALL_BULLETS) {
            bullet.move();
        }
    }

    private void handleCollisions() {
        MapObjects stabiles = map.getLayers().get("collidable").getObjects();
        for (MapObject stabile: stabiles) {
            Rectangle rectangle = ((RectangleMapObject) stabile).getRectangle();
            if (rectangle.overlaps(player.getSprite().getBoundingRectangle())) {
                player.onCollisionWithStabile();
            }
            if (rectangle.overlaps(enemy.getSprite().getBoundingRectangle())) {
                enemy.onCollisionWithStabile();
            }
        }
            // TODO - remove bullet from ALL_BULLETS if out of screen
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        batch.begin();
        player.draw(batch);
        enemy.draw(batch);
        for (Bullet bullet: ALL_BULLETS) {
            bullet.draw(batch);
        }
        batch.end();
    }

    private void loadAssets() {
        manager = new AssetManager();
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class);
        manager.load("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class);
        manager.load("kenney_topdownTanksRedux/PNG/Retina/bulletDark1.png", Texture.class);
        manager.setLoader(TiledMap.class, new TmxMapLoader());
        manager.load("tanks.tmx", TiledMap.class);
        manager.finishLoading();
    }

    private void spawnEnemy() {
        enemy = new EnemyTank();
        enemy.setSprite(new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class)));
        enemy.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                Gdx.graphics.getHeight() - enemy.getSprite().getHeight());
    }

    private void spawnPlayer() {
        player = new PlayerTank();
        player.setSprite(new Sprite(manager.get("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class)));
    }

    private void setupMap() {
        map = manager.get("tanks.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(map);
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }
}
