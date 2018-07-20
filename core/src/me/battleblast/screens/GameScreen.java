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
import me.battleblast.animations.BigBoomAnimation;
import me.battleblast.animations.SmallBoomAnimation;
import me.battleblast.animations.SmallSparksAnimation;
import me.battleblast.animations.TinyBoomAnimation;
import me.battleblast.BattleBlast;
import me.battleblast.entities.Bullet;
import me.battleblast.entities.EnemyTank;
import me.battleblast.entities.PlayerTank;
import me.battleblast.entities.Tank;
import me.battleblast.entities.Wall;


public class GameScreen implements Screen {
    public static Array<Bullet> ALL_BULLETS = new Array<Bullet>();

    private Array<Wall> walls = new Array<Wall>();
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
            enemy.update(delta, player.getBounds(), getCurrentWalls());
        }
        for (Bullet bullet: ALL_BULLETS) {
            bullet.move(delta);
        }
    }

    private void handleCollisions() {
        // wall collisions
        for (Iterator<Wall> w = walls.iterator(); w.hasNext();) {
            Wall wall = w.next();
            if (wall.getBounds().overlaps(player.getBounds())) {
                player.onCollisionWithObstacle();
            }
            for (EnemyTank enemy: enemies) {
                if (wall.getBounds().overlaps(enemy.getBounds())) {
                    enemy.onCollisionWithObstacle();
                }
            }
            for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
                Bullet bullet = i.next();
                if (wall.getBounds().overlaps(bullet.getBounds())) {
                    i.remove();
                    if (wall.isBreakable) {
                        w.remove();
                        animations.add(new SmallBoomAnimation(wall.getBounds().getX(), wall.getBounds().getY()));
                    } else {
                        animations.add(new SmallSparksAnimation(wall.getBounds().getX(), wall.getBounds().getY()));
                    }
                }
            }
        }

        // collision player<->tank
        for (EnemyTank enemy: enemies) {
            if (player.getBounds().overlaps(enemy.getBounds())) {
                player.onCollisionWithEnemy();
                enemy.onCollisionWithEnemy();
            }

        }

        // collisions tank<->bullet
        for (Bullet bullet: ALL_BULLETS) {
            if (player.getBounds().overlaps(bullet.getBounds()) && !bullet.isPlayerBullet) {
                // TODO - GAME OVER screen...
                Gdx.app.log("GAME OVER", "GAME OVER");
            }
            for (Iterator<EnemyTank> i = enemies.iterator(); i.hasNext(); ) {
                EnemyTank enemy  = i.next();
                if (enemy.getBounds().overlaps(bullet.getBounds()) && bullet.isPlayerBullet) {
                    bullet.markedForRemoval = true;
                    i.remove();
                    animations.add(new BigBoomAnimation(enemy.getBounds().getX(), enemy.getBounds().getY()));
                    // TODO - add boss tank when both tanks are neutralized.
                }
            }
        }

        // collisions bullet<->bullet
        for (int i = 0; i < ALL_BULLETS.size; i++) {
            for (int j = 0; j < ALL_BULLETS.size; j++) {
                Bullet b1 = ALL_BULLETS.get(i);
                Bullet b2 = ALL_BULLETS.get(j);
                if (i != j && b1.getBounds().overlaps(b2.getBounds())) {
                    b1.markedForRemoval = true;
                    b2.markedForRemoval = true;
                    float animationX = b1.getBounds().getX() + (b1.getBounds().getWidth() / 2);
                    float animationY = b1.getBounds().getY() + (b1.getBounds().getHeight() / 2);
                    animations.add(new TinyBoomAnimation(animationX, animationY));
                }
            }
        }
        for (Iterator<Bullet> i = ALL_BULLETS.iterator(); i.hasNext(); ) {
            Bullet bullet = i.next();
            if (bullet.isOutOfScreen()) i.remove();
            if (bullet.markedForRemoval) i.remove();
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
        for (EnemyTank enemy: enemies) {
            enemy.draw(game.batch);
        }
        for (Wall wall: walls) {
            if (wall.isBreakable) {
                wall.getSprite().draw(game.batch);
            }
        }
        for (Bullet bullet: ALL_BULLETS) {
            bullet.draw(game.batch);
        }
        drawAnimations();
        game.batch.end();
    }

    private void spawnEnemies() {
        EnemyTank enemy = new EnemyTank();
        enemy.setSprite(new Sprite(BattleBlast.getAtlas().findRegion("tank_dark_64x64")));
        enemy.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                Gdx.graphics.getHeight() - enemy.getSprite().getHeight());
        enemies.add(enemy);

        EnemyTank enemy2 = new EnemyTank();
        enemy2.setSprite(new Sprite(BattleBlast.getAtlas().findRegion("tank_dark_64x64")));
        enemy2.getSprite().setPosition(
                Gdx.graphics.getWidth() - enemy.getSprite().getWidth(),
                0);
        enemies.add(enemy2);
    }

    private void spawnPlayer() {
        player = new PlayerTank();
        player.setSprite(new Sprite(BattleBlast.getAtlas().findRegion("tank_blue_64x64")));
    }

    private void setupMap() {
        map = game.assets.get("tanks.tmx", TiledMap.class);
        renderer = new OrthogonalTiledMapRenderer(map);
        setupBreakableWalls();
        setupUnbreakableWalls();
    }

    private void setupCamera() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void setupBreakableWalls() {
        MapObjects breakableWalls = map.getLayers().get("breakable_obstacles").getObjects();
        for (MapObject wall: breakableWalls) {
            Rectangle bounds = ((RectangleMapObject) wall).getRectangle();
            walls.add(Wall.getBreakableInstance(bounds));
        }
    }

    private void setupUnbreakableWalls() {
        MapObjects unbreakableWalls = map.getLayers().get("unbreakable_obstacles").getObjects();
        for (MapObject wall: unbreakableWalls) {
            Rectangle bounds = ((RectangleMapObject) wall).getRectangle();
            walls.add(new Wall(bounds));
        }
    }

    private Array<Vector2> getCurrentWalls() {
        Array<Vector2> currentWalls = new Array<Vector2>();
        for (Wall wall: walls) {
            currentWalls.add(new Vector2(
                        wall.getBounds().getX() / BattleBlast.TILE_WIDTH,
                        wall.getBounds().getY() / BattleBlast.TILE_WIDTH));
        }
        return currentWalls;
    }

    private void drawAnimations() {
        for (Iterator<BaseAnimation> i = animations.iterator(); i.hasNext(); ) {
            BaseAnimation animation = i.next();
            if (animation.isOver()) {
                i.remove();
            } else {
                animation.draw(game.batch);
            }
        }
    }
}
