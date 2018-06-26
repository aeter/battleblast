package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;


public class Node {
    public final int x;
    public final int y;
    public boolean isWall;
    public Array<Connection<Node>> connections;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.isWall = false;
        this.connections = new Array<Connection<Node>>();
    }
}
