package me.battleblast;

import java.util.Iterator;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;


public class GameScreen implements Screen {
    public static Array<Bullet> ALL_BULLETS = new Array<Bullet>();
    public static Array<Sprite> ALL_BREAKABLES = new Array<Sprite>();

    private final BattleBlast game;
    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private PlayerTank player;
    private EnemyTank enemy;

    public GameScreen(final BattleBlast game) {
        this.game = game;

        setupMap();
        setupCamera();
        spawnEnemy();
        spawnPlayer();
    }

    @Override
    public void render(float delta) {
        handleInput(); 
        moveWorld();
        handleCollisions();
        draw();
    }

    @Override
    public void dispose() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void show() {}

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    private void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            player.moveLeft();
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            player.moveRight();
        } else if (Gdx.input.isKeyPressed(Keys.UP)) {
            player.moveUp();
        } else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            player.moveDown();
        } 
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
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
        MapObjects stabiles = map.getLayers().get("stabiles").getObjects();

        for (MapObject stabile: stabiles) {
            Rectangle stabileBounds = ((RectangleMapObject) stabile).getRectangle();
            if (stabileBounds.overlaps(player.getBounds())) {
                player.onCollisionWithStabile();
            }
            if (stabileBounds.overlaps(enemy.getBounds())) {
                enemy.onCollisionWithStabile();
            }
            for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
                Bullet bullet = i.next();
                if (stabileBounds.overlaps(bullet.getBounds()) && 
                    !stabile.getProperties().get("breakable", Boolean.class)) {
                    i.remove(); 
                }
            }
        }

        // collision Bullet<->Breakable object
        for (Iterator<Bullet> b = ALL_BULLETS.iterator(); b.hasNext(); ) {
            Bullet bullet = b.next();
            for (Iterator<Sprite> s = ALL_BREAKABLES.iterator(); s.hasNext(); ) {
                Sprite sprite = s.next();
                if (bullet.getBounds().overlaps(sprite.getBoundingRectangle())) {
                    b.remove();
                    s.remove();
                    // TODO - boom effect
                }
            }
        }

        if (player.getBounds().overlaps(enemy.getBounds())) {
            player.onCollisionWithEnemy();
            enemy.onCollisionWithEnemy();
        }
        for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
            Bullet bullet = i.next();
            if (bullet.isOutOfScreen()) i.remove();
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        renderer.setView(camera);
        renderer.render();

        game.batch.begin();
        player.draw(game.batch);
        enemy.draw(game.batch);
        for (Sprite breakable: ALL_BREAKABLES) {
            breakable.draw(game.batch);
        }
        for (Bullet bullet: ALL_BULLETS) {
            bullet.draw(game.batch);
        }
        game.batch.end();
    }

    private void spawnEnemy() {
        enemy = new EnemyTank();
        enemy.setSprite(new Sprite(game.assets.get("kenney_topdownTanksRedux/PNG/Retina/tank_dark_64x64.png", Texture.class)));
        enemy.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                Gdx.graphics.getHeight() - enemy.getSprite().getHeight());
    }

    private void spawnPlayer() {
        player = new PlayerTank();
        player.setSprite(new Sprite(game.assets.get("kenney_topdownTanksRedux/PNG/Retina/tank_blue_64x64.png", Texture.class)));
    }

    private void setupMap() {
        map = game.assets.get("tanks.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(map);
        setupBreakableTiles();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupBreakableTiles() {
        MapObjects stabiles = map.getLayers().get("stabiles").getObjects();
        for (MapObject stabile: stabiles) {
            if (stabile.getProperties().get("breakable", Boolean.class)) {
                Rectangle stabileBounds = ((RectangleMapObject) stabile).getRectangle();
                Sprite breakableSprite = new Sprite(game.assets.get("kenney_topdownTanksRedux/PNG/Retina/crateWood.png", Texture.class));
                breakableSprite.setBounds(stabileBounds.getX(), stabileBounds.getY(), stabileBounds.getWidth(), stabileBounds.getHeight());
                ALL_BREAKABLES.add(breakableSprite);
            }
        }
    }
}
