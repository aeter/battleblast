package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.utils.Array;


public class Node {
    public final int x;
    public final int y;
    public boolean passable;
    public final int index;
    public Array<Connection<Node>> connections;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        int mapHeightInTiles = 20; // TODO - calculate instead of hardcoding
        this.index = x * mapHeightInTiles + y;
        this.connections = new Array<Connection<Node>>();
    }

}
