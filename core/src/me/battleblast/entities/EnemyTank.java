package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;

import me.battleblast.BattleBlast;
import me.battleblast.pathfinding.Node;
import me.battleblast.pathfinding.PathFinding;
import me.battleblast.screens.GameScreen;


public class EnemyTank extends Tank {
    private long lastDecision = TimeUtils.millis();
    private static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    public void actClever() {
        Array walls = new Array<Vector2>();
        for (Sprite s: GameScreen.ALL_BREAKABLE_OBSTACLES) {
            walls.add(new Vector2(s.getX() / BattleBlast.TILE_WIDTH, s.getY() / BattleBlast.TILE_WIDTH));
        }
        for (Vector2 v: GameScreen.ALL_UNBREAKABLE_OBSTACLES) {
            walls.add(new Vector2(v.x / BattleBlast.TILE_WIDTH, v.y / BattleBlast.TILE_WIDTH));
        }
        Vector2 startPosition = new Vector2(sprite.getX() / BattleBlast.TILE_WIDTH, sprite.getY() / BattleBlast.TILE_WIDTH);

        //TODO - remove after tests pass
        Vector2 endPosition = new Vector2(10, 10);
        Node nextNode = new PathFinding(walls).getNextNode(startPosition, endPosition);
        Gdx.app.log("nextNode", String.format("x: %d, y: %d", nextNode.x, nextNode.y));


        if (TimeUtils.millis() - lastDecision > 2 * ONE_SECOND_IN_MILLISECONDS) {
            changeDirection();
            lastDecision = TimeUtils.millis();
        } else if (atTileStart() && random() == 9) { 
            changeDirection();
        } else {
            keepDirection();
        }

        if (random() == 2) shoot();
    }

    private void changeDirection() {
        int random_int = random();
        if (random_int < 2) {
            moveLeft();
        } else if (random_int < 4) {
            moveRight();
        } else if (random_int < 6) {
            moveUp();
        } else {
            moveDown();
        }
    }

    private void keepDirection() {
        if (previousX < sprite.getX()) {
            moveRight();
        } else if (previousY < sprite.getY()) { 
            moveUp();
        } else if (previousX > sprite.getX()) {
            moveLeft();
        } else {
            moveDown();
        }
    }

    private int random() {
        return (int) (Math.random() * 10);
    }

    private boolean atTileStart() {
        return sprite.getX() % 32 == 0 || sprite.getY() % 32 == 0;
    }

}
