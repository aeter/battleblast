package me.battleblast.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.DefaultConnection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import me.battleblast.BattleBlast;


public class AGraph implements IndexedGraph<Node> {
    // so we don't have to type 'BattleBlast.MAP_WI...'
    private static final int MAP_WIDTH = BattleBlast.MAP_WIDTH;
    private static final int MAP_HEIGHT = BattleBlast.MAP_HEIGHT;

    private Node[][] map;

    public AGraph(Array<Vector2> walls) {
        initNodes();
        setupWalls(walls);
        setupNodeConnections();
    }

    @Override
    public int getIndex(Node node) {
        return node.x * MAP_HEIGHT + node.y;
    }

    @Override
    public Array<Connection<Node>> getConnections(Node node) {
        return node.connections;
    }

    @Override
    public int getNodeCount() {
        return MAP_WIDTH * MAP_HEIGHT;
    }

    public Node[][] getMap() {
        return map;
    }

    private void setupNodeConnections() {
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                Node node = map[x][y];
                Node right = x < MAP_WIDTH - 1 ? map[x+1][y] : null;
                Node top = y < MAP_HEIGHT - 1 ? map[x][y+1] : null;
                Node left = x > 0 ? map[x-1][y] : null;
                Node bottom = y > 0 ? map[x][y-1] : null;
                // TODO - A tank uses 4 nodes (sprite is 64x64), mark adjacent
                // nodes as impassable for the pathfinding (i.e. no connections)
                //
                // @@
                // @@
                // ^
                // |
                // we use this tile as starting point, all 4 adjacent tiles
                // should be passable in order to add connections
                // For example, this is No for connections if we move into
                // position: (& denoting an obstacle):
                // @&
                // @@
                //
                if (right != null  && !right.isWall)
                    node.connections.add(new DefaultConnection<Node>(node, right));
                if (top != null && !top.isWall)
                    node.connections.add(new DefaultConnection<Node>(node, top));
                if (left != null && !left.isWall)
                    node.connections.add(new DefaultConnection<Node>(node, left));
                if (bottom != null && !bottom.isWall)
                    node.connections.add(new DefaultConnection<Node>(node, bottom));
            }
        }
    }

    private void initNodes() {
        map = new Node[MAP_WIDTH][MAP_HEIGHT];
        for (int x = 0; x < MAP_WIDTH; x++) {
            for (int y = 0; y < MAP_HEIGHT; y++) {
                map[x][y] = new Node(x, y);
            }
        }
    }

    private void setupWalls(Array<Vector2> walls) {
        for (Vector2 wall: walls) {
            map[toInt(wall.x)][toInt(wall.y)].isWall = true;
        }
    }

    private int toInt(float f) {
        return MathUtils.floor(f);
    }
}
