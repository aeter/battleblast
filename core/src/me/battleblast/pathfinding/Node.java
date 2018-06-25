package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;

import me.battleblast.BattleBlast;


public class Node {
    public final int x;
    public final int y;
    public final int index;
    public boolean isWall;
    public Array<Connection<Node>> connections;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.isWall = false;
        this.index = x * BattleBlast.MAP_HEIGHT + y;
        this.connections = new Array<Connection<Node>>();
    }
}
