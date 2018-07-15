package me.battleblast.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
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
    private Vector2 lastKnownPlayerPosition = new Vector2(0, 0);
    private Array<Vector2> currentPath = new Array<Vector2>();
    private Vector2 currentTarget = new Vector2(0, 0);
    private boolean wallsHaveChanged = false;

    public void update(float delta, float playerX, float playerY, Array<Vector2> walls) {
        this.wallsHaveChanged = currentWalls.size != walls.size;
        this.currentWalls = walls;
        this.currentPlayerPosition.x = toTile(playerX);
        this.currentPlayerPosition.y = toTile(playerY);
        brain();
    }

    // TODO - remove commented logging stuff after fixing this method
    public boolean seesPlayer() {
        int pixelsTolerance = 5;
        boolean seenHorizontally = Math.abs(sprite.getX() - currentPlayerPosition.x) < pixelsTolerance;
        boolean seenVertically = Math.abs(sprite.getY() - currentPlayerPosition.y) < pixelsTolerance;
        boolean wallOnLineOfSight = false;
        for (Vector2 wall: currentWalls) {
            if (seenVertically) {
                wallOnLineOfSight = Math.abs(wall.x * BattleBlast.TILE_WIDTH - currentPlayerPosition.x) < pixelsTolerance;
            } else if (seenHorizontally) {
                wallOnLineOfSight = Math.abs(wall.y * BattleBlast.TILE_WIDTH - currentPlayerPosition.y) < pixelsTolerance;
            }
            if (wallOnLineOfSight) {
                //Gdx.app.log("wall", "wall");
                return false;
            }
        }
        //Gdx.app.log("seen: ", String.format("%s", seenHorizontally || seenVertically ? "true" : "false"));
        return seenVertically || seenHorizontally;
    }

    public boolean reachedPosition(Vector2 position) {
        return toTile(sprite.getX()) == toTile(position.x) && toTile(sprite.getY()) == toTile(position.y);
    }

    private int toInt(float f) {
        return MathUtils.floor(f);
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

    private boolean nPercentChance(int n) {
        return MathUtils.random() <= 0.01 * n;
    }

    // TODO - uncommment after fixing the general logic
    private void chooseNewPatrollingPosition() {
        // we do "- BattleBlast.TILE_WIDTH" for the positions, because a tank
        // is drawn on 4 tiles, like:
        // @@
        // @@    
        Vector2 bottomLeftCorner = new Vector2(0, 0);
        //Vector2 bottomRightCorner = new Vector2(BattleBlast.TILE_WIDTH * BattleBlast.MAP_WIDTH - BattleBlast.TILE_WIDTH, 0);
        //Vector2 topLeftCorner = new Vector2(0, BattleBlast.TILE_WIDTH * BattleBlast.MAP_HEIGHT - BattleBlast.TILE_WIDTH);
        Vector2 topRightCorner = new Vector2(
                BattleBlast.TILE_WIDTH * BattleBlast.MAP_WIDTH - BattleBlast.TILE_WIDTH,
                BattleBlast.TILE_WIDTH * BattleBlast.MAP_HEIGHT - BattleBlast.TILE_WIDTH);
        if (nextPatrollingPosition.epsilonEquals(bottomLeftCorner)) {
            nextPatrollingPosition = topRightCorner;
        } else if (nextPatrollingPosition.epsilonEquals(topRightCorner)) {
            nextPatrollingPosition = bottomLeftCorner;
        }
        /*
        } else if (nextPatrollingPosition.epsilonEquals(bottomRightCorner)) {
            nextPatrollingPosition = topRightCorner;
        } else if (nextPatrollingPosition.epsilonEquals(topRightCorner)) {
            nextPatrollingPosition = topLeftCorner;
        } else if (nextPatrollingPosition.epsilonEquals(topLeftCorner)) {
            nextPatrollingPosition = bottomLeftCorner;
        }
        */
    }

    private void brain() {
        if (currentPath.size == 0) {
            chooseNewPatrollingPosition();
            recalculateCurrentPath(nextPatrollingPosition);
        }
        if (reachedPosition(nextPatrollingPosition)) {
            chooseNewPatrollingPosition();
            recalculateCurrentPath(nextPatrollingPosition);
        }
        if (reachedPosition(lastKnownPlayerPosition)) {
            recalculateCurrentPath(nextPatrollingPosition);
        }
        if (seesPlayer()) {
            lastKnownPlayerPosition = currentPlayerPosition;
            recalculateCurrentPath(lastKnownPlayerPosition);
        }
        if (wallsHaveChanged) {
            recalculateCurrentPath(currentTarget);
        }
        if (nPercentChance(3)) shoot();
        followPath();
    }

    private void recalculateCurrentPath(Vector2 position) {
        this.currentTarget = position;
        Vector2 graphStartPosition = new Vector2(simplify(toTile(sprite.getX())), simplify(toTile(sprite.getY())));
        Vector2 graphTargetPosition = new Vector2(simplify(toTile(position.x)), simplify(toTile(position.y)));
        this.currentPath = new PathFinding(currentWalls).getFullPath(graphStartPosition, graphTargetPosition);
    }

    private void followPath() {
        if (currentPath.size == 0) return;

        Vector2 nextNode = currentPath.first();
        float targetX = nextNode.x * BattleBlast.TILE_WIDTH;
        float targetY = nextNode.y * BattleBlast.TILE_WIDTH;
        if (reachedPosition(new Vector2(targetX, targetY))) {
            currentPath.removeIndex(0);
            followPath();
        } else {
            if (targetX < sprite.getX()) {
                moveLeft();
            } else if (targetY < sprite.getY()) {
                moveDown();
            } else if (targetX > sprite.getX()) {
                moveRight();
            } else if (targetY > sprite.getY()) {
                moveUp();
            }
        }
    }
}
