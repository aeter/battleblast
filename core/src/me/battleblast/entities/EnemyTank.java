package me.battleblast.entities;

import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.msg.Telegram;
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
    public StateMachine<EnemyTank, TankState> state;
    private Array<Vector2> currentWalls = new Array<Vector2>();
    private Vector2 currentPlayerPosition = new Vector2(0, 0);
    private Vector2 nextPatrollingPosition = new Vector2(0, 0);
    private Vector2 lastKnownPlayerPosition;

    public EnemyTank() {
        state = new DefaultStateMachine<EnemyTank, TankState>(this, TankState.PATROLLING);
    }

    public void update(float delta, float playerX, float playerY, Array<Vector2> walls) {
        this.currentWalls = walls;
        this.currentPlayerPosition.x = playerX;
        this.currentPlayerPosition.y = playerY;
        state.update();
    }

    public void patrol() {
        if (reachedPosition(nextPatrollingPosition)) {
            chooseNewPatrollingPosition();
        }
        moveTowards(nextPatrollingPosition);
        if (nPercentChance(2)) shoot();
    }

    public void chase() {
        moveTowards(lastKnownPlayerPosition);
        if (nPercentChance(3)) shoot();
    }

    public void onSeenPlayer() {
        lastKnownPlayerPosition = currentPlayerPosition;
    }

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

    public boolean reachedLastKnownPlayerPosition() {
        return reachedPosition(lastKnownPlayerPosition);
    }

    public boolean reachedNextPatrollingPosition() {
        return reachedPosition(nextPatrollingPosition);
    }

    private int toInt(float f) {
        return MathUtils.floor(f);
    }

    private float toTile(float coordinate) {
        return coordinate - (coordinate % BattleBlast.TILE_WIDTH);
    }

    private void moveTowards(Vector2 position) {
        Vector2 graphStartPosition = new Vector2(simplify(sprite.getX()), simplify(sprite.getY()));
        Vector2 graphTargetPosition = new Vector2(simplify(position.x), simplify(position.y));
        // TODO - optimise to not do full pathfinding on every frame
        Node nextNode = new PathFinding(currentWalls).getNextNode(graphStartPosition, graphTargetPosition);
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
        }
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

    private enum TankState implements State<EnemyTank> {
        PATROLLING() {
            @Override
            public void update(EnemyTank tank) {
                if (tank.seesPlayer()) {
                    tank.onSeenPlayer();
                    tank.state.changeState(CHASING);
                } else {
                    tank.patrol();
                }
            }
        },
        CHASING() {
            @Override
            public void update(EnemyTank tank) {
                if (tank.seesPlayer()) {
                    tank.onSeenPlayer();
                }
                if (tank.reachedLastKnownPlayerPosition()) {
                    tank.state.changeState(PATROLLING);
                } else {
                    tank.chase();
                }
            }
        };

        @Override
        public void enter(EnemyTank tank) {}

        @Override
        public void exit(EnemyTank tank) {}

        @Override
        public boolean onMessage(EnemyTank tank, Telegram telegram) {
            return false; // we don't use messaging.
        }
    }
}
