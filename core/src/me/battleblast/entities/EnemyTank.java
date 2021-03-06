package me.battleblast.entities;

import java.lang.Math;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import me.battleblast.BattleBlast;
import me.battleblast.pathfinding.Node;
import me.battleblast.pathfinding.PathFinding;


public class EnemyTank extends Tank {
    private Array<Vector2> currentWalls = new Array<Vector2>();
    private Vector2 currentPlayerPosition = new Vector2(0, 0);
    private Vector2 nextPatrollingPosition = new Vector2(0, 0);
    private Array<Vector2> currentPath = new Array<Vector2>();
    private Vector2 currentTarget = new Vector2(0, 0);
    private boolean wallsHaveChanged = false;
    private long everyNSecondsTimer = TimeUtils.millis();
    private float everyNSecondsX = 0f;
    private float everyNSecondsY = 0f;

    public void update(float delta, Rectangle playerBounds, Array<Vector2> walls) {
        wallsHaveChanged = currentWalls.size != walls.size;
        currentWalls = walls;
        currentPlayerPosition.x = toTile(playerBounds.getX());
        currentPlayerPosition.y = toTile(playerBounds.getY());
        brain();
    }

    protected boolean isTimeToShootAgain() {
        return nPercentChance(3);
    }

    protected boolean nPercentChance(int n) {
        return MathUtils.random() <= 0.01 * n;
    }

    private void brain() {
        if (this instanceof BossTank) {
            recalculateCurrentPath(currentPlayerPosition);
        }
        if (currentPath.size == 0 || stayedStillForSeconds(3)) {
            chooseNewPatrollingPosition();
            recalculateCurrentPath(nextPatrollingPosition);
        }
        if (wallsHaveChanged) {
            recalculateCurrentPath(currentTarget);
        }
        if (isTimeToShootAgain()) {
            shoot();
        }
        followPath();
    }

    private boolean stayedStillForSeconds(int nSeconds) {
        if (everyNSecondsX != getBounds().getX() || everyNSecondsY != getBounds().getY()) {
            everyNSecondsX = getBounds().getX();
            everyNSecondsY = getBounds().getY();
            everyNSecondsTimer = TimeUtils.millis();
            return false;
        }
        int oneSecondInMilliSeconds = 1000;
        if (TimeUtils.millis() - everyNSecondsTimer > nSeconds * oneSecondInMilliSeconds) {
            everyNSecondsX = getBounds().getX();
            everyNSecondsY = getBounds().getY();
            everyNSecondsTimer = TimeUtils.millis();
            return true;
        }
        return false;
    }

    private void recalculateCurrentPath(Vector2 position) {
        this.currentTarget = position;
        Vector2 graphStartPosition = new Vector2(simplify(toTile(sprite.getX())), simplify(toTile(sprite.getY())));
        Vector2 graphTargetPosition = new Vector2(simplify(toTile(position.x)), simplify(toTile(position.y)));
        this.currentPath = new PathFinding(currentWalls).getFullPath(graphStartPosition, graphTargetPosition);
    }

    private void followPath() {
        if (currentPath.size == 0) {
            return;
        }

        Vector2 nextNode = currentPath.first().cpy();
        nextNode.x *= BattleBlast.TILE_WIDTH;
        nextNode.y *= BattleBlast.TILE_WIDTH;
        if (reachedPosition(nextNode)) {
            currentPath.removeIndex(0);
        } else {
            if (nextNode.x < sprite.getX()) {
                move(Direction.LEFT);
            } else if (nextNode.y < sprite.getY()) {
                move(Direction.DOWN);
            } else if (nextNode.x > sprite.getX()) {
                move(Direction.RIGHT);
            } else if (nextNode.y > sprite.getY()) {
                move(Direction.UP);
            }
        }
    }

    private void chooseNewPatrollingPosition() {
        // a tank is drawn on 4 tiles like:
        // @@
        // @@
        Vector2 bottomLeftCorner = new Vector2(0, 0);
        Vector2 bottomRightCorner = new Vector2(BattleBlast.TILE_WIDTH * BattleBlast.MAP_WIDTH - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH, 0);
        Vector2 topLeftCorner = new Vector2(0, BattleBlast.TILE_WIDTH * BattleBlast.MAP_HEIGHT - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH);
        Vector2 topRightCorner = new Vector2(
                BattleBlast.TILE_WIDTH * BattleBlast.MAP_WIDTH - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH,
                BattleBlast.TILE_WIDTH * BattleBlast.MAP_HEIGHT - BattleBlast.TILE_WIDTH - BattleBlast.TILE_WIDTH);
        if (nextPatrollingPosition.epsilonEquals(bottomLeftCorner)) {
            nextPatrollingPosition = bottomRightCorner;
        } else if (nextPatrollingPosition.epsilonEquals(bottomRightCorner)) {
            nextPatrollingPosition = topRightCorner;
        } else if (nextPatrollingPosition.epsilonEquals(topRightCorner)) {
            nextPatrollingPosition = topLeftCorner;
        } else if (nextPatrollingPosition.epsilonEquals(topLeftCorner)) {
            nextPatrollingPosition = bottomLeftCorner;
        } else {
            nextPatrollingPosition = bottomLeftCorner;
        }
    }

    private boolean reachedPosition(Vector2 position) {
        return floatsAreEqual(sprite.getX(), position.x) && floatsAreEqual(sprite.getY(), position.y);
    }

    private boolean floatsAreEqual(float x, float y) {
        return Math.abs(x - y) < 0.00001;
    }

    private float toTile(float coordinate) {
        return coordinate - (coordinate % BattleBlast.TILE_WIDTH);
    }

    // Pathfinding graph coordinates are like (5, 10)
    // Map coordinates are like (160, 320)
    // simplify is like 320 / 32.
    private float simplify (float coordinateInPixels) {
        return coordinateInPixels / BattleBlast.TILE_WIDTH;
    }
}
