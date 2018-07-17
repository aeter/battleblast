package me.battleblast.screens;

import java.util.Iterator;

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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import me.battleblast.animations.BaseAnimation;
import me.battleblast.animations.SmallBoomAnimation;
import me.battleblast.animations.SmallSparksAnimation;
import me.battleblast.BattleBlast;
import me.battleblast.entities.Tank;
import me.battleblast.entities.EnemyTank;
import me.battleblast.entities.PlayerTank;
import me.battleblast.entities.Bullet;


public class GameScreen implements Screen {
    public static Array<Bullet> ALL_BULLETS = new Array<Bullet>();
    public static Array<Sprite> ALL_BREAKABLE_OBSTACLES = new Array<Sprite>();
    public static Array<Vector2> ALL_UNBREAKABLE_OBSTACLES = new Array<Vector2>();

    private Array<BaseAnimation> animations = new Array<BaseAnimation>();
    private Array<EnemyTank> enemies = new Array<EnemyTank>();
    private final BattleBlast game;
    private TiledMap map;
    private OrthographicCamera camera;
    private OrthogonalTiledMapRenderer renderer;
    private PlayerTank player;

    public GameScreen(final BattleBlast game) {
        this.game = game;
        setupMap();
        setupCamera();
        spawnEnemies();
        spawnPlayer();
    }

    @Override
    public void render(float delta) {
        handleInput(); 
        moveWorld(delta);
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

    public PlayerTank getPlayer() {
        return player;
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
        } 
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            player.shoot();
        }
    }

    private void moveWorld(float delta) {
        for (EnemyTank enemy: enemies) {
            enemy.update(delta, player.getBounds().getX(), player.getBounds().getY(), getCurrentWalls());
        }
        for (Bullet bullet: ALL_BULLETS) {
            bullet.move();
        }
    }

    private void handleCollisions() {
        // collisions with unbreakable objects
        MapObjects stabiles = map.getLayers().get("unbreakable_obstacles").getObjects();
        for (MapObject stabile: stabiles) {
            Rectangle stabileBounds = ((RectangleMapObject) stabile).getRectangle();
            if (stabileBounds.overlaps(player.getBounds())) {
                player.onCollisionWithObstacle();
            }
            for (EnemyTank enemy: enemies) {
                if (stabileBounds.overlaps(enemy.getBounds())) {
                    enemy.onCollisionWithObstacle();
                }
            }
            for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
                Bullet bullet = i.next();
                if (stabileBounds.overlaps(bullet.getBounds())) {
                    i.remove(); 
                    animations.add(new SmallSparksAnimation(stabileBounds.getX(), stabileBounds.getY()));
                }
            }
        }

        // collisions with breakable objects
        for (Iterator<Sprite> s = ALL_BREAKABLE_OBSTACLES.iterator(); s.hasNext(); ) {
            Sprite breakable = s.next();
            Rectangle breakableBounds = breakable.getBoundingRectangle();
            if (breakableBounds.overlaps(player.getBounds())) {
                player.onCollisionWithObstacle();
            }
            for (EnemyTank enemy: enemies) {
                if (breakableBounds.overlaps(enemy.getBounds())) {
                    enemy.onCollisionWithObstacle();
                }
            }
            for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
                Bullet bullet = i.next();
                if (breakableBounds.overlaps(bullet.getBounds())) {
                    i.remove();
                    s.remove();
                    animations.add(new SmallBoomAnimation(breakableBounds.getX(), breakableBounds.getY()));
                }
            }
        }
            

        for (EnemyTank enemy: enemies) {
            if (player.getBounds().overlaps(enemy.getBounds())) {
                player.onCollisionWithEnemy();
                enemy.onCollisionWithEnemy();
            }
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

        for (Iterator<BaseAnimation> i = animations.iterator(); i.hasNext(); ) {
            BaseAnimation animation = i.next();
            if (animation.isOver()) {
                animation.dispose();
                i.remove();
            } else {
                animation.draw(game.batch);
            }
        }

        player.draw(game.batch);
        for (EnemyTank enemy: enemies) {
            enemy.draw(game.batch);
        }
        for (Sprite breakable: ALL_BREAKABLE_OBSTACLES) {
            breakable.draw(game.batch);
        }
        for (Bullet bullet: ALL_BULLETS) {
            bullet.draw(game.batch);
        }
        game.batch.end();
    }

    private void spawnEnemies() {
        EnemyTank enemy = new EnemyTank();
        enemy.setSprite(new Sprite(game.assets.get("kenney_tank_images/tank_dark_64x64.png", Texture.class)));
        enemy.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                Gdx.graphics.getHeight() - enemy.getSprite().getHeight());
        enemy.getSprite().setBounds(enemy.getSprite().getX(), enemy.getSprite().getY(), 63, 63);
        enemies.add(enemy);

        EnemyTank enemy2 = new EnemyTank();
        enemy2.setSprite(new Sprite(game.assets.get("kenney_tank_images/tank_dark_64x64.png", Texture.class)));
        enemy2.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                0);
        enemy2.getSprite().setBounds(enemy2.getSprite().getX(), enemy2.getSprite().getY(), 63, 63);
        enemies.add(enemy2);
    }

    private void spawnPlayer() {
        player = new PlayerTank();
        player.setSprite(new Sprite(game.assets.get("kenney_tank_images/tank_blue_64x64.png", Texture.class)));
        player.getSprite().setBounds(player.getSprite().getX(), player.getSprite().getY(), 63, 63);
    }

    private void setupMap() {
        map = game.assets.get("tanks.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(map);
        setupBreakableTiles();
        setupUnbreakableTiles();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupBreakableTiles() {
        MapObjects breakables = map.getLayers().get("breakable_obstacles").getObjects();
        for (MapObject breakable: breakables) {
            Rectangle breakableBounds = ((RectangleMapObject) breakable).getRectangle();
            Sprite breakableSprite = new Sprite(game.assets.get("kenney_tank_images/crateWood_32x32.png", Texture.class));
            breakableSprite.setBounds(breakableBounds.getX(), breakableBounds.getY(), breakableBounds.getWidth(), breakableBounds.getHeight());
            ALL_BREAKABLE_OBSTACLES.add(breakableSprite);
        }
    }

    private void setupUnbreakableTiles() {
        MapObjects stabiles = map.getLayers().get("unbreakable_obstacles").getObjects();
        for (MapObject stabile: stabiles) {
            Rectangle stabileBounds = ((RectangleMapObject) stabile).getRectangle();
            ALL_UNBREAKABLE_OBSTACLES.add(new Vector2(stabileBounds.getX(), stabileBounds.getY()));
        }
    }

    private Array<Vector2> getCurrentWalls() {
        Array<Vector2> walls = new Array<Vector2>();
        for (Sprite s: ALL_BREAKABLE_OBSTACLES)
            walls.add(new Vector2(s.getX() / BattleBlast.TILE_WIDTH, s.getY() / BattleBlast.TILE_WIDTH));
        for (Vector2 v: ALL_UNBREAKABLE_OBSTACLES)
            walls.add(new Vector2(v.x / BattleBlast.TILE_WIDTH, v.y / BattleBlast.TILE_WIDTH));
        return walls;
    }
}
