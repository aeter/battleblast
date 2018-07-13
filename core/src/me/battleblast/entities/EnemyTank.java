package me.battleblast.entities;

import java.util.Random;
import java.lang.Math;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;

import me.battleblast.BattleBlast;
import me.battleblast.pathfinding.Node;
import me.battleblast.pathfinding.PathFinding;
import me.battleblast.screens.GameScreen;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.fsm.StateMachine;
import com.badlogic.gdx.ai.fsm.DefaultStateMachine;
import com.badlogic.gdx.ai.msg.Telegram;



public class EnemyTank extends Tank {
    public StateMachine<EnemyTank, TankState> state;
    private Vector2 currentPlayerPosition = new Vector2(0, 0);
    private Vector2 nextPatrollingPosition;

    public EnemyTank() {
        state = new DefaultStateMachine<EnemyTank, TankState>(this, TankState.PATROLLING);
    }

    public void update(float delta, float playerX, float playerY) {
        state.update();
        this.currentPlayerPosition.x = playerX;
        this.currentPlayerPosition.y = playerY;
    }

    public boolean isCloseToPlayer() {
        boolean lessThanThreeTilesAway = ( 
            Math.abs((currentPlayerPosition.x - sprite.getX()) / BattleBlast.TILE_WIDTH) <= 3 &&
            Math.abs((currentPlayerPosition.y - sprite.getY()) / BattleBlast.TILE_WIDTH) <= 3);
        return lessThanThreeTilesAway;
    }

    public void patrol() {
        if (nextPatrollingPosition == null) {
            // TODO - set the next patrolling positions within a circular structure
            nextPatrollingPosition = new Vector2(320, 320);
        }
        moveTowards(nextPatrollingPosition);
        if (nPercentChance(2)) shoot();
    }

    public void chase() {
        moveTowards(currentPlayerPosition);
        if (nPercentChance(3)) shoot();
    }

    private void moveTowards(Vector2 position) {
        Vector2 graphStartPosition = new Vector2(simplify(sprite.getX()), simplify(sprite.getY()));
        Vector2 graphTargetPosition = new Vector2(simplify(position.x), simplify(position.y));
        Node nextNode = new PathFinding(getCurrentWalls()).getNextNode(graphStartPosition, graphTargetPosition);
        if (nextNode != null) {
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

    private boolean nPercentChance(int n) {
        return Math.random() <= 0.01 * n;
    }

    private enum TankState implements State<EnemyTank> {
        PATROLLING() {
            @Override
            public void update(EnemyTank tank) {
                if (tank.isCloseToPlayer()) {
                    tank.state.changeState(CHASING);
                } else {
                    tank.patrol();
                }
            }
        },
        CHASING() {
            @Override
            public void update(EnemyTank tank) {
                if (!tank.isCloseToPlayer() || Math.random() <= 0.10) {
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
