package me.battleblast.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import com.badlogic.gdx.graphics.g2d.Sprite;

import me.battleblast.pathfinding.Node;
import me.battleblast.pathfinding.PathFinding;
import me.battleblast.screens.GameScreen;


public class EnemyTank extends Tank {
    private long lastDecision = TimeUtils.millis();
    private static final int ONE_SECOND_IN_MILLISECONDS = 1000;

    public void actClever() {
        Array walls = new Array<Vector2>();
        for (Sprite s: GameScreen.ALL_BREAKABLE_OBSTACLES) {
            walls.add(new Vector2(s.getX(), s.getY()));
        }
        for (Vector2 v: GameScreen.ALL_UNBREAKABLE_OBSTACLES) {
            walls.add(v);
        }
        // TODO - now we have all walls (for this graphics frame
        // so we need to do the pathfinding magic
        //
        /*
        TODO - remove after tests pass
        Gdx.app.log("in render", "in render");
        Vector2 s = new Vector2(0, 0);
        Vector2 t = new Vector2(0, 2);
        Node n = new PathFinding().getNextNode(s, t);
        Gdx.app.log("nextNode", String.format("x: %d, y: %d", n.x, n.y));
        */

        
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
