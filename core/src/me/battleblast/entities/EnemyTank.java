package me.battleblast.entities;

import java.util.Random;

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
    private boolean reachedTarget = false;
    private Vector2 targetPosition;

    public void actClever() {
        // move around the map
        targetPosition = chooseNewWanderPosition();
        if (reachedTarget) {
            targetPosition = chooseNewWanderPosition();
            reachedTarget = false;
        } else {
            Vector2 graphStartPosition = new Vector2(simplify(sprite.getX()), simplify(sprite.getY()));
            Vector2 graphTargetPosition = new Vector2(simplify(targetPosition.x), simplify(targetPosition.y));
            Node nextNode = new PathFinding(getCurrentWalls()).getNextNode(graphStartPosition, graphTargetPosition);
            if (nextNode != null) {
                Gdx.app.log("nextNode", String.format("x: %d, y: %d", nextNode.x, nextNode.y));
                int targetX = nextNode.x * BattleBlast.TILE_WIDTH;
                int targetY = nextNode.y * BattleBlast.TILE_WIDTH;
                if (targetX < sprite.getX()) {
                    moveLeft();
                } else if (targetY < sprite.getY()) {
                    moveDown();
                } else if (targetX > sprite.getX()) {
                    moveRight();
                } else if (targetY > sprite.getY()) {
                    moveUp();
                }

                if (targetX == sprite.getX() && targetY == sprite.getY()) 
                    reachedTarget = true;

            }
        }

        if (new Random().nextInt(40) == 2) shoot();
    }

    private Vector2 chooseNewWanderPosition() {
        // TODO - write logic to really choose some position :)
        return new Vector2(10 * 32,  10 * 32);
    }

    private Array<Vector2> getCurrentWalls() {
        Array walls = new Array<Vector2>();
        for (Sprite s: GameScreen.ALL_BREAKABLE_OBSTACLES)
            walls.add(new Vector2(s.getX() / BattleBlast.TILE_WIDTH, s.getY() / BattleBlast.TILE_WIDTH));
        for (Vector2 v: GameScreen.ALL_UNBREAKABLE_OBSTACLES)
            walls.add(new Vector2(v.x / BattleBlast.TILE_WIDTH, v.y / BattleBlast.TILE_WIDTH));
        return walls;
    }

    // Pathfinding graph coordinates are like (5, 10)
    // Map coordinates are like (160, 320)
    // simplify is like 320 / 32.
    private float simplify (float coordinateInPixels) {
        return coordinateInPixels / BattleBlast.TILE_WIDTH;
    }

}
